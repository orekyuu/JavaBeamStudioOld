package net.orekyuu.javatter.core.twitter;

import net.orekyuu.javatter.api.twitter.ClientUser;
import twitter4j.auth.AccessToken;

import java.util.LinkedList;
import java.util.List;

/**
 * ローカルに保存されているユーザー
 */
public class LocalClientUser implements ClientUser {

    private AccessToken token;

    public LocalClientUser(AccessToken token) {
        this.token = token;
    }

    @Override
    public AccessToken getAccessToken() {
        return token;
    }

    @Override
    public String getName() {
        return token.getScreenName();
    }

    /**
     * ローカルのデータベースへユーザーを保存します。
     */
    public void save() {

    }

    /**
     * ローカルのデータベースからユーザーを読み込みます。
     *
     * @return データベースから読み込んだユーザーのリスト
     */
    public static List<ClientUser> loadClientUsers() {
        LinkedList<ClientUser> users = new LinkedList<>();
        return users;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LocalClientUser{");
        sb.append("token=").append(token);
        sb.append('}');
        return sb.toString();
    }
}
