package net.orekyuu.javatter.api;

/**
 * ツイートの成功の可否についてのインターフェース
 *
 */
public interface TweetCallBack {
    /**
     * 
     * @param success
     *            成功したか否かが格納
     * 
     */
    void SuccessCallBack(Boolean success);
}
