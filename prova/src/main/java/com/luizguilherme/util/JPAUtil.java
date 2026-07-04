package com.luizguilherme.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JPAUtil {
    private static final Logger logger = LogManager.getLogger(JPAUtil.class);
    private static EntityManagerFactory factory;

    static {
        try {
            logger.info("Iniciando Hibernate (SQLite)...");
            factory = Persistence.createEntityManagerFactory("SisComPU");
        } catch (Throwable ex) {
            logger.error("Falha ao inicializar o Hibernate!", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}