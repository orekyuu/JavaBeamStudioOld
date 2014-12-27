package net.orekyuu.javatter.api.twitter;

import twitter4j.Status;

/**
 * ツイート成功時のイベント
 * @since 1.0.0
 */
@FunctionalInterface
public interface TweetSuccess {
    /**
     * 正常にツイートされた時に呼び出されます。
     * @param model 正常にツイートされたツイートの情報
     * @since 1.0.0
     */
    void success(Status model);
}
