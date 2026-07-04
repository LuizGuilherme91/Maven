package com.luizguilherme.controller;

import com.luizguilherme.dao.GenericDAO;
import com.luizguilherme.model.FormaPagamento;
import java.util.List;

public class FormaPagamentoController {

    private GenericDAO<FormaPagamento> formaPagamentoDAO;

    public FormaPagamentoController() {
        this.formaPagamentoDAO = new GenericDAO<>(FormaPagamento.class);
    }

    public boolean salvar(FormaPagamento formaPagamento) {
        return formaPagamentoDAO.salvar(formaPagamento);
    }

    public boolean alterar(FormaPagamento formaPagamento) {
        return formaPagamentoDAO.alterar(formaPagamento);
    }

    public boolean excluir(FormaPagamento formaPagamento) {
        return formaPagamentoDAO.excluir(formaPagamento);
    }

    public FormaPagamento pesquisarPorId(int id) {
        return formaPagamentoDAO.pesquisarPorId(id);
    }

    public List<FormaPagamento> pesquisarTodos() {
        return formaPagamentoDAO.pesquisarTodos();
    }
}