package com.luizguilherme.dao;

import com.luizguilherme.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class GenericDAO<T> {

    private Class<T> persistentClass;

    public GenericDAO(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    // Método para Salvar (Insert)
    public boolean salvar(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(entity); 
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); 
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Método para Alterar (Update)
    public boolean alterar(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(entity); 
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Método para Excluir (Delete)
    public boolean excluir(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            entity = em.merge(entity);
            em.remove(entity); 
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Método para Pesquisar por ID
    public T pesquisarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(persistentClass, id); 
        } finally {
            em.close();
        }
    }
    
    // Método para Pesquisar Todos
    public List<T> pesquisarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("FROM " + persistentClass.getName(), persistentClass).getResultList();
        } finally {
            em.close();
        }
    }
}