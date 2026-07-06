package com.luizguilherme.controller;

import com.luizguilherme.util.JPAUtil;
import jakarta.persistence.EntityManager;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

import java.io.InputStream;
import java.util.HashMap;

public class RelatorioController {

    private void gerarRelatorio(String caminhoJrxml) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            InputStream relatorioStream = getClass().getResourceAsStream(caminhoJrxml);
            if (relatorioStream == null) {
                System.out.println("Erro: Arquivo " + caminhoJrxml + " não encontrado!");
                return;
            }

            JasperReport relatorioCompilado = JasperCompileManager.compileReport(relatorioStream);
            Session session = em.unwrap(Session.class);
            
            session.doWork(conexao -> {
                try {
                    JasperPrint jasperPrint = JasperFillManager.fillReport(relatorioCompilado, new HashMap<>(), conexao);
                    JasperViewer.viewReport(jasperPrint, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void emitirRelatorioVendas() {
        gerarRelatorio("/relatorios/RelatorioVendas.jrxml");
    }

    public void emitirRelatorioCompras() {
        gerarRelatorio("/relatorios/RelatorioCompras.jrxml");
    }

    public void emitirRelatorioFinanceiro() {
        gerarRelatorio("/relatorios/RelatorioFinanceiro.jrxml");
    }
}