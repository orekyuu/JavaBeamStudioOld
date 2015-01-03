package net.orekyuu.javatter.api.twitter;

import java.util.List;

/**
 * {@link net.orekyuu.javatter.api.twitter.ClientUser}が登録削除された時のイベントを受け取るリスナーです。
 */
@FunctionalInterface
public interface UserRegisterListener {

    /**
     * 変更があった時のイベント
     * @param users {@link net.orekyuu.javatter.api.twitter.ClientUserRegister}に登録されているユーザー
     */
    void onChanged(List<ClientUser> users);
}
