package com.luizguilherme.view;

import com.luizguilherme.controller.*;
import com.luizguilherme.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendaView extends JFrame {

    private JComboBox<Cliente> cbCliente;
    private JComboBox<Produto> cbProduto;
    private JTextField txtPrecoVenda; // <-- NOVO CAMPO DE PREÇO AQUI
    private JComboBox<FormaPagamento> cbFormaPagamento;
    private JComboBox<TipoConta> cbTipoConta;
    
    private JButton btnAdicionarProduto;
    private JButton btnFinalizarVenda;
    
    private DefaultListModel<String> listModelCarrinho;
    private JList<String> lstCarrinho;
    private JLabel lblTotal;

    private List<Produto> carrinho;
    private double valorTotal = 0.0;

    private VendaController vendaController;

    public VendaView() {
        vendaController = new VendaController();
        carrinho = new ArrayList<>();

        setTitle("SisCom - Registrar Venda");
        setSize(700, 550); // Aumentei um pouquinho a tela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ----- PAINEL SUPERIOR (Formulário de Seleção) -----
        // Aumentei de 5 para 6 linhas para caber o novo campo
        JPanel panelNorte = new JPanel(new GridLayout(6, 2, 5, 5));
        panelNorte.setBorder(BorderFactory.createTitledBorder("Dados da Venda"));

        panelNorte.add(new JLabel(" Cliente:"));
        cbCliente = new JComboBox<>();
        carregarClientes();
        panelNorte.add(cbCliente);

        panelNorte.add(new JLabel(" Produto:"));
        cbProduto = new JComboBox<>();
        carregarProdutos();
        panelNorte.add(cbProduto);

        // --- ADICIONANDO O CAMPO DE PREÇO NA TELA ---
        panelNorte.add(new JLabel(" Preço de Venda (R$):"));
        txtPrecoVenda = new JTextField("0.00");
        panelNorte.add(txtPrecoVenda);

        panelNorte.add(new JLabel(" Forma de Pagamento:"));
        cbFormaPagamento = new JComboBox<>();
        carregarFormasPagamento();
        panelNorte.add(cbFormaPagamento);

        panelNorte.add(new JLabel(" Tipo de Conta:"));
        cbTipoConta = new JComboBox<>();
        carregarTiposConta();
        panelNorte.add(cbTipoConta);

        panelNorte.add(new JLabel()); // Espaço vazio
        btnAdicionarProduto = new JButton("Adicionar Produto ao Carrinho");
        panelNorte.add(btnAdicionarProduto);

        add(panelNorte, BorderLayout.NORTH);

        // ----- PAINEL CENTRAL (Carrinho de Compras) -----
        listModelCarrinho = new DefaultListModel<>();
        lstCarrinho = new JList<>(listModelCarrinho);
        JScrollPane scrollCarrinho = new JScrollPane(lstCarrinho);
        scrollCarrinho.setBorder(BorderFactory.createTitledBorder("Carrinho de Compras"));
        add(scrollCarrinho, BorderLayout.CENTER);

        // ----- PAINEL INFERIOR (Total e Finalização) -----
        JPanel panelSul = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: R$ 0.00   ");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        
        btnFinalizarVenda = new JButton("Finalizar Venda");
        btnFinalizarVenda.setBackground(new Color(34, 139, 34)); // Cor verde
        btnFinalizarVenda.setForeground(Color.WHITE);

        panelSul.add(lblTotal);
        panelSul.add(btnFinalizarVenda);
        add(panelSul, BorderLayout.SOUTH);

        // ----- AÇÕES DOS BOTÕES -----
        btnAdicionarProduto.addActionListener(e -> adicionarAoCarrinho());
        btnFinalizarVenda.addActionListener(e -> finalizarVenda());
        
        // Ação para facilitar: se o usuário selecionar um produto, puxar o preço médio dele para a caixinha
        cbProduto.addActionListener(e -> {
            Produto p = (Produto) cbProduto.getSelectedItem();
            if (p != null) {
                // Sugere o preço médio, mas deixa o usuário apagar e digitar outro
                txtPrecoVenda.setText(String.format("%.2f", p.getPreco_medio()).replace(",", "."));
            }
        });
    }

    // --- MÉTODOS PARA CARREGAR DADOS DO BANCO NOS COMBOBOXES ---
    
    private void carregarClientes() {
        List<Cliente> clientes = new ClienteController().pesquisarTodos();
        for (Cliente c : clientes) {
            cbCliente.addItem(c);
        }
        cbCliente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente) setText(((Cliente) value).getNome());
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
                if (value instanceof Produto) setText(((Produto) value).getNome() + " (Estoque: " + ((Produto) value).getQtde_estoque() + ")");
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

    // --- LÓGICA DA TELA ---

    private void adicionarAoCarrinho() {
        Produto produtoSelecionado = (Produto) cbProduto.getSelectedItem();
        
        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto primeiro.");
            return;
        }

        try {
            // Lemos o preço que o usuário digitou na caixinha nova
            double precoProduto = Double.parseDouble(txtPrecoVenda.getText().replace(",", "."));
            
            if (precoProduto <= 0) {
                JOptionPane.showMessageDialog(this, "O preço deve ser maior que zero.");
                return;
            }

            carrinho.add(produtoSelecionado);
            valorTotal += precoProduto;

            listModelCarrinho.addElement(produtoSelecionado.getNome() + " - R$ " + String.format("%.2f", precoProduto));
            lblTotal.setText("Total: R$ " + String.format("%.2f", valorTotal) + "   ");
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite um preço válido (ex: 150.50).", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void finalizarVenda() {
        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O carrinho está vazio!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cliente = (Cliente) cbCliente.getSelectedItem();
        FormaPagamento formaPgto = (FormaPagamento) cbFormaPagamento.getSelectedItem();
        TipoConta tipoConta = (TipoConta) cbTipoConta.getSelectedItem();

        if (cliente == null || formaPgto == null || tipoConta == null) {
            JOptionPane.showMessageDialog(this, "Selecione Cliente, Forma de Pagamento e Tipo de Conta.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Venda novaVenda = new Venda();
        novaVenda.setCliente(cliente);
        novaVenda.setData_venda(new Date());
        novaVenda.setValor_total(valorTotal);
        novaVenda.setProdutos(new ArrayList<>(carrinho)); 

        boolean sucesso = vendaController.registrarVenda(novaVenda, formaPgto, tipoConta);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!\nEstoque baixado e parcelas geradas.");
            this.dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao registrar venda. Verifique o limite de vendas do cliente e o estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}