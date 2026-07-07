package com.luizguilherme.controller;

import com.luizguilherme.model.FinanceiroParcela;
import com.luizguilherme.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

public class FinanceiroController {

    private static final Logger logger = LogManager.getLogger(FinanceiroController.class);

    public boolean darBaixaParcela(int idParcela, Date dataPagamento, double desconto, double acrescimo) {
        logger.info("Executando método darBaixaParcela para a parcela ID: " + idParcela);

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Busca a parcela específica no banco de dados
            FinanceiroParcela parcela = em.find(FinanceiroParcela.class, idParcela);

            if (parcela == null) {
                logger.warn("Baixa abortada: Parcela não encontrada no banco.");
                tx.rollback();
                return false;
            }

            // Verifica se a conta já não foi paga (0 = Pendente, 1 = Paga)
            if (parcela.getStatus() == 1) {
                logger.warn("Baixa abortada: A parcela selecionada já consta como paga.");
                tx.rollback();
                return false;
            }

            // Atualiza os dados do pagamento
            parcela.setData_pagamento(dataPagamento);
            parcela.setDesconto(desconto);
            parcela.setAcrescimo(acrescimo);

            // Calcula o valor final que foi efetivamente pago/recebido
            double valorFinal = parcela.getValor_original() + acrescimo - desconto;
            
            // Tratamento de segurança para não deixar o valor final ficar negativo
            if (valorFinal < 0) {
                valorFinal = 0;
            }
            parcela.setValor_final(valorFinal);

            // Altera o status para 1 (Conta baixada/paga)
            parcela.setStatus(1);

            // Salva a atualização no banco de dados
            em.merge(parcela);
            tx.commit();

            logger.info("Baixa realizada com sucesso! Valor final consolidado: R$ " + valorFinal);
            return true;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erro fatal ao dar baixa na parcela", e);
            return false;
        } finally {
            em.close();
        }
    }

    public List<FinanceiroParcela> listarParcelasPendentes() {
        logger.info("Buscando lista de parcelas pendentes...");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // JPQL para filtrar apenas as parcelas com status 0
            return em.createQuery("SELECT p FROM FinanceiroParcela p WHERE p.status = 0", FinanceiroParcela.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<FinanceiroParcela> listarParcelasPagas() {
        logger.info("Buscando lista de parcelas pagas...");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM FinanceiroParcela p WHERE p.status = 1", FinanceiroParcela.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}