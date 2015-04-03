package net.orekyuu.javatter.api.column;

import javafx.scene.Parent;
import net.orekyuu.javatter.api.loader.FxLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Columnのローダーです。
 */
public class ColumnLoader {

    private final Column column;
    private FxLoader columnContentLoader;
    private boolean isLoaded;

    /**
     * ColumnをロードするColumnLoaderを作成します。
     * @param column ロードするColumn
     */
    public ColumnLoader(Column column) {
        this.column = column;
        columnContentLoader = new FxLoader();
    }

    /**
     * ロードします。
     * @throws IOException FXMLのロードに失敗した時
     * @throws IllegalStateException すでにロードされていた時
     */
    public void load() throws IOException {
        if (isLoaded) {
            throw new IllegalStateException(column.getPluginId() + ":" + column.getColumnId() + " is loaded.");
        }
        InputStream inputStream = column.getType().createInputStream(column.getColumnPath());
        columnContentLoader.load(inputStream);
        isLoaded = true;
    }

    /**
     * Columnのコントローラを返します。
     * @return ロードしたColumnのコントローラ
     * @throws IllegalStateException ロードされていない状態で呼び出された時
     */
    public ColumnController getController() {
        if (!isLoaded) {
            throw new IllegalStateException(column.getPluginId() + ":" + column.getColumnId() + " is not loaded.");
        }
        return columnContentLoader.getController();
    }

    /**
     * Columnのルート要素を返します。
     * @param <T> ルートノードの型
     * @return ルートノード
     * @throws IllegalStateException ロードされていない状態で呼び出された時
     */
    public <T extends Parent> T getRoot() {
        if (!isLoaded) {
            throw new IllegalStateException(column.getPluginId() + ":" + column.getColumnId() + " is not loaded.");
        }
        return columnContentLoader.getRoot();
    }

}
