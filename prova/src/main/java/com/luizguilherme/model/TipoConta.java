package com.luizguilherme.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_conta")
public class TipoConta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String descricao; 

    public TipoConta() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}