package com.luizguilherme.view;

import com.luizguilherme.controller.ProdutoController;
import com.luizguilherme.model.Produto;

import javax.swing.*;
import java.awt.*;

public class ProdutoView extends JFrame {

    private JTextField txtNome;
    private JTextField txtEstoque;
    private JButton btnSalvar;
    private ProdutoController produtoController;

    public ProdutoView() {
        // Inicializa o Controller para comunicar com o banco de dados
        produtoController = new ProdutoController();

        // Configurações principais da Janela
        setTitle("SisCom - Cadastro de Produto Simples");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela no meio da tela
        
        // Define o layout como uma grade (Grid) de 3 linhas e 2 colunas
        setLayout(new GridLayout(3, 2, 10, 10));

        // Adicionando os componentes (Rótulos e Campos de Texto)
        add(new JLabel(" Nome do Produto:"));
        txtNome = new JTextField();
        add(txtNome);

        add(new JLabel(" Quantidade em Estoque:"));
        txtEstoque = new JTextField();
        add(txtEstoque);

        // Configuração do botão
        btnSalvar = new JButton("Salvar no Banco");
        add(new JLabel()); // Adiciona um espaço vazio na grade para alinhar o botão à direita
        add(btnSalvar);

        // Adiciona a ação de clique no botão
        btnSalvar.addActionListener(e -> salvarProduto());
    }

    private void salvarProduto() {
        try {
            // Captura os dados da tela e coloca no Objeto Produto
            Produto produto = new Produto();
            produto.setNome(txtNome.getText());
            produto.setQtde_estoque(Double.parseDouble(txtEstoque.getText()));
            
            // Preenchendo com zeros os valores financeiros para não dar erro, 
            // já que é um teste inicial simples.
            produto.setPreco_medio(0.0);
            produto.setValor_ultima_compra(0.0);
            produto.setValor_ultima_venda(0.0);

            // Chama o Controller para salvar no banco
            boolean sucesso = produtoController.salvar(produto);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Produto salvo com sucesso no banco SQLite!");
                // Limpa os campos após salvar
                txtNome.setText("");
                txtEstoque.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            // Validação simples caso o usuário digite texto no lugar de números
            JOptionPane.showMessageDialog(this, "Por favor, insira um número válido para o estoque.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}