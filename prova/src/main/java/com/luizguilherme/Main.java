package com.luizguilherme;

import com.luizguilherme.util.JPAUtil;
import com.luizguilherme.view.ProdutoView;
import jakarta.persistence.EntityManager;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando o sistema SisCom...");

        // 1. Inicia o EntityManager para forçar o Hibernate a criar o arquivo SQLite e as tabelas
        EntityManager em = JPAUtil.getEntityManager();
        if (em.isOpen()) {
            System.out.println("Banco de dados SQLite inicializado/atualizado com sucesso!");
            em.close();
        }

        // 2. Inicia a Interface Gráfica de forma segura utilizando a thread do Swing
        SwingUtilities.invokeLater(() -> {
            ProdutoView tela = new ProdutoView();
            tela.setVisible(true);
        });
        
        // Obs: Removemos o JPAUtil.close() daqui do final, pois agora a aplicação 
        // vai ficar rodando continuamente aguardando você interagir com a tela.
    }
}