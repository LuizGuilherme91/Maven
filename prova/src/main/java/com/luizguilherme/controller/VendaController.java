package com.luizguilherme.controller;

import com.luizguilherme.model.*;
import com.luizguilherme.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Controller responsável pelo motor de regras de negócio das Vendas.
 */
public class VendaController {

    // RNF-S002: Logger para registrar cada método executado
    private static final Logger logger = LogManager.getLogger(VendaController.class);

    /**
     * Registra a venda, atualiza estoques e gera o financeiro de forma atômica.
     */
    public boolean registrarVenda(Venda venda, FormaPagamento formaPgto, TipoConta tipoConta) {
        logger.info("Executando método registrarVenda...");
        
        // Abrimos a conexão diretamente para usar o controle de Transação (Tudo ou Nada)
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // RNF-P004: Verificar limite de 3 vendas por CPF dentro do mesmo mês
            if (venda.getCliente() != null) {
                if (!verificarLimiteVendasCpf(em, venda.getCliente().getCpf(), venda.getData_venda())) {
                    logger.warn("Venda abortada: O cliente excedeu o limite de 3 vendas mensais.");
                    tx.rollback();
                    return false;
                }
            }

            // Como a modelagem M:N não possui uma classe 'ItemVenda' definindo a quantidade
            // e o valor unitário exato, vamos simular o valor unitário dividindo o total.
            double valorUnitarioSimulado = 0;
            if (venda.getProdutos() != null && !venda.getProdutos().isEmpty()) {
                valorUnitarioSimulado = venda.getValor_total() / venda.getProdutos().size();
            }

            // Processar os Produtos da Venda
            if (venda.getProdutos() != null) {
                for (Produto p : venda.getProdutos()) {
                    // Trazemos o produto atualizado do banco de dados
                    Produto produtoBD = em.find(Produto.class, p.getId());

                    // RNF-P003: O sistema não pode realizar venda com estoque inferior a 1
                    if (produtoBD.getQtde_estoque() < 1) {
                        logger.warn("Venda abortada: Produto '" + produtoBD.getNome() + "' sem estoque suficiente.");
                        tx.rollback();
                        return false;
                    }

                    // RNF-P001: Atualizar o estoque para menos (subtraindo 1 unidade)
                    produtoBD.setQtde_estoque(produtoBD.getQtde_estoque() - 1);

                    // RNF-P005: Atualizar o campo valor_ultima_venda
                    produtoBD.setValor_ultima_venda(valorUnitarioSimulado);

                    // Salva a alteração do produto no banco
                    em.merge(produtoBD);
                }
            }

            // RNF-P009 e RF010: Ao gerar uma venda, deve-se gerar uma conta a receber
            Financeiro financeiro = new Financeiro();
            financeiro.setData_conta(venda.getData_venda());
            
            // RNF-P010: Como é Venda, o status de pagar_ou_receber deve ser 1
            financeiro.setPagar_ou_receber(1); 
            financeiro.setFormaPagamento(formaPgto);
            financeiro.setTipoConta(tipoConta);

            // RNF-P011: Status, parcelas e prazos gerenciados pela Forma de Pagamento
            List<FinanceiroParcela> parcelas = gerarParcelas(financeiro, venda.getValor_total(), formaPgto);
            financeiro.setParcelas(parcelas);

            // Vincula o financeiro à venda
            venda.setFinanceiro(financeiro);

            // O comando persist salva a Venda, e graças ao CascadeType.ALL nas anotações, 
            // ele salva o Financeiro e todas as Parcelas automaticamente.
            em.persist(venda);

            tx.commit(); // Confirma todas as operações no banco de dados
            logger.info("Venda registrada com sucesso junto ao financeiro e estoque atualizado!");
            return true;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback(); // Se der erro, desfaz tudo para não corromper os dados
            logger.error("Erro fatal ao executar método registrarVenda", e);
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * RNF-P004: Verifica se o CPF tem menos de 3 vendas no mês da compra atual.
     */
    private boolean verificarLimiteVendasCpf(EntityManager em, String cpf, Date dataVenda) {
        logger.info("Executando método verificarLimiteVendasCpf...");
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataVenda);

        // Define a data para o primeiro dia do mês à meia-noite
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date inicioMes = cal.getTime();

        // Define a data para o último segundo do mês
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date fimMes = cal.getTime();

        // Conta quantas vendas existem para esse CPF neste intervalo
        String jpql = "SELECT COUNT(v) FROM Venda v WHERE v.cliente.cpf = :cpf " +
                    "AND v.data_venda >= :inicio AND v.data_venda <= :fim";

        Long quantidadeVendas = em.createQuery(jpql, Long.class)
                .setParameter("cpf", cpf)
                .setParameter("inicio", inicioMes)
                .setParameter("fim", fimMes)
                .getSingleResult();

        return quantidadeVendas < 3;
    }

    /**
     * RNF-P011: Gera a lista de parcelas financeiras baseada na Forma de Pagamento.
     */
    private List<FinanceiroParcela> gerarParcelas(Financeiro financeiro, double valorTotal, FormaPagamento formaPgto) {
        logger.info("Executando método gerarParcelas...");
        List<FinanceiroParcela> parcelas = new ArrayList<>();
        
        // Garante que não ocorra divisão por zero
        int qtde = formaPgto.getQtde_parcela() > 0 ? formaPgto.getQtde_parcela() : 1;
        double valorParcela = valorTotal / qtde;

        Calendar cal = Calendar.getInstance();
        cal.setTime(financeiro.getData_conta());

        for (int i = 1; i <= qtde; i++) {
            FinanceiroParcela parcela = new FinanceiroParcela();
            parcela.setFinanceiro(financeiro);
            parcela.setN_parcela(i);
            parcela.setValor_original(valorParcela);
            parcela.setValor_final(valorParcela);
            parcela.setStatus(0); // 0 = Status Pendente (não paga ainda)

            // Incrementa a data com o prazo estabelecido na forma de pagamento para cada parcela
            cal.add(Calendar.DAY_OF_MONTH, formaPgto.getPrazo());
            parcela.setData_vencimento(cal.getTime());

            parcelas.add(parcela);
        }
        return parcelas;
    }
}