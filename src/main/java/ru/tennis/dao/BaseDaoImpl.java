package ru.tennis.dao;

import org.hibernate.Session;

public abstract class BaseDaoImpl<T>{

    protected T save(Session session, T entity) {
        session.persist(entity);
        return entity;
    }
}
