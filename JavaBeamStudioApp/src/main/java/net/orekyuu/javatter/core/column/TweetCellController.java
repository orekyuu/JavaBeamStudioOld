package net.orekyuu.javatter.core.column;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.cache.IconCache;
import net.orekyuu.javatter.core.config.GeneralConfigHelper;
import net.orekyuu.javatter.core.config.NameDisplayType;
import net.orekyuu.javatter.core.models.StatusModel;
import net.orekyuu.javatter.core.models.UserModel;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class TweetCellController implements Initializable {

    @FXML
    public TextFlow tweet;
    @FXML
    private Hyperlink via;
    @FXML
    private ImageView rtSourceUser;
    @FXML
    private Label name;
    @FXML
    private Label time;
    @FXML
    private ImageView profileimage;

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

        StatusModel retweetFrom = status.getRetweetFrom();
        StatusModel s = retweetFrom == null ? status : retweetFrom;
        this.status = s;
        name.setText(getConfigFormatName(s.getOwner()));
        time.setText(s.getCreatedAt().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        tweet.getChildren().clear();
        tweet.getChildren().add(new Text(s.getText()));
        via.setText(s.getViaName());
        // イメージ設定(非同期処理)
        CompletableFuture.runAsync(() -> {

            if(retweetFrom == null) {
                Image img = IconCache.getImage(status.getOwner().getProfileImageURL());
                Platform.runLater(() -> {
                    profileimage.setImage(img);
                    rtSourceUser.setImage(null);
                    rtSourceUser.setVisible(false);
                });
            } else {
                Image source = IconCache.getImage(status.getOwner().getProfileImageURL());
                Image img = IconCache.getImage(retweetFrom.getOwner().getProfileImageURL());
                Platform.runLater(() -> {
                    profileimage.setImage(img);
                    rtSourceUser.setImage(source);
                    rtSourceUser.setVisible(true);
                });
            }
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

    public void openVia() {
        try {
            Desktop.getDesktop().browse(new URL(status.getViaLink()).toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private NameDisplayType nameDisplayType;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String type = GeneralConfigHelper.loadConfigFromDB().getNameDisplayType();
        nameDisplayType = NameDisplayType.valueOf(type);
    }

    private String getConfigFormatName(UserModel user) {
        switch (nameDisplayType) {
            case NAME: return user.getName();
            case ID: return "@" + user.getScreenName();
            case ID_NAME: return "@" + user.getScreenName() + " / " + user.getName();
            case NAME_ID: return user.getName() + " / " + "@" + user.getScreenName();
            default: throw new IllegalArgumentException(nameDisplayType.name());
        }
    }
}
