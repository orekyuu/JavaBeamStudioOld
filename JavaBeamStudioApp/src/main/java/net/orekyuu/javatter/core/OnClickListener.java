package net.orekyuu.javatter.core;

import javafx.event.Event;
/**
 * クリックに関するインターフェース 
 *
 */
public interface OnClickListener {
    /**
     * クリック後に呼ばれるメソッド
     * @param e イベント
     */
    void onCLicked(Event e);
}
