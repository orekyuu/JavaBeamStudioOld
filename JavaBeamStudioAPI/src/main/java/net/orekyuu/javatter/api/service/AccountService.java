package net.orekyuu.javatter.api.service;

import net.orekyuu.javatter.api.twitter.ClientUser;

import java.util.List;
import java.util.Optional;

/**
 * 登録されたアカウントを操作するサービス
 */
public interface AccountService {

    /**
     * アカウントを作成します。
     * @param name Twitterのスクリーンネーム
     * @param token アクセストークン
     * @param tokenSecret アクセストークンシークレット
     * @return 作成されたアカウント
     */
    ClientUser createAccount(String name, String token, String tokenSecret);

    /**
     * スクリーンネームからアカウントを取得します。
     * @param screenName スクリーンネーム
     * @return 見つかったユーザー
     */
    Optional<ClientUser> findByScreenName(String screenName);

    /**
     * すべてのアカウント情報を取得します。
     * @return 登録されているすべてのアカウント
     */
    List<ClientUser> findAll();

    /**
     * アカウントを削除します。
     * @param account 削除するアカウント
     */
    void removeAccount(ClientUser account);
}
