package net.orekyuu.javatter.api.service;

import net.orekyuu.javatter.api.column.Column;

import java.util.List;
import java.util.Optional;

/**
 * カラムの管理クラス
 */
public interface ColumnManager {

    /**
     * カラムを登録します
     * @param column 登録するカラム
     */
    void registerColumn(Column column);

    /**
     * IDからカラムを検索します。
     * @param columnId カラムID
     * @return 見つかったカラム
     */
    Optional<Column> findColumnById(String columnId);

    /**
     * プラグインIDからカラムを検索します。
     * @param pluginId プラグインID
     * @return 一致するカラムのリスト
     */
    List<Column> findColumnByPlugin(String pluginId);

    /**
     * 登録されているすべてのカラムを返します。
     * @return すべてのカラム
     */
    List<Column> findAll();
}
