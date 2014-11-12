package net.orekyuu.javatter.core;

import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import twitter4j.Status;
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

    /**
     * timeラベル用の時刻フォーマット
     */
    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

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
        User user = status.getUser();
        screen_name.setText(user.getScreenName());
        name.setText(user.getName());

        // 時間の取得と表示
        ZonedDateTime ztd = status.getCreatedAt().toInstant()
                .atZone(ZoneId.systemDefault());
        time.setText(ztd.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        tweet_sentence.setText(status.getText());
        // イメージ設定(非同期処理)
        CompletableFuture.runAsync(() -> {
            Image img = new Image(user.getProfileImageURL());
            Platform.runLater(() -> profileimage.setImage(img));
        });
    }
}
