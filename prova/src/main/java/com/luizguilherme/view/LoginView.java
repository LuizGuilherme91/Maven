package com.luizguilherme.view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public LoginView() {
        setTitle("SisCom - Acesso Restrito");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        
        // Layout de grid para organizar os campos
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel(" Usuário:"));
        txtUsuario = new JTextField();
        add(txtUsuario);

        add(new JLabel(" Senha:"));
        txtSenha = new JPasswordField();
        add(txtSenha);

        add(new JLabel()); // Espaço vazio para alinhar o botão à direita
        btnEntrar = new JButton("Entrar");
        add(btnEntrar);

        // Ação do botão de login
        btnEntrar.addActionListener(e -> fazerLogin());
    }

    private void fazerLogin() {
        String usuario = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        // RF015 e RNF-S001: Verificação de credenciais
        if (usuario.equals("admin") && senha.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Bem-vindo ao SisCom!");
            this.dispose(); // Fecha a tela de login
            
            // Abre o menu principal do sistema
            SwingUtilities.invokeLater(() -> {
                MenuView menu = new MenuView();
                menu.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
        }
    }
}