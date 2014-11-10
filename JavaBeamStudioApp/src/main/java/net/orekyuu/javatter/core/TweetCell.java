package net.orekyuu.javatter.core;

import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import twitter4j.Status;
import twitter4j.User;

public class TweetCell extends ListCell<Status> implements Initializable {

	@FXML
	Label screen_name;
	@FXML
	Label name;
	@FXML
	Label time;
	@FXML
	Label tweet_sentence;
	@FXML
	ImageView profileimage;
	@FXML
	TextFlow caption;

	/**
	 * timeラベル用の時刻フォーマット
	 */
	private String datetimeformatstring;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		datetimeformatstring = "yyyy/MM/dd HH:mm:ss";
	}

	/**
	 * アイテムの内容をStatusに従って切り替える
	 *
	 * @param status
	 *            受け取ったステータス
	 * @param empty
	 *            空かどうか
	 */
	@Override
	protected void updateItem(Status status, boolean empty) {
		// 現在はとりあえずの実装です

		// スーパークラスから必要な機能を継承
		super.updateItem(status, empty);
		if (empty) {
			// 空の場合は表示・描画を行わない
			setText(null);
			setGraphic(null);
		} else {
			// 空でない場合は
			// 名前の取得と表示
			User user = status.getUser();
			screen_name.setText(user.getScreenName());
			name.setText(user.getName());
			// 時間の取得と表示
			// TODO ゾーンIDの設定
			ZonedDateTime ztd = status.getCreatedAt().toInstant()
					.atZone(ZoneId.systemDefault());
			time.setText(ztd.format(DateTimeFormatter
					.ofPattern(datetimeformatstring)));
			tweet_sentence.setText(status.getText());
			// イメージ設定
			profileimage.setImage(new Image(user.getProfileImageURL()));
			// 描画
		}
	}
}
