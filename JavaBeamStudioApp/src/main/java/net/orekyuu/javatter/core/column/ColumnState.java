package net.orekyuu.javatter.core.column;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import net.orekyuu.javatter.api.twitter.ClientUser;

@DatabaseTable(tableName = "ColumnState")
public class ColumnState {

    @DatabaseField(id = true)
    private long id;
    @DatabaseField(canBeNull = false)
    private String columnName;
    @DatabaseField(canBeNull = false)
    private String userName;

    public ColumnState() {

    }

    ColumnState(String columnName, ClientUser user) {
        this.columnName = columnName;
        userName = user.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnState that = (ColumnState) o;

        if (id != that.id) return false;
        if (columnName != null ? !columnName.equals(that.columnName) : that.columnName != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (columnName != null ? columnName.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ColumnState{");
        sb.append("id=").append(id);
        sb.append(", columnName='").append(columnName).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
