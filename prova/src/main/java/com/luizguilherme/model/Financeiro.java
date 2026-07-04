package com.luizguilherme.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "financeiro")
public class Financeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date data_conta;
    
    private int pagar_ou_receber;

    @ManyToOne
    @JoinColumn(name = "forma_pagamento_id")
    private FormaPagamento formaPagamento;

    @ManyToOne
    @JoinColumn(name = "tipo_conta_id")
    private TipoConta tipoConta;

    // 1:N com Parcelas - cascade = ALL para salvar as parcelas automaticamente
    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinanceiroParcela> parcelas;

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getData_conta() { return data_conta; }
    public void setData_conta(Date data_conta) { this.data_conta = data_conta; }
    public int getPagar_ou_receber() { return pagar_ou_receber; }
    public void setPagar_ou_receber(int pagar_ou_receber) { this.pagar_ou_receber = pagar_ou_receber; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }
    public TipoConta getTipoConta() { return tipoConta; }
    public void setTipoConta(TipoConta tipoConta) { this.tipoConta = tipoConta; }
    public List<FinanceiroParcela> getParcelas() { return parcelas; }
    public void setParcelas(List<FinanceiroParcela> parcelas) { this.parcelas = parcelas; }
}