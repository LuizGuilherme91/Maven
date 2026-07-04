package com.luizguilherme;

import com.luizguilherme.util.JPAUtil;
import com.luizguilherme.view.LoginView;
import jakarta.persistence.EntityManager;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando o sistema SisCom...");

        // Garante que o banco de dados e as tabelas estejam criadas no SQLite
        EntityManager em = JPAUtil.getEntityManager();
        if (em.isOpen()) {
            System.out.println("Banco de dados verificado com sucesso!");
            em.close();
        }

        // Inicia a aplicação exibindo a tela de Login
        SwingUtilities.invokeLater(() -> {
            LoginView tela = new LoginView();
            tela.setVisible(true);
        });
    }
}