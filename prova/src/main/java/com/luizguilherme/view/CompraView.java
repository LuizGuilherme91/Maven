package com.luizguilherme.view;

import com.luizguilherme.controller.*;
import com.luizguilherme.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompraView extends JFrame {

    private JComboBox<Fornecedor> cbFornecedor;
    private JComboBox<Produto> cbProduto;
    private JComboBox<FormaPagamento> cbFormaPagamento;
    private JComboBox<TipoConta> cbTipoConta;
    private JTextField txtPrecoItem;
    
    private JButton btnAdicionarProduto;
    private JButton btnFinalizarCompra;
    
    private DefaultListModel<String> listModelCarrinho;
    private JList<String> lstCarrinho;
    private JLabel lblTotal;

    private List<Produto> carrinho;
    private double valorTotal = 0.0;

    private CompraController compraController;

    public CompraView() {
        compraController = new CompraController();
        carrinho = new ArrayList<>();

        setTitle("SisCom - Registrar Compra (Entrada de Estoque)");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ----- PAINEL SUPERIOR (Formulário) -----
        JPanel panelNorte = new JPanel(new GridLayout(6, 2, 5, 5));
        panelNorte.setBorder(BorderFactory.createTitledBorder("Dados da Compra"));

        panelNorte.add(new JLabel(" Fornecedor:"));
        cbFornecedor = new JComboBox<>();
        carregarFornecedores();
        panelNorte.add(cbFornecedor);

        panelNorte.add(new JLabel(" Produto:"));
        cbProduto = new JComboBox<>();
        carregarProdutos();
        panelNorte.add(cbProduto);

        panelNorte.add(new JLabel(" Preço de Custo (Unidade):"));
        txtPrecoItem = new JTextField("0.00");
        panelNorte.add(txtPrecoItem);

        panelNorte.add(new JLabel(" Forma de Pagamento:"));
        cbFormaPagamento = new JComboBox<>();
        carregarFormasPagamento();
        panelNorte.add(cbFormaPagamento);

        panelNorte.add(new JLabel(" Tipo de Conta:"));
        cbTipoConta = new JComboBox<>();
        carregarTiposConta();
        panelNorte.add(cbTipoConta);

        panelNorte.add(new JLabel()); // Espaço vazio
        btnAdicionarProduto = new JButton("Adicionar Produto");
        panelNorte.add(btnAdicionarProduto);

        add(panelNorte, BorderLayout.NORTH);

        // ----- PAINEL CENTRAL (Lista de Itens) -----
        listModelCarrinho = new DefaultListModel<>();
        lstCarrinho = new JList<>(listModelCarrinho);
        JScrollPane scrollCarrinho = new JScrollPane(lstCarrinho);
        scrollCarrinho.setBorder(BorderFactory.createTitledBorder("Itens da Compra"));
        add(scrollCarrinho, BorderLayout.CENTER);

        // ----- PAINEL INFERIOR (Total e Finalização) -----
        JPanel panelSul = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total da Compra: R$ 0.00   ");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        
        btnFinalizarCompra = new JButton("Finalizar Compra");
        btnFinalizarCompra.setBackground(new Color(70, 130, 180)); // Azul aço
        btnFinalizarCompra.setForeground(Color.WHITE);

        panelSul.add(lblTotal);
        panelSul.add(btnFinalizarCompra);
        add(panelSul, BorderLayout.SOUTH);

        // ----- AÇÕES DOS BOTÕES -----
        btnAdicionarProduto.addActionListener(e -> adicionarAoCarrinho());
        btnFinalizarCompra.addActionListener(e -> finalizarCompra());
    }

    // --- CARREGAMENTO DOS DADOS ---
    private void carregarFornecedores() {
        List<Fornecedor> fornecedores = new FornecedorController().pesquisarTodos();
        for (Fornecedor f : fornecedores) {
            cbFornecedor.addItem(f);
        }
        cbFornecedor.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Fornecedor) setText(((Fornecedor) value).getNome_fantasia());
                return this;
            }
        });
    }

    private void carregarProdutos() {
        List<Produto> produtos = new ProdutoController().pesquisarTodos();
        for (Produto p : produtos) {
            cbProduto.addItem(p);
        }
        cbProduto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Produto) setText(((Produto) value).getNome() + " (Estoque atual: " + ((Produto) value).getQtde_estoque() + ")");
                return this;
            }
        });
    }

    private void carregarFormasPagamento() {
        List<FormaPagamento> formas = new FormaPagamentoController().pesquisarTodos();
        for (FormaPagamento f : formas) {
            cbFormaPagamento.addItem(f);
        }
        cbFormaPagamento.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof FormaPagamento) setText(((FormaPagamento) value).getNome());
                return this;
            }
        });
    }

    private void carregarTiposConta() {
        List<TipoConta> tipos = new TipoContaController().pesquisarTodos();
        for (TipoConta t : tipos) {
            cbTipoConta.addItem(t);
        }
        cbTipoConta.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoConta) setText(((TipoConta) value).getDescricao());
                return this;
            }
        });
    }

    // --- LÓGICA DE COMPRA ---
    private void adicionarAoCarrinho() {
        Produto produtoSelecionado = (Produto) cbProduto.getSelectedItem();
        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.");
            return;
        }

        try {
            double preco = Double.parseDouble(txtPrecoItem.getText().replace(",", "."));
            if (preco <= 0) {
                JOptionPane.showMessageDialog(this, "O preço deve ser maior que zero.");
                return;
            }

            carrinho.add(produtoSelecionado);
            valorTotal += preco;

            listModelCarrinho.addElement(produtoSelecionado.getNome() + " - Custo: R$ " + String.format("%.2f", preco));
            lblTotal.setText("Total da Compra: R$ " + String.format("%.2f", valorTotal) + "   ");
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite um preço válido (ex: 15.50).");
        }
    }

    private void finalizarCompra() {
        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione produtos antes de finalizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Fornecedor fornecedor = (Fornecedor) cbFornecedor.getSelectedItem();
        FormaPagamento formaPgto = (FormaPagamento) cbFormaPagamento.getSelectedItem();
        TipoConta tipoConta = (TipoConta) cbTipoConta.getSelectedItem();

        if (fornecedor == null || formaPgto == null || tipoConta == null) {
            JOptionPane.showMessageDialog(this, "Selecione Fornecedor, Forma de Pagamento e Tipo de Conta.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Compra novaCompra = new Compra();
        novaCompra.setFornecedor(fornecedor);
        novaCompra.setData_compra(new Date());
        novaCompra.setValor_total(valorTotal);
        novaCompra.setProdutos(new ArrayList<>(carrinho));

        boolean sucesso = compraController.registrarCompra(novaCompra, formaPgto, tipoConta);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Compra registrada com sucesso!\nO estoque foi abastecido e o Contas a Pagar foi gerado.");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao registrar a compra.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}