package com.luizguilherme.view;

import com.luizguilherme.controller.ClienteController;
import com.luizguilherme.model.Cliente;

import javax.swing.*;
import java.awt.*;

public class ClienteView extends JFrame {

    private JTextField txtNome, txtCpf, txtRg, txtEndereco, txtTelefone;
    private JButton btnSalvar;
    private ClienteController clienteController;

    public ClienteView() {
        clienteController = new ClienteController();

        setTitle("SisCom - Cadastro de Cliente");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha só esta janela
        setLocationRelativeTo(null);
        
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel(" Nome:"));
        txtNome = new JTextField();
        add(txtNome);

        add(new JLabel(" CPF:"));
        txtCpf = new JTextField();
        add(txtCpf);

        add(new JLabel(" RG:"));
        txtRg = new JTextField();
        add(txtRg);

        add(new JLabel(" Endereço:"));
        txtEndereco = new JTextField();
        add(txtEndereco);

        add(new JLabel(" Telefone:"));
        txtTelefone = new JTextField();
        add(txtTelefone);

        btnSalvar = new JButton("Salvar Cliente");
        add(new JLabel()); // Espaço vazio na grid
        add(btnSalvar);

        btnSalvar.addActionListener(e -> salvarCliente());
    }

    private void salvarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome(txtNome.getText());
        cliente.setCpf(txtCpf.getText());
        cliente.setRg(txtRg.getText());
        cliente.setEndereco(txtEndereco.getText());
        cliente.setTelefone(txtTelefone.getText());

        if (clienteController.salvar(cliente)) {
            JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso no banco de dados!");
            // Limpa os campos após salvar
            txtNome.setText("");
            txtCpf.setText("");
            txtRg.setText("");
            txtEndereco.setText("");
            txtTelefone.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar cliente. O CPF é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}