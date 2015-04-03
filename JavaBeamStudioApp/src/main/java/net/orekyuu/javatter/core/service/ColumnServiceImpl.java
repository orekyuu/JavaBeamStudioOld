package net.orekyuu.javatter.core.service;

import net.orekyuu.javatter.api.column.Column;
import net.orekyuu.javatter.api.models.OpenColumnInfo;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.entity.Account;
import net.orekyuu.javatter.core.entity.OpenColumnEntity;
import net.orekyuu.javatter.api.service.ColumnService;
import net.orekyuu.javatter.core.jpa.JavatterEntityManagerFactory;
import net.orekyuu.javatter.core.models.OpenColumnInfoImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ColumnServiceImpl implements ColumnService {

    private EntityManager em;

    public ColumnServiceImpl() {
        JavatterEntityManagerFactory factory = new JavatterEntityManagerFactory();
        em = factory.create();
    }

    @Override
    public OpenColumnInfo create(Column column, ClientUser account) {
        EntityTransaction transaction = em.getTransaction();
        OpenColumnEntity entity = null;
        try {
            transaction.begin();
            entity = new OpenColumnEntity();
            entity.setAccount(new Account(account.getName(), account.getAccessToken().getToken(), account.getAccessToken().getTokenSecret()));
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
        return new OpenColumnInfoImpl(entity);
    }

    @Override
    public OpenColumnInfo update(int index, Column column, ClientUser account) {
        EntityTransaction transaction = em.getTransaction();
        OpenColumnEntity entity = null;
        try {
            transaction.begin();
            List<OpenColumnEntity> result = em.createNamedQuery(OpenColumnEntity.FIND_BY_INDEX, OpenColumnEntity.class).setParameter("columnIndex", index).getResultList();
            if (!result.isEmpty()) {
                entity = result.get(0);
                entity.setAccount(new Account(account.getName(), account.getAccessToken().getToken(), account.getAccessToken().getTokenSecret()));
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
        return new OpenColumnInfoImpl(entity);
    }

    @Override
    public Optional<OpenColumnInfo> findColumnById(String pluginId, String columnId) {
        return Optional.empty();
    }

    @Override
    public List<OpenColumnInfo> findAllColumn() {
        List<OpenColumnEntity> resultList = em.createNamedQuery(OpenColumnEntity.FIND_ALL, OpenColumnEntity.class).getResultList();
        return resultList.stream().map(OpenColumnInfoImpl::new).collect(Collectors.toList());
    }

    @Override
    public void remove(OpenColumnInfo entity) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(new OpenColumnEntity(entity));
            em.flush();
            List<OpenColumnInfo> allColumn = findAllColumn();
            for (int i = 0; i < allColumn.size(); i++) {
                OpenColumnEntity e = new OpenColumnEntity(allColumn.get(i));
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
            List<OpenColumnEntity> allColumn = findAllColumn().stream().map(OpenColumnEntity::new).collect(Collectors.toList());
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
