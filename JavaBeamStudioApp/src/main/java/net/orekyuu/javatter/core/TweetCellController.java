package net.orekyuu.javatter.core;

import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import net.orekyuu.javatter.api.twitter.ClientUser;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class TweetCellController implements Initializable {

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
    private Status status;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // 特になし
    }

    /**
     * アイテムの内容をStatusに従って切り替える
     *
     * @param status
     *            受け取ったステータス
     */
    public void updateTweetCell(Status status) {
        this.status = status;
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
    protected void favoriteAction(ActionEvent e) {
        // Twitterクラスがあれば取得し、なければnullにしておく
        Twitter twitter = clientUser.getTwitter() != null ? clientUser
                .getTwitter() : null;
        if (twitter != null) {
            // Twitterクラスがあれば
            if (status.isFavorited()) {
                // お気に入りにあるなら
                try {
                    // お気に入りから解除
                    twitter.destroyFavorite(status.getId());
                } catch (TwitterException e1) {
                    e1.printStackTrace();
                }
            } else {
                // お気に入りにないなら
                try {
                    // お気に入りに追加
                    twitter.createFavorite(status.getId());
                } catch (TwitterException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
