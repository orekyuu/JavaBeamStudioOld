package net.orekyuu.javatter.api;

import net.orekyuu.javatter.api.twitter.ClientUser;
/**
 * カラムを表すインターフェース
 * @since 1.0.0
 */
public interface JavatterColumn {
	/**
	 * ユーザーが変更された時に{@link ClientUser}をセットします。
	 * 
	 * @param clientUser カラムを使用するユーザー
	 * @since 1.0.0
	 */
	void setClientUser(ClientUser clientUser);
}
