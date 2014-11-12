package net.orekyuu.javatter.core.column;

import net.orekyuu.javatter.api.ColumnRegister;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Columnの管理クラス
 */
public class ColumnManager implements ColumnRegister {

    private List<Column> columns = new LinkedList<>();

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
     * @return 登録されたカラム
     */
    public List<Column> getRegisteredColumns() {
        return columns;
    }

    /**
     * 指定された名前のカラムを探します。
     * @param columnName カラム名
     * @return columnNameに一致したカラム
     */
    public Column findColumn(String columnName) {
        return columns.parallelStream().filter(c -> c.getName().equals(columnName)).findFirst().get();
    }
}
