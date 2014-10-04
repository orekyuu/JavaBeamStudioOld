package net.orekyuu.javatter.api.twitter;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * クライアントに認証されているユーザーのレジスタ
 */
public class ClientUserRegister {

    private static ClientUserRegister instance = new ClientUserRegister();
    private final List<ClientUser> users=new LinkedList<>();
    private ClientUserRegister () {

    }

    /**
     * レジスタのインスタンスを返します。
     * @return 唯一のインスタンス
     */
    public static ClientUserRegister getInstance() {
        return instance;
    }

    /**
     * Javatterにユーザーを登録します。
     * @param user 登録を行うユーザー
     */
    public void registerUser(ClientUser user) {
    	users.add(user);
    }

    /**
     * Javatterに登録されたユーザーを返します。
     * @return 登録されているユーザーのリスト
     */
    public List<ClientUser> registeredUserList() {
        return new LinkedList<>(users);
    }

    /**
     * 登録さているユーザー数を返します。
     * @return ユーザー数
     */
    public int registeredUserCount() {
        return  users.size();
    }

    /**
     * 条件に当てはまるユーザーの削除を行う。
     * @param condition ユーザーの削除条件
     */
    public void removeUsers(Predicate<ClientUser> condition) {
    	users.removeIf(condition);
    }

    /**
     * 条件に当てはまるユーザーの抽出を行う。
     * @param condition ユーザーの抽出条件
     * @return 条件にヒットしたユーザーのリスト
     */
    public List<ClientUser> getUsers(Predicate<ClientUser> condition) {
        return users.stream().filter(condition).collect(Collectors.toList());
    }
}
