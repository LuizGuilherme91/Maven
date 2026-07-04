package com.luizguilherme.view;

import com.luizguilherme.controller.TipoContaController;
import com.luizguilherme.model.TipoConta;

import javax.swing.*;
import java.awt.*;

public class TipoContaView extends JFrame {

    private JTextField txtDescricao;
    private JButton btnSalvar;
    private TipoContaController tipoContaController;

    public TipoContaView() {
        tipoContaController = new TipoContaController();

        setTitle("SisCom - Cadastro de Tipo de Conta");
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridLayout(2, 2, 10, 10));

        add(new JLabel(" Descrição da Conta:"));
        txtDescricao = new JTextField();
        add(txtDescricao);

        btnSalvar = new JButton("Salvar");
        add(new JLabel()); // Espaço vazio na grid
        add(btnSalvar);

        btnSalvar.addActionListener(e -> salvarTipoConta());
    }

    private void salvarTipoConta() {
        if (txtDescricao.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "A descrição não pode ser vazia.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TipoConta tipo = new TipoConta();
        tipo.setDescricao(txtDescricao.getText());

        if (tipoContaController.salvar(tipo)) {
            JOptionPane.showMessageDialog(this, "Tipo de Conta salvo com sucesso!");
            txtDescricao.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o Tipo de Conta.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}