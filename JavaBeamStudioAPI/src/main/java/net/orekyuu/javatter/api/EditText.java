package net.orekyuu.javatter.api;
/**
 * EditTextについてのインターフェース
 *
 */
public interface EditText {
	/**
	 * テキストをセットする
	 * @param s
	 */
    void setText(String s);
    /**
     * テキストを取得する
     */
    String getText();
    
	/**
	 * テキストが変更された時に呼ばれるメソッド。
	 * @param cs 新規に入力された文字を含むテキスト
	 * @param count ｃｓに格納された文字の数
	 */
    void onChanged(CharSequence cs, int count);
   
    
}
