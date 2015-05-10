package net.orekyuu.javatter.core.column;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import net.orekyuu.javatter.api.column.ColumnRegister;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Columnの管理クラス
 */
@Singleton
public class ColumnManager implements ColumnRegister {

    private List<Column> columns = new LinkedList<>();
    private static final Path path = Paths.get("columns.db");

    @Override
    public void registerColumn(String name, Class<?> clazz, String fxmlPath) {
        columns.add(new Column() {
            @Override
            public int compareTo(Column o) {
                return getName().compareTo(o.getName());
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public InputStream createInputStream() {
                return clazz.getResourceAsStream(fxmlPath);
            }
        });
    }

    @Override
    public void registerColumn(String name, Path fxmlPath) {
        columns.add(new Column() {
            @Override
            public int compareTo(Column o) {
                return getName().compareTo(o.getName());
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public InputStream createInputStream() {
                try {
                    return Files.newInputStream(fxmlPath);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        });
    }

    @Override
    public void registerColumn(String name, URL url) {
        columns.add(new Column() {
            @Override
            public int compareTo(Column o) {
                return getName().compareTo(o.getName());
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public InputStream createInputStream() {
                try {
                    return url.openStream();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        });
    }

    /**
     * 登録されているカラムのリストを返します。
     *
     * @return 登録されたカラム
     */
    public List<Column> getRegisteredColumns() {
        return columns;
    }

    /**
     * 指定された名前のカラムを探します。
     *
     * @param columnName カラム名
     * @return columnNameに一致したカラム
     */
    public Optional<Column> findColumn(String columnName) {
        if (columnName == null) {
            return Optional.empty();
        }

        return columns.parallelStream().filter(c -> c.getName().equals(columnName)).findFirst();
    }

    public void saveColumnState(List<ColumnState> columnStates) {
        createFile();
        JdbcPooledConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:" + path);
            Dao<ColumnState, Long> dao = DaoManager.createDao(connectionSource, ColumnState.class);
            TableUtils.createTableIfNotExists(connectionSource, ColumnState.class);
            for (int i = columnStates.size(); i < dao.countOf(); i++) {
                dao.deleteById((long) i);
            }

            for (int i = 0; i < columnStates.size(); i++) {
                ColumnState state = columnStates.get(i);
                state.setId(i);
                dao.createOrUpdate(state);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<ColumnState> loadColumnState() {
        JdbcPooledConnectionSource connectionSource = null;
        createFile();
        try {
            connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:" + path);
            TableUtils.createTableIfNotExists(connectionSource, ColumnState.class);
            Dao<ColumnState, Long> dao = DaoManager.createDao(connectionSource, ColumnState.class);
            QueryBuilder<ColumnState, Long> queryBuilder = dao.queryBuilder();
            if (dao.countOf() == 0) {
                return new LinkedList<>();
            }
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return new LinkedList<>();
    }

    private void createFile() {
        try {
            if (Files.notExists(path))
                Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
