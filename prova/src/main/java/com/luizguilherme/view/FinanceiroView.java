package com.luizguilherme.view;

import com.luizguilherme.controller.FinanceiroController;
import com.luizguilherme.model.FinanceiroParcela;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FinanceiroView extends JFrame {

    private JTable tabelaContas;
    private DefaultTableModel modeloTabela;
    
    private JTextField txtDataPagamento;
    private JTextField txtDesconto;
    private JTextField txtAcrescimo;
    private JButton btnDarBaixa;
    
    private FinanceiroController financeiroController;
    private SimpleDateFormat sdf;

    public FinanceiroView() {
        financeiroController = new FinanceiroController();
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        setTitle("SisCom - Baixa de Contas a Pagar e Receber");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ----- PAINEL CENTRAL (Tabela de Contas Pendentes) -----
        // Colunas da nossa tabela
        String[] colunas = {"ID Parcela", "Tipo", "Vencimento", "Valor Original (R$)"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede a edição direta na tabela
            }
        };
        
        tabelaContas = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaContas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Contas Pendentes (Selecione uma para dar baixa)"));
        add(scrollPane, BorderLayout.CENTER);

        // ----- PAINEL INFERIOR (Formulário de Baixa) -----
        JPanel panelSul = new JPanel(new GridLayout(2, 4, 10, 5));
        panelSul.setBorder(BorderFactory.createTitledBorder("Dados do Pagamento / Recebimento"));

        panelSul.add(new JLabel(" Data (dd/mm/aaaa):"));
        txtDataPagamento = new JTextField(sdf.format(new Date())); // Preenche com a data de hoje
        panelSul.add(txtDataPagamento);

        panelSul.add(new JLabel(" Desconto (R$):"));
        txtDesconto = new JTextField("0.00");
        panelSul.add(txtDesconto);

        panelSul.add(new JLabel(" Acréscimo/Juros (R$):"));
        txtAcrescimo = new JTextField("0.00");
        panelSul.add(txtAcrescimo);

        btnDarBaixa = new JButton("Confirmar Baixa");
        btnDarBaixa.setBackground(new Color(255, 140, 0)); // Laranja escuro
        btnDarBaixa.setForeground(Color.WHITE);
        panelSul.add(new JLabel()); // Espaço
        panelSul.add(btnDarBaixa);

        add(panelSul, BorderLayout.SOUTH);

        // Carrega os dados na tabela ao abrir a tela
        carregarTabela();

        // Ação do botão
        btnDarBaixa.addActionListener(e -> realizarBaixa());
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0); // Limpa a tabela
        
        // RF011: Busca apenas as contas pendentes
        List<FinanceiroParcela> pendentes = financeiroController.listarParcelasPendentes();

        for (FinanceiroParcela p : pendentes) {
            // Verifica se é conta a Pagar (0) ou a Receber (1) através do Financeiro pai
            String tipo = p.getFinanceiro().getPagar_ou_receber() == 0 ? "A Pagar (Compra)" : "A Receber (Venda)";
            String dataVencimento = p.getData_vencimento() != null ? sdf.format(p.getData_vencimento()) : "N/D";
            String valor = String.format("%.2f", p.getValor_original());

            modeloTabela.addRow(new Object[]{
                p.getId(), 
                tipo, 
                dataVencimento, 
                valor
            });
        }
    }

    private void realizarBaixa() {
        int linhaSelecionada = tabelaContas.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma conta na tabela para dar baixa.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Pega o ID da parcela que está na coluna 0 da linha selecionada
            int idParcela = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            
            // Lê os dados digitados
            Date dataPgto = sdf.parse(txtDataPagamento.getText());
            double desconto = Double.parseDouble(txtDesconto.getText().replace(",", "."));
            double acrescimo = Double.parseDouble(txtAcrescimo.getText().replace(",", "."));

            // Chama o Controller
            boolean sucesso = financeiroController.darBaixaParcela(idParcela, dataPgto, desconto, acrescimo);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Baixa realizada com sucesso!\nA conta agora consta como paga.");
                carregarTabela(); // Recarrega a tabela (a conta paga vai sumir da lista)
                
                // Reseta os campos
                txtDesconto.setText("0.00");
                txtAcrescimo.setText("0.00");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao dar baixa na conta.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/mm/aaaa.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite valores numéricos válidos para desconto e acréscimo.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}