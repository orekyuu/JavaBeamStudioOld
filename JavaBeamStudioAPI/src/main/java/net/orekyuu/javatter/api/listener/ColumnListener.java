package net.orekyuu.javatter.api.listener;

import net.orekyuu.javatter.api.entity.OpenColumnEntity;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ColumnListener {

    @PostPersist
    public void postPersist(OpenColumnEntity entity) {
        System.out.println("persist:" + entity);
    }

    @PostUpdate
    public void postUpdate(OpenColumnEntity entity) {
        System.out.println("update:" + entity);
    }

    @PostRemove
    public void postRemove(OpenColumnEntity entity) {
        System.out.println("remove: " + entity);
    }
}
