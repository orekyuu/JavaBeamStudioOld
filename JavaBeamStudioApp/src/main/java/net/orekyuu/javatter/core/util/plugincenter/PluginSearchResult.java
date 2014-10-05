package net.orekyuu.javatter.core.util.plugincenter;

/**
 * プラグインセンターの検索結果。
 */
public class PluginSearchResult {
    private final String title;
    private final String date;
    private final String url;

    PluginSearchResult(String title, String date, String url) {
        this.title = title;
        this.date = date;
        this.url = url;
    }

    /**
     * プラグインの名前を返します。
     *
     * @return プラグイン名
     */
    public String getTitle() {
        return title;
    }

    /**
     * プラグインの更新日時。<br>
     * フォーマットは以下のようになります。<br>
     * yyy年mm月dd日HH:MM:SS
     *
     * @return プラグインの更新日時
     */
    public String getDate() {
        return date;
    }

    /**
     * プラグインのURLを返します。
     *
     * @return プラグインのURL
     */
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PluginSearchResult{");
        sb.append("title='").append(title).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginSearchResult that = (PluginSearchResult) o;

        return date.equals(that.date) && title.equals(that.title) && url.equals(that.url);

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }
}
