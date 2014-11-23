package net.orekyuu.javatter.core;

import javafx.scene.input.DragEvent;

/**
 * 画像のD&Dに関するインターフェース
 *
 */
public interface OnDropListener {
    /**
     * ドロップ前のイベント
     * 
     * @param e
     *            イベント
     */
    void onDrop(DragEvent e);

    /**
     * ドロップ後のイベント
     * 
     * @param e
     *            イベント
     */
    void onDroped(DragEvent e);
}
