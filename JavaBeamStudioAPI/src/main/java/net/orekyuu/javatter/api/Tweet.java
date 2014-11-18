package net.orekyuu.javatter.api;

import java.io.File;

import net.orekyuu.javatter.api.twitter.ClientUser;

/**
 * ツイートに関するインターフェース
 */
public interface Tweet {
	/**
	 * ツイートを送信する。
	 * 
	 * @param user
	 *            クライアントユーザー
	 * @param text
	 *            送信するテキスト
	 */
	public void sendTweet(ClientUser user, String text);

	/**
	 * 
	 * @param user
	 *            クライアントユーザー
	 * @param text
	 *            送信するテキスト
	 * @param file
	 *            添付するイメージファイル
	 */
	public void sendImageTweet(ClientUser user, String text, File file);

	/**
	 * ツイートに対してリプライを送信する
	 * 
	 * @param user
	 *            クライアントユーザー
	 * @param destinationId
	 *            リプライ元のツイートのid
	 * @param text
	 *            送信するテキスト
	 * @param destinationName
	 *            送信相手のスクリーンネーム
	 */
	public void sendReply(ClientUser user, long destinationId, String text,
			String destinationName);

	/**
	 * 
	 * @param user
	 *            クライアントユーザー
	 * @param destinationId
	 *            リプライ元のツイートのid
	 * @param text
	 *            送信するテキスト
	 * @param destinationName
	 *            送信相手のスクリーンネーム
	 * @param file
	 *            添付するイメージファイル
	 */
	public void sendImageReply(ClientUser user, long destinationId,
			String text, String destinationName, File file);
}
