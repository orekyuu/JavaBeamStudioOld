package net.orekyuu.javatter.api;

import twitter4j.Status;

/**
 * ツイート成功時のイベント
 */
@FunctionalInterface
public interface TweetSuccess {
    /**
     * 正常にツイートされた時に呼び出されます。
     * @param model 正常にツイートされたツイートの情報
     */
    void success(Status model);
}
