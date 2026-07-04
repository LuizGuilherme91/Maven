package com.luizguilherme.controller;

import com.luizguilherme.dao.GenericDAO;
import com.luizguilherme.model.Categoria;
import java.util.List;

/**
 * Controller responsável pelo CRUD de Categorias (RF004).
 */
public class CategoriaController {

    private GenericDAO<Categoria> categoriaDAO;

    public CategoriaController() {
        this.categoriaDAO = new GenericDAO<>(Categoria.class);
    }

    public boolean salvar(Categoria categoria) {
        // Aqui poderíamos adicionar validações, como não deixar salvar categoria sem nome
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            return false;
        }
        return categoriaDAO.salvar(categoria);
    }

    public boolean alterar(Categoria categoria) {
        return categoriaDAO.alterar(categoria);
    }

    public boolean excluir(Categoria categoria) {
        return categoriaDAO.excluir(categoria);
    }

    public Categoria pesquisarPorId(int id) {
        return categoriaDAO.pesquisarPorId(id);
    }

    public List<Categoria> pesquisarTodos() {
        return categoriaDAO.pesquisarTodos();
    }
}