package com.luizguilherme.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "fornecedor")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome_fantasia;
    private String razao_social;
    private String cnpj;

    // Relação M:N mapeada no Produto
    @ManyToMany(mappedBy = "fornecedores")
    private List<Produto> produtos;

    @OneToMany(mappedBy = "fornecedor")
    private List<Compra> compras;

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome_fantasia() { return nome_fantasia; }
    public void setNome_fantasia(String nome_fantasia) { this.nome_fantasia = nome_fantasia; }
    public String getRazao_social() { return razao_social; }
    public void setRazao_social(String razao_social) { this.razao_social = razao_social; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
    public List<Compra> getCompras() { return compras; }
    public void setCompras(List<Compra> compras) { this.compras = compras; }
}