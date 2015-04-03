package net.orekyuu.javatter.api.service;

import net.orekyuu.javatter.api.column.Column;
import net.orekyuu.javatter.api.models.OpenColumnInfo;
import net.orekyuu.javatter.api.twitter.ClientUser;

import java.util.List;
import java.util.Optional;


/**
 * Javaビーム工房のカラムの機能を提供するサービスです
 */
public interface ColumnService {

    /**
     * 新しいカラムを作成します。
     * @param column 作成するColumn
     * @param account 使用するアカウント
     * @return 作成されたColumn
     */
    OpenColumnInfo create(Column column, ClientUser account);

    /**
     * カラムの状態を更新します。
     * @param index カラムのインデックス
     * @param column カラムのタイプ
     * @param account アカウント
     * @return 更新されたColumn
     */
    OpenColumnInfo update(int index, Column column, ClientUser account);

    /**
     * IDから現在開かれているColumnを取得します
     * @param pluginId プラグインID
     * @param columnId ColumnID
     * @return 一致するColumn
     */
    Optional<OpenColumnInfo> findColumnById(String pluginId, String columnId);

    /**
     * 現在開かれているすべてのColumnを返します
     * @return 開かれているすべてのカラム
     */
    List<OpenColumnInfo> findAllColumn();

    /**
     * 指定されたカラムを削除します。
     * @param entity 削除するカラム
     */
    void remove(OpenColumnInfo entity);

    /**
     * インデックスからカラムを削除します。
     * @param index 削除するインデックス
     */
    void removeByIndex(int index);
}
