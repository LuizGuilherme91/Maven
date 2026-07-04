package com.luizguilherme.controller;

import com.luizguilherme.dao.GenericDAO;
import com.luizguilherme.model.Fornecedor;
import java.util.List;

public class FornecedorController {

    private GenericDAO<Fornecedor> fornecedorDAO;

    public FornecedorController() {
        this.fornecedorDAO = new GenericDAO<>(Fornecedor.class);
    }

    public boolean salvar(Fornecedor fornecedor) {
        if (fornecedor.getCnpj() == null || fornecedor.getCnpj().isEmpty()) {
            return false; // Validação básica: CNPJ é obrigatório
        }
        return fornecedorDAO.salvar(fornecedor);
    }

    public boolean alterar(Fornecedor fornecedor) {
        return fornecedorDAO.alterar(fornecedor);
    }

    public boolean excluir(Fornecedor fornecedor) {
        return fornecedorDAO.excluir(fornecedor);
    }

    public Fornecedor pesquisarPorId(int id) {
        return fornecedorDAO.pesquisarPorId(id);
    }

    public List<Fornecedor> pesquisarTodos() {
        return fornecedorDAO.pesquisarTodos();
    }
}