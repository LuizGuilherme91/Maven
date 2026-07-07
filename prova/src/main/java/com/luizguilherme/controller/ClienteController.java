package com.luizguilherme.controller;

import com.luizguilherme.dao.GenericDAO;
import com.luizguilherme.model.Cliente;
import java.util.List;

public class ClienteController {

    private GenericDAO<Cliente> clienteDAO;

    public ClienteController() {
        this.clienteDAO = new GenericDAO<>(Cliente.class);
    }

    public boolean salvar(Cliente cliente) {
        if (cliente.getCpf() == null || cliente.getCpf().isEmpty()) {
            return false; 
        }
        return clienteDAO.salvar(cliente);
    }

    public boolean alterar(Cliente cliente) {
        return clienteDAO.alterar(cliente);
    }

    public boolean excluir(Cliente cliente) {
        return clienteDAO.excluir(cliente);
    }

    public Cliente pesquisarPorId(int id) {
        return clienteDAO.pesquisarPorId(id);
    }

    public List<Cliente> pesquisarTodos() {
        return clienteDAO.pesquisarTodos();
    }
}