package net.orekyuu.javatter.api.service;

import net.orekyuu.javatter.api.column.Column;
import net.orekyuu.javatter.api.entity.Account;
import net.orekyuu.javatter.api.entity.OpenColumnEntity;

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
    OpenColumnEntity create(Column column, Account account);

    /**
     * カラムの状態を更新します。
     * @param index カラムのインデックス
     * @param column カラムのタイプ
     * @param account アカウント
     * @return 更新されたColumn
     */
    OpenColumnEntity update(int index, Column column, Account account);

    /**
     * IDから現在開かれているColumnを取得します
     * @param pluginId プラグインID
     * @param columnId ColumnID
     * @return 一致するColumn
     */
    Optional<OpenColumnEntity> findColumnById(String pluginId, String columnId);

    /**
     * 現在開かれているすべてのColumnを返します
     * @return 開かれているすべてのカラム
     */
    List<OpenColumnEntity> findAllColumn();

    /**
     * 指定されたカラムを削除します。
     * @param entity 削除するカラム
     */
    void remove(OpenColumnEntity entity);

    /**
     * インデックスからカラムを削除します。
     * @param index 削除するインデックス
     */
    void removeByIndex(int index);
}
