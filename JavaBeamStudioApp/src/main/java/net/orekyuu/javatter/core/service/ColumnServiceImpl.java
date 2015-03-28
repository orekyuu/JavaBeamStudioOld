package net.orekyuu.javatter.core.service;

import net.orekyuu.javatter.api.column.Column;
import net.orekyuu.javatter.api.entity.Account;
import net.orekyuu.javatter.api.entity.OpenColumnEntity;
import net.orekyuu.javatter.api.service.ColumnService;
import net.orekyuu.javatter.core.jpa.JavatterEntityManagerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ColumnServiceImpl implements ColumnService {

    private EntityManager em;

    public ColumnServiceImpl() {
        JavatterEntityManagerFactory factory = new JavatterEntityManagerFactory();
        em = factory.create();
    }

    @Override
    public OpenColumnEntity create(Column column, Account account) {
        EntityTransaction transaction = em.getTransaction();
        OpenColumnEntity entity = null;
        try {
            transaction.begin();
            entity = new OpenColumnEntity();
            entity.setAccount(account);
            entity.setColumnId(column.getPluginId() + ":" + column.getColumnId());
            entity.setColumnType(column.getType());
            entity.setPath(column.getColumnPath());
            entity.setColumnIndex((long) findAllColumn().size());
            em.persist(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
        }
        return entity;
    }

    @Override
    public OpenColumnEntity update(int index, Column column, Account account) {
        EntityTransaction transaction = em.getTransaction();
        OpenColumnEntity entity = null;
        try {
            transaction.begin();
            List<OpenColumnEntity> result = em.createNamedQuery(OpenColumnEntity.FIND_BY_INDEX, OpenColumnEntity.class).setParameter("columnIndex", index).getResultList();
            if (!result.isEmpty()) {
                entity = result.get(0);
                entity.setAccount(account);
                entity.setColumnId(column.getPluginId() + ":" + column.getColumnId());
                entity.setColumnType(column.getType());
                entity.setPath(column.getColumnPath());
                em.merge(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
        }
        return entity;
    }

    @Override
    public Optional<OpenColumnEntity> findColumnById(String pluginId, String columnId) {
        return Optional.empty();
    }

    @Override
    public List<OpenColumnEntity> findAllColumn() {
        return em.createNamedQuery(OpenColumnEntity.FIND_ALL, OpenColumnEntity.class).getResultList();
    }

    @Override
    public void remove(OpenColumnEntity entity) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(entity);
            em.flush();
            List<OpenColumnEntity> allColumn = findAllColumn();
            for (int i = 0; i < allColumn.size(); i++) {
                OpenColumnEntity e = allColumn.get(i);
                if (e.getColumnIndex() != i) {
                    e.setColumnIndex((long)i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
        }
    }

    @Override
    public void removeByIndex(int index) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.createNamedQuery(OpenColumnEntity.DELETE_BY_INDEX).setParameter("columnIndex", index).executeUpdate();
            em.flush();
            List<OpenColumnEntity> allColumn = findAllColumn();
            for (int i = 0; i < allColumn.size(); i++) {
                OpenColumnEntity e = allColumn.get(i);
                if (e.getColumnIndex() != i) {
                    e.setColumnIndex((long)i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transaction.commit();
        }
    }
}
