package net.orekyuu.javatter.api;

import net.orekyuu.javatter.api.twitter.ClientUser;
/**
 * カラムを表すインターフェース
 *
 */
public interface JavatterColumn {
	/**
	 * クライアントユーザー
	 * 
	 * @param clientUser
	 */
	void setClientUser(ClientUser clientUser);
}
