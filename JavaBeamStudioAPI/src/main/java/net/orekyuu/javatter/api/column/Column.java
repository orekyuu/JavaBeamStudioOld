package net.orekyuu.javatter.api.column;

import java.util.Objects;

/**
 * Columnの情報
 */
public class Column {

    private final String pluginId;
    private final String columnId;
    private final ColumnType type;
    private final String columnName;
    private final String columnPath;

    public Column(String pluginId, String columnId, ColumnType type, String columnName, String columnPath) {
        this.pluginId = pluginId;
        this.columnId = columnId;
        this.type = type;
        this.columnName = columnName;
        this.columnPath = columnPath;
    }

    /**
     * カラムのコピーを作成するコンストラクタです。
     * @param column コピー元
     * @throws NullPointerException 引数がNullの時
     */
    public Column(Column column) {
        Objects.requireNonNull(column, "column is null");
        this.pluginId = column.pluginId;
        this.columnId = column.columnId;
        this.type = column.type;
        this.columnName = column.columnName;
        this.columnPath = column.columnPath;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getColumnId() {
        return columnId;
    }

    public ColumnType getType() {
        return type;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnPath() {
        return columnPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        if (columnId != null ? !columnId.equals(column.columnId) : column.columnId != null) return false;
        if (pluginId != null ? !pluginId.equals(column.pluginId) : column.pluginId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pluginId != null ? pluginId.hashCode() : 0;
        result = 31 * result + (columnId != null ? columnId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Column{");
        sb.append("pluginId='").append(pluginId).append('\'');
        sb.append(", columnId='").append(columnId).append('\'');
        sb.append(", type=").append(type);
        sb.append(", columnName='").append(columnName).append('\'');
        sb.append(", columnPath='").append(columnPath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
