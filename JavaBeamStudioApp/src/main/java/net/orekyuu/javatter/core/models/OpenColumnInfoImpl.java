package net.orekyuu.javatter.core.models;

import net.orekyuu.javatter.api.column.ColumnType;
import net.orekyuu.javatter.api.models.OpenColumnInfo;
import net.orekyuu.javatter.api.service.AccountService;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.Main;
import net.orekyuu.javatter.core.entity.OpenColumnEntity;
import net.orekyuu.javatter.core.service.AccountServiceImpl;

import java.util.Optional;

public class OpenColumnInfoImpl implements OpenColumnInfo {

    public final String columnId;
    public final Long columnIndex;
    public final ClientUser user;
    public String path;
    public ColumnType type;

    public OpenColumnInfoImpl(String columnId, Long columnIndex, ClientUser user, String path, ColumnType type) {
        this.columnId = columnId;
        this.columnIndex = columnIndex;
        this.user = user;
        this.path = path;
        this.type = type;
    }

    public OpenColumnInfoImpl(OpenColumnEntity entity) {
        this(entity.getColumnId(), entity.getColumnIndex(),
                Main.getInjector().getDependency(AccountService.class).findByScreenName(entity.getAccount().getScreenName()).get(),
                entity.getPath(), entity.getColumnType());
    }

    @Override
    public String getColumnId() {
        return columnId;
    }

    @Override
    public Long getColumnIndex() {
        return columnIndex;
    }

    @Override
    public Optional<ClientUser> getClientUser() {
        return Optional.ofNullable(user);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public ColumnType getColumnType() {
        return type;
    }
}
