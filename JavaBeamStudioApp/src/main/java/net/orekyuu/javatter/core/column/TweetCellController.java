package net.orekyuu.javatter.core.column;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.cache.IconCache;
import net.orekyuu.javatter.core.models.StatusModel;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

public class TweetCellController {

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
    private StatusModel status;
    /**
     * アイテムの内容をStatusに従って切り替える
     *
     * @param status 受け取ったステータス
     */
    public void updateTweetCell(StatusModel status) {

        screen_name.setText(status.getOwner().getScreenName());
        name.setText(status.getOwner().getName());
        time.setText(status.getCreatedAt().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        tweet_sentence.setText(status.getText());
        // イメージ設定(非同期処理)
        CompletableFuture.runAsync(() -> {
            Image img = IconCache.getImage(status.getOwner().getProfileImageURL());
            Platform.runLater(() -> profileimage.setImage(img));
        });
    }

    /**
     * clientUserをセットする
     *
     * @param clientUser カラムの持ち主
     */
    public void setClientUser(ClientUser clientUser) {
        this.clientUser = clientUser;
    }

    /**
     * お気に入りボタンから呼び出されるメソッド お気に入りの追加/解除を行う
     *
     */
    @FXML
    protected void favoriten() {
        Twitter twitter = clientUser.getTwitter();
        if (status.isFavorited()) {
            // お気に入りにあるなら
            try {
                // お気に入りから解除
                twitter.destroyFavorite(status.getStatusId());
            } catch (TwitterException e1) {
                e1.printStackTrace();
            }
        } else {
            // お気に入りにないなら
            try {
                // お気に入りに追加
                twitter.createFavorite(status.getStatusId());
            } catch (TwitterException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * リツイートボタンから呼び出される。 リツイートとリツイートの解除を行う。
     *
     */
    @FXML
    protected void retweet() {
        Twitter twitter = clientUser.getTwitter();
        // RTされたものでなければ
        if (!status.isRetweeted()) {
            // RTする
            try {
                twitter.retweetStatus(status.getStatusId());
            } catch (TwitterException e1) {
                e1.printStackTrace();
            }
            // RTされたものならば
        } else {
            try {
                // RTから削除
                twitter.destroyStatus(status.getStatusId());
            } catch (TwitterException e1) {
                e1.printStackTrace();
            }
        }
    }
}
