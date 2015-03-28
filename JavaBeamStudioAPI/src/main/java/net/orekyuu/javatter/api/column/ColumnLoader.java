package net.orekyuu.javatter.api.column;

import javafx.scene.Parent;
import net.orekyuu.javatter.api.loader.FxLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * カラムのロードを行うローダーです。
 */
public class ColumnLoader {

    private final Column column;
    private FxLoader columnContentLoader;
    private boolean isLoaded;

    /**
     * カラムをロードするローダーを作成します。
     * @param column ロードするカラム
     */
    public ColumnLoader(Column column) {
        this.column = column;
        columnContentLoader = new FxLoader();
    }

    /**
     * カラムをロードします。
     * @throws IOException FXMLの読み込みで例外発生時
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
     * カラムのコントローラ
     * @return カラムのコントローラ
     * @throws IllegalStateException ロードされていない場合
     */
    public ColumnController getController() {
        if (!isLoaded) {
            throw new IllegalStateException(column.getPluginId() + ":" + column.getColumnId() + " is not loaded.");
        }
        return columnContentLoader.getController();
    }

    /**
     * カラムのルートNodeを返します。
     * @param <T> ルートNodeの型
     * @return カラムのルートNode
     * @throws IllegalStateException ロードされていない場合
     */
    public <T extends Parent> T getRoot() {
        if (!isLoaded) {
            throw new IllegalStateException(column.getPluginId() + ":" + column.getColumnId() + " is not loaded.");
        }
        return columnContentLoader.getRoot();
    }

}
