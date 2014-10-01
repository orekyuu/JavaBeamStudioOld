package net.orekyuu.javatter.core.util.plugincenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * JavatterPluginCenterから情報を得るためのクラス
 */
public class JavatterPluginCenter {

    /**
     * JavatterPluginCenterで、プラグインの検索を行います。
     * このメソッドを使用した場合、全てのプラグインの情報を得ます。
     * @return 全てのプラグインのリスト
     */
    public List<PluginSearchResult> searchPlugins() throws IOException {
        return searchPlugins("");
    }

    /**
     * JavatterPluginCenterで、プラグインの検索を行います。
     * @param query 検索する文字列
     * @return プラグインのリスト
     */
    public List<PluginSearchResult> searchPlugins(String query) throws IOException {
        List<PluginSearchResult> result = new LinkedList<>();
        Document document = Jsoup.connect("http://javatter.plugin.orekyuu.net/plugins/?query=" + query).get();
        Elements elements = document.select("tr");
        for (Element element : elements) {
            Elements data = element.select("td");
            String title = data.get(0).text();
            String link = data.get(0).select("a").attr("href");
            String date = data.get(1).text();

            result.add(new PluginSearchResult(title, date, "http://javatter.plugin.orekyuu.net" + link));
        }

        return result;
    }

    public PluginInfo downloadPluginInfo(PluginSearchResult result) throws IOException {
        System.out.println(result);

        Document document = Jsoup.connect(result.getUrl()).ignoreContentType(true).get();
        Elements elements = document.select(".panel .panel-default");
        String name = elements.first().text();
        Elements panelBody = elements.get(1).select("h4");
        String description = panelBody.get(0).text();
        String version = panelBody.get(1).text().substring(9);
        String link = panelBody.get(2).select("a").attr("href");

        return new PluginInfo("", "", "", "", "", "");
    }
}
