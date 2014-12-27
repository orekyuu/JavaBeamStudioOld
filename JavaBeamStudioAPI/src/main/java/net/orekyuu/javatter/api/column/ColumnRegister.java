package net.orekyuu.javatter.api.column;

import java.net.URL;
import java.nio.file.Path;

/**
 * Columnの登録を行う
 *
 * @since 1.0.0
 */
public interface ColumnRegister {
    /**
     * 指定されたクラスからリソースを探します。
     *
     * @param name     カラムの名前
     * @param clazz    class
     * @param fxmlPath FXMLのパス
     * @since 1.0.0
     */
    void registerColumn(String name, Class<?> clazz, String fxmlPath);

    /**
     * PathからFXMLを探します。
     *
     * @param name     カラムの名前
     * @param fxmlPath FXMLのパス
     * @since 1.0.0
     */
    void registerColumn(String name, Path fxmlPath);

    /**
     * URLからFXMLを探します。
     *
     * @param name カラムの名前
     * @param url  FXMLのurl
     * @since 1.0.0
     */
    void registerColumn(String name, URL url);
}
