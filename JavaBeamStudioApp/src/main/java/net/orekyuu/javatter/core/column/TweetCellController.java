package net.orekyuu.javatter.core.column;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.cache.IconCache;
import net.orekyuu.javatter.core.config.GeneralConfigHelper;
import net.orekyuu.javatter.core.config.NameDisplayType;
import net.orekyuu.javatter.core.models.StatusModel;
import net.orekyuu.javatter.core.models.UserModel;
import twitter4j.*;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TweetCellController implements Initializable {

    @FXML
    public TextFlow tweet;
    @FXML
    private HBox imgPreview;
    @FXML
    private AnchorPane root;
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
        updateTweetTextFlow(s);
        updateImagePreview(s);
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

    private void updateImagePreview(StatusModel status) {
        imgPreview.getChildren().clear();
        List<MediaEntity> medias = status.getMedias();
        for (MediaEntity e : medias) {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(64);
            imageView.setFitHeight(64);
            imgPreview.getChildren().add(imageView);
            CompletableFuture.runAsync(() -> {
                Image image = new Image(e.getMediaURL());
                Platform.runLater(() -> imageView.setImage(image));
            });
        }
    }

    private void updateTweetTextFlow(StatusModel status) {
        tweet.getChildren().clear();
        List<TweetEntity> entities = new LinkedList<>();
        entities.addAll(status.getHashtags());
        entities.addAll(status.getMentions());
        entities.addAll(status.getUrls());
        entities.addAll(status.getMedias());
        //通常のテキストの位置を探す
        boolean[] text = new boolean[status.getText().length()];
        for (TweetEntity e : entities) {
            for (int i = e.getStart(); i < e.getEnd(); i++) {
                text[i] = true;
            }
        }
        List<TextEntity> texts = new LinkedList<>();
        //TextEntityを作成
        int start = 0;
        boolean oldFlag = true;
        for (int i = 0; i < text.length; i++) {
            if(oldFlag && !text[i])
                start = i;
            if(!oldFlag && text[i]) {
                texts.add(new TextEntity(status.getText().substring(start, i), start, i));
            }
            if(text.length - 1 == i && !text[i]) {
                texts.add(new TextEntity(status.getText().substring(start, i + 1), start, i));
            }

            oldFlag = text[i];
        }

        //開始地点で並べ替え
        entities.addAll(texts);
        entities.sort(Comparator.comparingInt(TweetEntity::getStart));

        //順番にTextFlowに入れていく
        for (TweetEntity entity : entities) {
            if (entity instanceof TextEntity) {
                tweet.getChildren().add(new Text(entity.getText()));
            }
            if (entity instanceof URLEntity) {
                Hyperlink hyperlink = new Hyperlink(((URLEntity) entity).getExpandedURL());
                System.out.println("url: " + entity.toString());
                hyperlink.setOnAction(this::openBrowser);
                tweet.getChildren().add(hyperlink);
            }
            if (entity instanceof UserMentionEntity) {
                Hyperlink hyperlink = new Hyperlink("@" + entity.getText());
                tweet.getChildren().add(hyperlink);
            }
            if (entity instanceof HashtagEntity) {
                Hyperlink hyperlink = new Hyperlink("#" + entity.getText());
                tweet.getChildren().add(hyperlink);
            }
        }
    }

    private void openBrowser(ActionEvent event) {
        Hyperlink link = (Hyperlink) event.getSource();
        try {
            Desktop.getDesktop().browse(new URL(link.getText()).toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private class TextEntity implements TweetEntity {

        private final String text;
        private final int start;
        private final int end;

        TextEntity(String text, int start, int end) {
            this.text = text;
            this.start = start;
            this.end = end;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public int getStart() {
            return start;
        }

        @Override
        public int getEnd() {
            return end;
        }
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
        root.layoutXProperty().addListener(e -> root.setLayoutX(0));
        root.layoutYProperty().addListener(e -> root.setLayoutY(0));
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
