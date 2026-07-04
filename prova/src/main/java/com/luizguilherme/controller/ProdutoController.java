package com.luizguilherme.controller;

import com.luizguilherme.dao.GenericDAO;
import com.luizguilherme.model.Produto;
import java.util.List;

/**
 * Controller responsável por intermediar as ações da tela de Produto 
 * e as operações de banco de dados (RF001).
 */
public class ProdutoController {

    // Instanciamos o DAO Genérico passando a classe Produto como tipo
    private GenericDAO<Produto> produtoDAO;

    public ProdutoController() {
        this.produtoDAO = new GenericDAO<>(Produto.class);
    }

    public boolean salvar(Produto produto) {
        // Regra de negócio básica: garantir que a quantidade de estoque não seja iniciada negativa
        if (produto.getQtde_estoque() < 0) {
            produto.setQtde_estoque(0);
        }
        return produtoDAO.salvar(produto);
    }

    public boolean alterar(Produto produto) {
        return produtoDAO.alterar(produto);
    }

    public boolean excluir(Produto produto) {
        return produtoDAO.excluir(produto);
    }

    public Produto pesquisarPorId(int id) {
        return produtoDAO.pesquisarPorId(id);
    }

    public List<Produto> pesquisarTodos() {
        return produtoDAO.pesquisarTodos();
    }
}