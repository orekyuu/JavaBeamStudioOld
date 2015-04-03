package net.orekyuu.javatter.core.entity;

import net.orekyuu.javatter.api.column.ColumnType;

import javax.persistence.*;

@Entity
@Table(name = "OPEN_COLUMN")
@NamedQueries({
    @NamedQuery(name = OpenColumnEntity.FIND_ALL, query = "SELECT e FROM OpenColumnEntity e"),
    @NamedQuery(name = net.orekyuu.javatter.core.entity.OpenColumnEntity.FIND_BY_INDEX, query = "SELECT e FROM OpenColumnEntity e WHERE e.columnIndex = :columnIndex"),
    @NamedQuery(name = net.orekyuu.javatter.core.entity.OpenColumnEntity.DELETE_ALL, query = "DELETE FROM OpenColumnEntity e"),
    @NamedQuery(name = net.orekyuu.javatter.core.entity.OpenColumnEntity.DELETE_BY_INDEX, query = "DELETE FROM OpenColumnEntity e WHERE e.columnIndex = :columnIndex"),
})
public class OpenColumnEntity implements Comparable<net.orekyuu.javatter.core.entity.OpenColumnEntity> {

    public static final String FIND_ALL = "OpenColumnEntity.findAll";
    public static final String FIND_BY_INDEX = "OpenColumnEntity.findByIndex";
    public static final String DELETE_ALL = "OpenColumnEntity.deleteAll";
    public static final String DELETE_BY_INDEX = "OpenColumnEntity.deleteByIndex";

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long columnIndex;

    @OneToOne
    @JoinColumn(nullable = false)
    private Account account;

    @Column(nullable = false)
    private String columnId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ColumnType columnType;

    @Column(nullable = false)
    private String path;

    public OpenColumnEntity(Long columnIndex, Account account, String columnId, ColumnType columnType, String path) {
        this.columnIndex = columnIndex;
        this.account = account;
        this.columnId = columnId;
        this.columnType = columnType;
        this.path = path;
    }

    public OpenColumnEntity() {
    }

    public Long getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Long columnIndex) {
        this.columnIndex = columnIndex;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        net.orekyuu.javatter.core.entity.OpenColumnEntity entity = (net.orekyuu.javatter.core.entity.OpenColumnEntity) o;

        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;
        if (columnIndex != null ? !columnIndex.equals(entity.columnIndex) : entity.columnIndex != null) return false;
        if (account != null ? !account.equals(entity.account) : entity.account != null) return false;
        if (columnId != null ? !columnId.equals(entity.columnId) : entity.columnId != null) return false;
        if (columnType != entity.columnType) return false;
        return !(path != null ? !path.equals(entity.path) : entity.path != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (columnIndex != null ? columnIndex.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (columnId != null ? columnId.hashCode() : 0);
        result = 31 * result + (columnType != null ? columnType.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OpenColumnEntity{");
        sb.append("id=").append(id);
        sb.append(", columnIndex=").append(columnIndex);
        sb.append(", account=").append(account);
        sb.append(", columnId='").append(columnId).append('\'');
        sb.append(", columnType=").append(columnType);
        sb.append(", path='").append(path).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(net.orekyuu.javatter.core.entity.OpenColumnEntity o) {
        return (int) (this.columnIndex - o.columnIndex);
    }
}
