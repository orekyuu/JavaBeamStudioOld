package net.orekyuu.javatter.core;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import net.orekyuu.javatter.api.twitter.ClientUser;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class TweetCellController  {

	@FXML
	private Label screen_name;
	@FXML
	private Label name;
	@FXML
	private Label time;
	@FXML
	private Label tweet_sentence;
	@FXML
	private ImageView profileimage;
	@FXML
	private TextFlow caption;

	private ClientUser clientUser;

	/**
	 * timeラベル用の時刻フォーマット
	 */
	private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
	private long id;
	private boolean isRetweeted;
	private boolean isFavorited;

	/**
	 * アイテムの内容をStatusに従って切り替える
	 *
	 * @param status
	 *            受け取ったステータス
	 */
	public void updateTweetCell(Status status) {
		id = status.getId();
		isRetweeted = status.isRetweeted();
        isFavorited = status.isFavorited();
		User user = status.getUser();
		
		// 時間の取得と表示
		ZonedDateTime ztd = status.getCreatedAt().toInstant()
				.atZone(ZoneId.systemDefault());

		Platform.runLater(() -> {
			screen_name.setText(user.getScreenName());
			name.setText(user.getName());
			time.setText(ztd.format(DateTimeFormatter
					.ofPattern(DATE_TIME_FORMAT)));
			tweet_sentence.setText(status.getText());
		});
		// イメージ設定(非同期処理)
		CompletableFuture.runAsync(() -> {
			Image img = new Image(user.getProfileImageURL());
			Platform.runLater(() -> profileimage.setImage(img));
		});
	}

	/**
	 * clientUserをセットする
	 *
	 * @param status
	 */
	public void setClientUser(ClientUser clientuser) {
		clientUser = clientuser;
	}

	/**
	 * お気に入りボタンから呼び出されるメソッド お気に入りの追加/解除を行う
	 *
	 * @param e
	 *            アクションイベント
	 */
	@FXML
	protected void favoriten() {
		Twitter twitter = clientUser.getTwitter();
		if (isFavorited) {
			// お気に入りにあるなら
			try {
				// お気に入りから解除
				twitter.destroyFavorite(id);
			} catch (TwitterException e1) {
				e1.printStackTrace();
			}
		} else {
			// お気に入りにないなら
			try {
				// お気に入りに追加
				twitter.createFavorite(id);
			} catch (TwitterException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * リツイートボタンから呼び出される。 リツイートとリツイートの解除を行う。
	 * 
	 * @param e
	 *            アクションイベント
	 */
	@FXML
	protected void retweet() {
		Twitter twitter = clientUser.getTwitter();
		// RTされたものでなければ
		if (!isRetweeted) {
			// RTする
			try {
				twitter.retweetStatus(id);
			} catch (TwitterException e1) {
				e1.printStackTrace();
			}
			// RTされたものならば
		} else {
			try {
				// RTから削除
				twitter.destroyStatus(id);
			} catch (TwitterException e1) {
				e1.printStackTrace();
			}
		}
	}
}
