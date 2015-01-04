package net.orekyuu.javatter.api.twitter;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * クライアントに認証されているユーザーのレジスタ
 *
 * @since 1.0.0
 */
public final class ClientUserRegister {

    private static ClientUserRegister instance = new ClientUserRegister();
    private final List<ClientUser> users = new LinkedList<>();
    private List<UserRegisterListener> listeners = new LinkedList<>();

    private ClientUserRegister() {

    }

    /**
     * レジスタのインスタンスを返します。
     *
     * @return 唯一のインスタンス
     * @since 1.0.0
     */
    public static ClientUserRegister getInstance() {
        return instance;
    }

    /**
     * Javatterにユーザーを登録します。
     *
     * @param user 登録を行うユーザー
     * @since 1.0.0
     */
    public void registerUser(ClientUser user) {
        users.add(user);
        listeners.forEach(i -> i.onChanged(registeredUserList()));
    }

    /**
     * Javatterに登録されたユーザーを返します。
     *
     * @return 登録されているユーザーのリスト
     * @since 1.0.0
     */
    public List<ClientUser> registeredUserList() {
        return new LinkedList<>(users);
    }

    /**
     * 登録さているユーザー数を返します。
     *
     * @return 登録されているユーザーの数
     * @since 1.0.0
     */
    public int registeredUserCount() {
        return users.size();
    }

    /**
     * 条件に当てはまるユーザーの削除を行う。
     *
     * @param condition ユーザーの削除条件
     * @since 1.0.0
     */
    public void removeUsers(Predicate<ClientUser> condition) {
        users.removeIf(condition);
        listeners.forEach(i -> i.onChanged(registeredUserList()));
    }

    /**
     * 条件に当てはまるユーザーの抽出を行う。
     *
     * @param condition ユーザーの抽出条件
     * @return 条件にヒットしたユーザーのリスト
     * @since 1.0.0
     */
    public List<ClientUser> getUsers(Predicate<ClientUser> condition) {
        return users.stream().filter(condition).collect(Collectors.toList());
    }

    /**
     * 登録情報変更時のリスナーを追加します。
     * @param listener {@link net.orekyuu.javatter.api.twitter.UserRegisterListener}
     */
    public void addUserChangeListener(UserRegisterListener listener) {
        listeners.add(listener);
    }

    /**
     * 登録情報変更時のリスナーを削除します。
     * @param listener 削除したい{@link net.orekyuu.javatter.api.twitter.UserRegisterListener}
     */
    public void removeUserChangeListener(UserRegisterListener listener) {
        listeners.remove(listener);
    }

    /**
     * 登録情報変更時のリスナーを空にします。
     */
    public void clearUserChangeListener() {
        listeners.clear();
    }
}
