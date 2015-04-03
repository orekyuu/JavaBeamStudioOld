package net.orekyuu.javatter.core.service;

import net.orekyuu.javatter.core.entity.StatusEntity;
import net.orekyuu.javatter.api.models.Status;
import net.orekyuu.javatter.api.service.StatusService;
import net.orekyuu.javatter.core.models.StatusModel;

import java.util.Optional;

public class StatusServiceImpl implements StatusService {
    @Override
    public Status create(Status status) {
        return new StatusModel(new StatusEntity());
    }

    @Override
    public Optional<Status> findByID(long statusId) {
        return null;
    }

    @Override
    public void persist(Status status) {

    }
}
