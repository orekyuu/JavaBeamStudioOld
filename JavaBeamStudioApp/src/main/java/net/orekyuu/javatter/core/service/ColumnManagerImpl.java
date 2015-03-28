package net.orekyuu.javatter.core.service;

import net.orekyuu.javatter.api.column.Column;
import net.orekyuu.javatter.api.service.ColumnManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ColumnManagerImpl implements ColumnManager {
    private static Map<String, Column> registeredColumn = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(ColumnManagerImpl.class.getName());

    @Override
    public void registerColumn(Column column) {
        registeredColumn.put(column.getPluginId() + ":" + column.getColumnId(), column);
        logger.info("Registered column: " + column);
    }

    @Override
    public Optional<Column> findColumnById(String columnId) {
        return Optional.ofNullable(new Column(registeredColumn.get(columnId)));
    }

    @Override
    public List<Column> findColumnByPlugin(String pluginId) {
        return registeredColumn.values().stream()
                .filter(column -> column.getPluginId().equals(pluginId))
                .map(Column::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Column> findAll() {
        return registeredColumn.values().stream().map(Column::new).collect(Collectors.toList());
    }
}
