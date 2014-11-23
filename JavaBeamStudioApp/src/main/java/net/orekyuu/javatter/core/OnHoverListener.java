package net.orekyuu.javatter.core;

import javafx.event.Event;

/**
 * ホバーイベントに関するインターフェースです
 * 
 * @author seroriKETC
 *
 */
public interface OnHoverListener {
    /**
     * ホバー時に呼ばれるメソッド
     * 
     * @param e
     *            イベント
     */
    void onHover(Event e);

    /**
     * ホバーが解除された時に呼ばれるメソッド
     * 
     * @param e
     *            イベント
     */
    void onHoverd(Event e);
}
