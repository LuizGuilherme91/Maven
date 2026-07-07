package com.luizguilherme.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_compra;
    
    private double valor_total;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @ManyToMany
    @JoinTable(
        name = "compra_produto",
        joinColumns = @JoinColumn(name = "compra_id"),
        inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<Produto> produtos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "financeiro_id")
    private Financeiro financeiro;

    public Compra() {
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getData_compra() { return data_compra; }
    public void setData_compra(Date data_compra) { this.data_compra = data_compra; }
    public double getValor_total() { return valor_total; }
    public void setValor_total(double valor_total) { this.valor_total = valor_total; }
    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
    public Financeiro getFinanceiro() { return financeiro; }
    public void setFinanceiro(Financeiro financeiro) { this.financeiro = financeiro; }
}