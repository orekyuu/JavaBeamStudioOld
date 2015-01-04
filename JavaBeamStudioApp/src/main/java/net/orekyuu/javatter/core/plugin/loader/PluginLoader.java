package net.orekyuu.javatter.core.plugin.loader;

import java.nio.file.Path;

/**
 * プラグインのローダー
 */
public interface PluginLoader {

    /**
     * ロードできるファイルかどうか検査します
     * @param path 検査するファイル
     * @return ロードできる拡張子であればtrue
     */
    boolean match(Path path);

    /**
     * プラグイン情報を返します
     * @param path ファイルのパス
     * @return プラグインの情報
     */
    PluginContainer load(Path path);

}
