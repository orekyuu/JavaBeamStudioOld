package net.orekyuu.javatter.api.models;

import net.orekyuu.javatter.api.column.ColumnType;
import net.orekyuu.javatter.api.twitter.ClientUser;

import java.util.Optional;

/**
 * 開いているカラムの情報
 */
public interface OpenColumnInfo {
    /**
     * カラムのID</br>
     * PluginID:ColumnIDのフォーマットで返されます。
     * @return ColumnID
     */
    String getColumnId();

    /**
     * Columnのインデックスです。
     * @return Columnのインデックス
     */
    Long getColumnIndex();

    /**
     * Columnの所有者
     * @return Columnの所有者
     */
    Optional<ClientUser> getClientUser();

    /**
     * ColumnのFXMLのパス
     * @return FXMLのファイルパス
     */
    String getPath();

    /**
     * Columnのタイプ
     * @return Columnのタイプ
     */
    ColumnType getColumnType();
}
