package com.luizguilherme.view;

import com.luizguilherme.controller.CategoriaController;
import com.luizguilherme.model.Categoria;

import javax.swing.*;
import java.awt.*;

public class CategoriaView extends JFrame {

    private JTextField txtNome;
    private JButton btnSalvar;
    private CategoriaController categoriaController;

    public CategoriaView() {
        categoriaController = new CategoriaController();

        setTitle("SisCom - Cadastro de Categoria");
        setSize(350, 150);
        // DISPOSE_ON_CLOSE fecha apenas esta janela, não o programa todo
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridLayout(2, 2, 10, 10));

        add(new JLabel(" Nome da Categoria:"));
        txtNome = new JTextField();
        add(txtNome);

        btnSalvar = new JButton("Salvar");
        add(new JLabel()); // Espaço vazio para alinhar o botão
        add(btnSalvar);

        btnSalvar.addActionListener(e -> salvarCategoria());
    }

    private void salvarCategoria() {
        String nome = txtNome.getText();
        
        if (nome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome da categoria não pode ser vazio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Categoria categoria = new Categoria();
        categoria.setNome(nome);

        if (categoriaController.salvar(categoria)) {
            JOptionPane.showMessageDialog(this, "Categoria salva com sucesso!");
            txtNome.setText(""); // Limpa o campo
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar a categoria.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}