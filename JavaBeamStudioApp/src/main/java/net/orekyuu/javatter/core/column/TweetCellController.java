package net.orekyuu.javatter.core.column;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.api.entity.StatusEntity;
import net.orekyuu.javatter.api.inject.Inject;
import net.orekyuu.javatter.api.loader.FxLoader;
import net.orekyuu.javatter.api.models.*;
import net.orekyuu.javatter.api.models.Status;
import net.orekyuu.javatter.api.service.StatusService;
import net.orekyuu.javatter.api.service.UserService;
import net.orekyuu.javatter.core.models.*;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.util.tasks.GetIconTask;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import net.orekyuu.javatter.core.Main;
import net.orekyuu.javatter.core.config.GeneralConfigHelper;
import net.orekyuu.javatter.core.config.NameDisplayType;
import net.orekyuu.javatter.core.userprofile.UserProfilePresenter;
import twitter4j.*;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class TweetCellController implements Initializable {

    @FXML
    private VBox box;
    @FXML
    private TextFlow tweet;
    @FXML
    private HBox replyRoot;
    @FXML
    private ImageView replyImage;
    @FXML
    private Label replyName;
    @FXML
    private TextFlow replyText;
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
    private Status status;

    private Task<Image> imageTask;

    @Inject
    private UserService userService;
    @Inject
    private StatusService statusService;

    /**
     * アイテムの内容をStatusに従って切り替える
     *
     * @param status 受け取ったステータス
     */
    public void updateTweetCell(Status status) {

        Status retweetFrom = status.getRetweetFrom();
        Status s = retweetFrom == null ? status : retweetFrom;
        this.status = s;
        name.setText(getConfigFormatName(s.getOwner()));
        time.setText(s.getCreatedAt().format(
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        updateTweetTextFlow(s, tweet);
        updateImagePreview(s);
        via.setText(s.getViaName());
        // イメージ設定(非同期処理)
        if (retweetFrom == null) {
            if (imageTask != null && imageTask.isRunning()) {
                imageTask.cancel();
            }
            imageTask = new GetIconTask(status.getOwner());
            imageTask.setOnSucceeded( e-> {
                profileimage.setImage(imageTask.getValue());
                rtSourceUser.setImage(null);
                rtSourceUser.setVisible(false);
            });
            TaskUtil.startTask(imageTask);
        } else {
            startRtweetIconTask(status, retweetFrom);
        }

        // リプライ先
        if (s.getReplyStatusId() != -1) {
            Task<Status> modelTask = new Task<Status>() {
                @Override
                protected Status call() throws Exception {
                    Optional<Status> byID = statusService.findByID(s.getReplyStatusId());
                    byID.ifPresent(TweetCellController.this::setReplyImage);
                    return s;
                }

                @Override
                protected void succeeded() {
                    updateTweetTextFlow(getValue(), replyText);
                    replyName.setText(getConfigFormatName(getValue().getOwner()));
                    replyRoot.setVisible(true);
                    box.getChildren().remove(replyRoot);
                    box.getChildren().add(replyRoot);
                }
            };
            TaskUtil.startTask(modelTask);
        } else {
            box.getChildren().remove(replyRoot);
            replyRoot.setVisible(false);
            replyName.setText("");
            replyText.getChildren().clear();
            replyImage.setImage(null);
        }
    }

    private GetIconTask sourceIconTask;
    private GetIconTask rtOwnerIconTask;
    private void startRtweetIconTask(Status status, Status rt) {
        if (sourceIconTask != null && sourceIconTask.isRunning()) {
            sourceIconTask.cancel();
        }
        sourceIconTask = new GetIconTask(status.getOwner());
        sourceIconTask.setOnSucceeded(e -> {
            rtSourceUser.setImage(sourceIconTask.getValue());
            rtSourceUser.setVisible(true);
        });
        if (rtOwnerIconTask != null && rtOwnerIconTask.isRunning()) {
            rtOwnerIconTask.cancel();
        }
        rtOwnerIconTask = new GetIconTask(rt.getOwner());
        rtOwnerIconTask.setOnSucceeded(e -> profileimage.setImage(rtOwnerIconTask.getValue()));
        TaskUtil.startTask(sourceIconTask);
        TaskUtil.startTask(rtOwnerIconTask);
    }

    private GetIconTask replyIconTask;
    private void setReplyImage(Status status) {
        if (replyIconTask != null && replyIconTask.isRunning()) {
            replyIconTask.cancel();
        }

        replyIconTask = new GetIconTask(status.getOwner());
        replyIconTask.setOnSucceeded(e -> replyImage.setImage(replyIconTask.getValue()));
        TaskUtil.startTask(replyIconTask);
    }

    private void updateImagePreview(Status status) {
        imgPreview.getChildren().clear();
        List<MediaEntity> medias = status.getMedias();
        for (MediaEntity e : medias) {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(64);
            imageView.setFitHeight(64);
            imgPreview.getChildren().add(imageView);
            Task<Image> task = new Task<Image>() {
                @Override
                protected Image call() throws Exception {
                    return new Image(e.getMediaURL());
                }

                @Override
                protected void succeeded() {
                    imageView.setImage(getValue());
                }
            };
            TaskUtil.startTask(task);

            imageView.setOnMouseClicked(clickEvent -> {
                new ImageViewerBuilder().setMedia(imageView.getImage()).show();
            });
        }
    }

    private void updateTweetTextFlow(Status status, TextFlow textFlow) {
        textFlow.getChildren().clear();
        List<TweetEntity> entities = new LinkedList<>();
        entities.addAll(status.getHashtags());
        entities.addAll(status.getMentions());
        entities.addAll(status.getUrls());
        entities.addAll(status.getMedias());
        entities.sort(Comparator.comparingInt(TweetEntity::getStart));
        if (entities.size() == 0) {
            textFlow.getChildren().add(new Text(status.getText()));
            return;
        }
        int start = 0;
        for (TweetEntity entity : entities) {
            if (entity.getStart() - start > 0)
                textFlow.getChildren().add(new Text(status.getText().substring(start, entity.getStart())));
            addTweetEntity(entity, textFlow);
            start = entity.getEnd();
        }
        TweetEntity last = entities.get(entities.size() - 1);
        if (status.getText().length() - last.getEnd() > 0)
            textFlow.getChildren().add(new Text(status.getText().substring(last.getEnd(), status.getText().length())));
    }

    private void addTweetEntity(TweetEntity entity, TextFlow textFlow) {
        if (entity instanceof URLEntity) {
            Hyperlink hyperlink = new Hyperlink(((URLEntity) entity).getExpandedURL());
            hyperlink.setOnAction(this::openBrowser);
            textFlow.getChildren().add(hyperlink);
        }
        if (entity instanceof UserMentionEntity) {
            Hyperlink hyperlink = new Hyperlink("@" + entity.getText());
            hyperlink.setOnAction(e -> openUserProfile(((UserMentionEntity) entity).getId()));
            textFlow.getChildren().add(hyperlink);
        }
        if (entity instanceof HashtagEntity) {
            Hyperlink hyperlink = new Hyperlink("#" + entity.getText());
            textFlow.getChildren().add(hyperlink);
        }
    }

    private void openBrowser(ActionEvent event) {
        Hyperlink link = (Hyperlink) event.getSource();
        openBrowser(link.getText());
    }

    private void openBrowser(String url) {
        TaskUtil.startTask(new Task() {
            @Override
            protected Object call() {
                try {
                    Desktop.getDesktop().browse(new URL(url).toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
                return null;
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
     * リプライボタンから呼び出される。 リプライを行う。
     */
    @FXML
    private void reply() {
        API
                .getInstance()
                .getApplication()
                .getCurrentWindow()
                .setReply(status.getStatusId(),
                        "@" + status.getOwner().getScreenName() + " ");
    }

    /**
     * お気に入りボタンから呼び出されるメソッド お気に入りの追加/解除を行う
     */
    @FXML
    private void favoriten() {
        Twitter twitter = clientUser.getTwitter();
        TaskUtil.startTask(new Task() {
            @Override
            protected Object call() {
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
                return null;
            }
        });
    }

    /**
     * リツイートボタンから呼び出される。 リツイートとリツイートの解除を行う。
     */
    @FXML
    private void retweet() {
        Twitter twitter = clientUser.getTwitter();
        TaskUtil.startTask(new Task() {
            @Override
            protected Object call() {
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
                return null;
            }
        });
    }

    @FXML
    private void openVia() {
        openBrowser(status.getViaLink());
    }

    private static NameDisplayType nameDisplayType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (nameDisplayType == null) {
            String type = GeneralConfigHelper.loadConfigFromDB().getNameDisplayType();
            nameDisplayType = NameDisplayType.valueOf(type);
        }
        root.layoutXProperty().addListener(e -> root.setLayoutX(0));
        root.layoutYProperty().addListener(e -> root.setLayoutY(0));
    }

    private String getConfigFormatName(net.orekyuu.javatter.api.models.User user) {
        switch (nameDisplayType) {
            case NAME:
                return user.getName();
            case ID:
                return "@" + user.getScreenName();
            case ID_NAME:
                return "@" + user.getScreenName() + " / " + user.getName();
            case NAME_ID:
                return user.getName() + " / " + "@" + user.getScreenName();
            default:
                throw new IllegalArgumentException(nameDisplayType.name());
        }
    }

    private void openUserProfile(long l) {
        Optional<net.orekyuu.javatter.api.models.User> byID = userService.findByID(l, null);
        byID.ifPresent(this::openUserProfile);
    }

    @FXML
    private void openUserProfile() {
        openUserProfile(status.getOwner());
    }

    private void openUserProfile(net.orekyuu.javatter.api.models.User usermodel) {
        FxLoader loader = new FxLoader();
        try {
            Parent root = loader.load(Main.class.getResourceAsStream("userprofile.fxml"));
            UserProfilePresenter presenter = loader.getController();
            presenter.setUser(usermodel);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(usermodel.getName() + "さんのプロファイル");
            stage.initOwner(API.getInstance().getApplication().getPrimaryStage().getScene().getWindow());
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openStatusURL() {
        openBrowser(String.format("https://twitter.com/%s/status/%d", status.getOwner().getScreenName(), status.getStatusId()));
    }
    @FXML
    private void sendJavaBeam(){
        clientUser.createTweet().setAsync().setReplyTo(status.getStatusId())
        .setText("@"+status.getOwner().getScreenName()+" Javaビームﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞwwwwww").tweet();
    }
}
