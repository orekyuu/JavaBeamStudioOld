package net.orekyuu.javatter.api.service;

import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.models.User;

import java.util.Optional;

/**
 * Userの情報の操作を行うためのサービスです。
 */
public interface UserService {

    /**
     * UserEntityが存在しなければ新しくUserEntityを作成します。
     * @param user User
     * @param account 管理するアカウント
     * @return 作成されたUserEntity またはすでにあるEntity
     */
    User createIfNotExist(twitter4j.User user, ClientUser account);

    /**
     * UserEntityの情報を更新します。
     * @param user User
     * @param account 管理するアカウント
     * @return 更新されたUser
     */
    User update(User user, ClientUser account);

    /**
     * スクリーンネームからアカウントを検索します。
     * @param screenName 検索するアカウント名
     * @param account 検索を行うアカウント
     * @return 検索結果
     */
    Optional<User> findByScreenName(String screenName, ClientUser account);

    /**
     * IDからアカウントを検索します。
     * @param id 検索するアカウントID
     * @param account 検索を行うアカウント
     * @return 検索結果
     */
    Optional<User> findByID(long id, ClientUser account);
}
