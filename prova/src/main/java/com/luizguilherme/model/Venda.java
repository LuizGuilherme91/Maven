package com.luizguilherme.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_venda;
    
    private double valor_total;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToMany
    @JoinTable(
        name = "venda_produto",
        joinColumns = @JoinColumn(name = "venda_id"),
        inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<Produto> produtos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "financeiro_id")
    private Financeiro financeiro;

    public Venda() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getData_venda() { return data_venda; }
    public void setData_venda(Date data_venda) { this.data_venda = data_venda; }
    public double getValor_total() { return valor_total; }
    public void setValor_total(double valor_total) { this.valor_total = valor_total; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
    public Financeiro getFinanceiro() { return financeiro; }
    public void setFinanceiro(Financeiro financeiro) { this.financeiro = financeiro; }
}