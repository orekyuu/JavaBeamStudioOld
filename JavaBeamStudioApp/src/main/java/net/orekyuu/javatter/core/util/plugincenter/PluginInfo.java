package net.orekyuu.javatter.core.util.plugincenter;

/**
 * プラグインに関する情報
 */
public class PluginInfo {
    private final String name;
    private final String description;
    private final String javatterVersion;
    private final String dlLink;
    private final String updateDate;
    private final String author;

    PluginInfo(String name, String description, String javatterVersion, String dlLink, String updateDate, String author) {
        this.name = name;
        this.description = description;
        this.javatterVersion = javatterVersion;
        this.dlLink = dlLink;
        this.updateDate = updateDate;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getJavatterVersion() {
        return javatterVersion;
    }

    public String getDlLink() {
        return dlLink;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PluginInfo{");
        sb.append("name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", javatterVersion='").append(javatterVersion).append('\'');
        sb.append(", dlLink='").append(dlLink).append('\'');
        sb.append(", updateDate='").append(updateDate).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginInfo that = (PluginInfo) o;

        if (!author.equals(that.author)) return false;
        if (!description.equals(that.description)) return false;
        if (!dlLink.equals(that.dlLink)) return false;
        if (!javatterVersion.equals(that.javatterVersion)) return false;
        if (!name.equals(that.name)) return false;
        if (!updateDate.equals(that.updateDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + javatterVersion.hashCode();
        result = 31 * result + dlLink.hashCode();
        result = 31 * result + updateDate.hashCode();
        result = 31 * result + author.hashCode();
        return result;
    }
}
