package com.luizguilherme.view;

import com.luizguilherme.controller.FornecedorController;
import com.luizguilherme.model.Fornecedor;

import javax.swing.*;
import java.awt.*;

public class FornecedorView extends JFrame {

    private JTextField txtNomeFantasia, txtRazaoSocial, txtCnpj;
    private JButton btnSalvar;
    private FornecedorController fornecedorController;

    public FornecedorView() {
        fornecedorController = new FornecedorController();

        setTitle("SisCom - Cadastro de Fornecedor");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel(" Nome Fantasia:"));
        txtNomeFantasia = new JTextField();
        add(txtNomeFantasia);

        add(new JLabel(" Razão Social:"));
        txtRazaoSocial = new JTextField();
        add(txtRazaoSocial);

        add(new JLabel(" CNPJ:"));
        txtCnpj = new JTextField();
        add(txtCnpj);

        btnSalvar = new JButton("Salvar Fornecedor");
        add(new JLabel()); // Espaço vazio na grid
        add(btnSalvar);

        btnSalvar.addActionListener(e -> salvarFornecedor());
    }

    private void salvarFornecedor() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome_fantasia(txtNomeFantasia.getText());
        fornecedor.setRazao_social(txtRazaoSocial.getText());
        fornecedor.setCnpj(txtCnpj.getText());

        if (fornecedorController.salvar(fornecedor)) {
            JOptionPane.showMessageDialog(this, "Fornecedor salvo com sucesso!");
            txtNomeFantasia.setText("");
            txtRazaoSocial.setText("");
            txtCnpj.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar fornecedor. O CNPJ é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}