package com.luizguilherme.controller;

import com.luizguilherme.model.*;
import com.luizguilherme.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Controller responsável pelo motor de regras de negócio das Compras.
 */
public class CompraController {

    // RNF-S002: Logger para registrar cada método executado
    private static final Logger logger = LogManager.getLogger(CompraController.class);

    /**
     * Registra a compra, atualiza estoques para mais e gera o financeiro a pagar.
     */
    public boolean registrarCompra(Compra compra, FormaPagamento formaPgto, TipoConta tipoConta) {
        logger.info("Executando método registrarCompra...");
        
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Como o diagrama não possui uma tabela intermediária com "quantidade comprada",
            // vamos simular que cada item na lista representa a compra de 1 unidade e ratear o valor.
            double valorUnitarioSimulado = 0;
            if (compra.getProdutos() != null && !compra.getProdutos().isEmpty()) {
                valorUnitarioSimulado = compra.getValor_total() / compra.getProdutos().size();
            }

            // Processar os Produtos da Compra
            if (compra.getProdutos() != null) {
                for (Produto p : compra.getProdutos()) {
                    // Busca o produto atualizado do banco
                    Produto produtoBD = em.find(Produto.class, p.getId());

                    // RNF-P002: Atualizar o estoque para mais (acrescentar 1 unidade por registro na lista)
                    produtoBD.setQtde_estoque(produtoBD.getQtde_estoque() + 1);

                    // RNF-P006: Atualizar o campo valor_ultima_compra
                    produtoBD.setValor_ultima_compra(valorUnitarioSimulado);

                    // RNF-P007: Atualizar o campo preco_medio 
                    // (Cálculo simplificado de média entre o preço médio atual e a nova compra)
                    double precoMedioAtual = produtoBD.getPreco_medio();
                    if (precoMedioAtual > 0) {
                        produtoBD.setPreco_medio((precoMedioAtual + valorUnitarioSimulado) / 2);
                    } else {
                        produtoBD.setPreco_medio(valorUnitarioSimulado); // Se for a primeira compra, o médio é o atual
                    }

                    // Salva a alteração do produto no banco
                    em.merge(produtoBD);
                }
            }

            // RF009 e RNF-P009: Ao gerar uma compra, deve-se gerar uma conta a pagar
            Financeiro financeiro = new Financeiro();
            financeiro.setData_conta(compra.getData_compra());
            
            // RNF-P010: Se a conta for gerada na compra, pagar_ou_receber deve ser 0
            financeiro.setPagar_ou_receber(0); 
            financeiro.setFormaPagamento(formaPgto);
            financeiro.setTipoConta(tipoConta);

            // RNF-P011: Status, parcelas e prazos gerenciados pela Forma de Pagamento
            List<FinanceiroParcela> parcelas = gerarParcelas(financeiro, compra.getValor_total(), formaPgto);
            financeiro.setParcelas(parcelas);

            // Vincula o financeiro à compra
            compra.setFinanceiro(financeiro);

            // Persiste a Compra, que salvará o Financeiro e Parcelas em Cascata
            em.persist(compra);

            tx.commit();
            logger.info("Compra registrada com sucesso! Estoque e contas a pagar atualizados.");
            return true;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erro fatal ao executar método registrarCompra", e);
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * RNF-P011: Gera a lista de parcelas financeiras a pagar.
     */
    private List<FinanceiroParcela> gerarParcelas(Financeiro financeiro, double valorTotal, FormaPagamento formaPgto) {
        logger.info("Executando método gerarParcelas da Compra...");
        List<FinanceiroParcela> parcelas = new ArrayList<>();
        
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
            parcela.setStatus(0); // 0 = Pendente

            // Aplica o prazo para cada parcela
            cal.add(Calendar.DAY_OF_MONTH, formaPgto.getPrazo());
            parcela.setData_vencimento(cal.getTime());

            parcelas.add(parcela);
        }
        return parcelas;
    }
}