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


public class CompraController {

    private static final Logger logger = LogManager.getLogger(CompraController.class);

    public boolean registrarCompra(Compra compra, FormaPagamento formaPgto, TipoConta tipoConta) {
        logger.info("Executando método registrarCompra...");
        
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            double valorUnitarioSimulado = 0;
            if (compra.getProdutos() != null && !compra.getProdutos().isEmpty()) {
                valorUnitarioSimulado = compra.getValor_total() / compra.getProdutos().size();
            }

            // Processar os Produtos da Compra
            if (compra.getProdutos() != null) {
                for (Produto p : compra.getProdutos()) {
                    Produto produtoBD = em.find(Produto.class, p.getId());

                    produtoBD.setQtde_estoque(produtoBD.getQtde_estoque() + 1);

                    produtoBD.setValor_ultima_compra(valorUnitarioSimulado);

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

            Financeiro financeiro = new Financeiro();
            financeiro.setData_conta(compra.getData_compra());
            
            financeiro.setPagar_ou_receber(0); 
            financeiro.setFormaPagamento(formaPgto);
            financeiro.setTipoConta(tipoConta);

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