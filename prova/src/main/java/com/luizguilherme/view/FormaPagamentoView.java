package com.luizguilherme.view;

import com.luizguilherme.controller.FormaPagamentoController;
import com.luizguilherme.model.FormaPagamento;

import javax.swing.*;
import java.awt.*;

public class FormaPagamentoView extends JFrame {

    private JTextField txtNome, txtQtdeParcelas, txtPrazo;
    private JComboBox<String> cbAvistaPrazo;
    private JButton btnSalvar;
    private FormaPagamentoController formaPagamentoController;

    public FormaPagamentoView() {
        formaPagamentoController = new FormaPagamentoController();

        setTitle("SisCom - Cadastro Forma de Pagamento");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel(" Nome (Ex: Cartão 3x, Pix):"));
        txtNome = new JTextField();
        add(txtNome);

        add(new JLabel(" Qtd. de Parcelas:"));
        txtQtdeParcelas = new JTextField("1"); // Padrão 1
        add(txtQtdeParcelas);

        add(new JLabel(" Prazo entre Parcelas (Dias):"));
        txtPrazo = new JTextField("30"); // Padrão 30 dias
        add(txtPrazo);

        add(new JLabel(" Modalidade:"));
        cbAvistaPrazo = new JComboBox<>(new String[]{"0 - À Vista", "1 - A Prazo"});
        add(cbAvistaPrazo);

        btnSalvar = new JButton("Salvar");
        add(new JLabel()); // Espaço vazio
        add(btnSalvar);

        btnSalvar.addActionListener(e -> salvarFormaPagamento());
    }

    private void salvarFormaPagamento() {
        try {
            FormaPagamento forma = new FormaPagamento();
            forma.setNome(txtNome.getText());
            forma.setQtde_parcela(Integer.parseInt(txtQtdeParcelas.getText()));
            forma.setPrazo(Integer.parseInt(txtPrazo.getText()));
            
            // Pega o primeiro caractere da seleção (0 ou 1)
            int modalidade = Integer.parseInt(cbAvistaPrazo.getSelectedItem().toString().substring(0, 1));
            forma.setAvista_aprazo(modalidade);

            if (formaPagamentoController.salvar(forma)) {
                JOptionPane.showMessageDialog(this, "Forma de Pagamento salva com sucesso!");
                txtNome.setText("");
                txtQtdeParcelas.setText("1");
                txtPrazo.setText("30");
                cbAvistaPrazo.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira apenas números nas parcelas e prazo.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}