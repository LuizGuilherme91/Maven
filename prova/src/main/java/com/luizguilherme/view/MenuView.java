package com.luizguilherme.view;

import javax.swing.*;

public class MenuView extends JFrame {

    public MenuView() {
        setTitle("SisCom - Sistema Comercial");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        // ---------------- MENU DE CADASTROS ----------------
        JMenu menuCadastros = new JMenu("Cadastros");
        
        JMenuItem itemCategoria = new JMenuItem("Categorias");
        JMenuItem itemProduto = new JMenuItem("Produtos");
        JMenuItem itemCliente = new JMenuItem("Clientes");
        JMenuItem itemFornecedor = new JMenuItem("Fornecedores");
        JMenuItem itemFormaPgto = new JMenuItem("Formas de Pagamento");
        JMenuItem itemTipoConta = new JMenuItem("Tipos de Conta");
        
        itemCategoria.addActionListener(e -> new CategoriaView().setVisible(true));
        itemProduto.addActionListener(e -> new ProdutoView().setVisible(true));
        itemCliente.addActionListener(e -> new ClienteView().setVisible(true));
        itemFornecedor.addActionListener(e -> new FornecedorView().setVisible(true));
        itemFormaPgto.addActionListener(e -> new FormaPagamentoView().setVisible(true));
        itemTipoConta.addActionListener(e -> new TipoContaView().setVisible(true));

        menuCadastros.add(itemCategoria);
        menuCadastros.add(itemProduto);
        menuCadastros.addSeparator();
        menuCadastros.add(itemCliente);
        menuCadastros.add(itemFornecedor);
        menuCadastros.addSeparator();
        menuCadastros.add(itemFormaPgto);
        menuCadastros.add(itemTipoConta);

        // --------------- MENU DE MOVIMENTAÇÕES ---------------
        JMenu menuMovimentacoes = new JMenu("Movimentações");
        JMenuItem itemVenda = new JMenuItem("Registrar Venda");
        JMenuItem itemCompra = new JMenuItem("Registrar Compra");
        
        itemVenda.addActionListener(e -> new VendaView().setVisible(true));
        itemCompra.addActionListener(e -> new CompraView().setVisible(true));

        menuMovimentacoes.add(itemVenda);
        menuMovimentacoes.add(itemCompra);

        // ----------------- MENU FINANCEIRO ------------------
        JMenu menuFinanceiro = new JMenu("Financeiro");
        JMenuItem itemContas = new JMenuItem("Baixa de Contas a Pagar/Receber");
        
        // AÇÃO DO FINANCEIRO LIGADA
        itemContas.addActionListener(e -> new FinanceiroView().setVisible(true));
        
        menuFinanceiro.add(itemContas);

        // ----------------- MENU RELATÓRIOS ------------------
        JMenu menuRelatorios = new JMenu("Relatórios");
        JMenuItem relVendas = new JMenuItem("Vendas por Período/Cliente");
        JMenuItem relCompras = new JMenuItem("Compras por Período/Fornecedor");
        JMenuItem relFinanceiro = new JMenuItem("Contas (Pagar/Receber)");
        
        menuRelatorios.add(relVendas);
        menuRelatorios.add(relCompras);
        menuRelatorios.add(relFinanceiro);

        menuBar.add(menuCadastros);
        menuBar.add(menuMovimentacoes);
        menuBar.add(menuFinanceiro);
        menuBar.add(menuRelatorios);

        setJMenuBar(menuBar);
    }
}