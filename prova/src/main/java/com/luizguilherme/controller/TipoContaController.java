package com.luizguilherme.controller;

import com.luizguilherme.dao.GenericDAO;
import com.luizguilherme.model.TipoConta;
import java.util.List;

public class TipoContaController {

    private GenericDAO<TipoConta> tipoContaDAO;

    public TipoContaController() {
        this.tipoContaDAO = new GenericDAO<>(TipoConta.class);
    }

    public boolean salvar(TipoConta tipoConta) {
        return tipoContaDAO.salvar(tipoConta);
    }

    public boolean alterar(TipoConta tipoConta) {
        return tipoContaDAO.alterar(tipoConta);
    }

    public boolean excluir(TipoConta tipoConta) {
        return tipoContaDAO.excluir(tipoConta);
    }

    public TipoConta pesquisarPorId(int id) {
        return tipoContaDAO.pesquisarPorId(id);
    }

    public List<TipoConta> pesquisarTodos() {
        return tipoContaDAO.pesquisarTodos();
    }
}