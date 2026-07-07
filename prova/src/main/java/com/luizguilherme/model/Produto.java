package com.luizguilherme.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private double preco_medio;
    private double qtde_estoque;
    private double valor_ultima_compra;
    private double valor_ultima_venda;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToMany
    @JoinTable(
        name = "produto_fornecedor",
        joinColumns = @JoinColumn(name = "produto_id"),
        inverseJoinColumns = @JoinColumn(name = "fornecedor_id")
    )
    private List<Fornecedor> fornecedores;

    @ManyToMany(mappedBy = "produtos")
    private List<Venda> vendas;

    @ManyToMany(mappedBy = "produtos")
    private List<Compra> compras;

    public Produto() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public double getPreco_medio() { return preco_medio; }
    public void setPreco_medio(double preco_medio) { this.preco_medio = preco_medio; }
    public double getQtde_estoque() { return qtde_estoque; }
    public void setQtde_estoque(double qtde_estoque) { this.qtde_estoque = qtde_estoque; }
    public double getValor_ultima_compra() { return valor_ultima_compra; }
    public void setValor_ultima_compra(double valor_ultima_compra) { this.valor_ultima_compra = valor_ultima_compra; }
    public double getValor_ultima_venda() { return valor_ultima_venda; }
    public void setValor_ultima_venda(double valor_ultima_venda) { this.valor_ultima_venda = valor_ultima_venda; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public List<Fornecedor> getFornecedores() { return fornecedores; }
    public void setFornecedores(List<Fornecedor> fornecedores) { this.fornecedores = fornecedores; }
    public List<Venda> getVendas() { return vendas; }
    public void setVendas(List<Venda> vendas) { this.vendas = vendas; }
    public List<Compra> getCompras() { return compras; }
    public void setCompras(List<Compra> compras) { this.compras = compras; }
}