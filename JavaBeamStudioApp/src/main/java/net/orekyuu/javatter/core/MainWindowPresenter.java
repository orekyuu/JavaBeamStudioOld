package net.orekyuu.javatter.core;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.api.CurrentWindow;
import net.orekyuu.javatter.api.column.Column;
import net.orekyuu.javatter.api.models.OpenColumnInfo;
import net.orekyuu.javatter.core.entity.Account;
import net.orekyuu.javatter.core.entity.OpenColumnEntity;
import net.orekyuu.javatter.api.inject.Inject;
import net.orekyuu.javatter.api.loader.FxLoader;
import net.orekyuu.javatter.api.service.AccountService;
import net.orekyuu.javatter.api.service.ColumnManager;
import net.orekyuu.javatter.api.service.ColumnService;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.twitter.TweetBuilder;
import net.orekyuu.javatter.core.column.ColumnPresenter;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;
import twitter4j.TwitterException;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainWindowPresenter implements Initializable, CurrentWindow {
    @FXML
    private AnchorPane root;
    @FXML
    private Text clientUserName;
    @FXML
    private HBox hbox;
    @FXML
    private TextArea tweetTextArea;
    @FXML
    private Text remaining;
    @FXML
    private ImageView clientUserImage;
    @FXML
    private PreviewImage tweetImageView1;
    @FXML
    private PreviewImage tweetImageView2;
    @FXML
    private PreviewImage tweetImageView3;
    @FXML
    private PreviewImage tweetImageView4;
    @FXML
    private ImageView hoverImageView1;
    @FXML
    private ImageView hoverImageView2;
    @FXML
    private ImageView hoverImageView3;
    @FXML
    private ImageView hoverImageView4;

    @Inject
    private AccountService accountService;

    @Inject
    private ColumnService columnService;

    @Inject
    private ColumnManager columnManager;

    private int nowUserIndex = 0;
    private List<Image> myProfileImage = new ArrayList<>();
    private List<ClientUser> users;
    private List<PreviewImage> appendedImagesViews = new ArrayList<>();
    private Property<ClientUser> currentUserProperty = new SimpleObjectProperty<>();
    private Property<ClientUser> currentUser = new SimpleObjectProperty<>();
    private boolean isReply = false;
    private long destinationId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users = accountService.findAll().stream().collect(Collectors.toList());
        initUser();

        initPreviewImageViews();
        tweetTextArea.setOnDragOver(this::onDrop);
        tweetTextArea.setOnDragDropped(this::onDroped);
        NumberBinding length = Bindings.subtract(140, Bindings.length(tweetTextArea.textProperty()));
        remaining.textProperty().bind(Bindings.convert(length));
        tweetTextArea.setOnKeyPressed(this::tweetShortCut);
        initColumns();
    }

    private void initUser() {
        currentUser.setValue(getCurrentUser().orElse(null));
        currentUserProperty.bind(currentUser);
        myProfileImage.clear();
        nowUserIndex = 0;

        if (!users.isEmpty()) {
            Platform.runLater(() -> {
                try {
                    for (ClientUser user : users) {
                        myProfileImage.add(new Image(user.getTwitter()
                                .verifyCredentials().getProfileImageURL()));
                    }
                    clientUserImage.setImage(myProfileImage.get(nowUserIndex));
                    clientUserName.setText(getCurrentUser().get().getName());
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            });
        } else {
            Platform.runLater(() -> {
                clientUserImage.setImage(null);
                clientUserName.setText("null");
            });
        }
    }

    private void tweetShortCut(KeyEvent keyEvent) {
        if (!tweetTextArea.getText().isEmpty()) {
            if (keyEvent.isShortcutDown()
                    && keyEvent.getCode().equals(KeyCode.ENTER)) {
                tweet();
            }
        }
    }

    private void initColumns() {
        List<OpenColumnInfo> allColumn = columnService.findAllColumn();
        allColumn.forEach(this::addColumn);
    }

    private Optional<ClientUser> getCurrentUser() {
        if (users.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(users.get(nowUserIndex));
    }

    private void initPreviewImageViews() {
        List<ImageView> hoverImageViews = new LinkedList<>();
        hoverImageViews.add(hoverImageView1);
        hoverImageViews.add(hoverImageView2);
        hoverImageViews.add(hoverImageView3);
        hoverImageViews.add(hoverImageView4);
        appendedImagesViews.add(tweetImageView1);
        appendedImagesViews.add(tweetImageView2);
        appendedImagesViews.add(tweetImageView3);
        appendedImagesViews.add(tweetImageView4);

        for (int i = 0; i < appendedImagesViews.size(); i++) {
            appendedImagesViews.get(i).setHoverImageView(hoverImageViews.get(i));
        }
    }

    @FXML
    private void addColumn() {
        getAccount().ifPresent(account -> {
            OpenColumnInfo info = columnService.create(ApplicationImpl.EMPTY_COLUMN, account);
            addColumn(info);
        });
    }

    private Optional<ClientUser> getAccount() {
        if (users.isEmpty()) {
            return Optional.empty();
        }
        ClientUser user = users.get(nowUserIndex);
        return accountService.findByScreenName(user.getName());
    }

    private void addColumn(OpenColumnInfo entity) {
        Optional<Column> columnById = columnManager.findColumnById(entity.getColumnId());
        columnById.ifPresent(column -> {

            FxLoader loader = new FxLoader();
            try {
                TitledPane node = loader.load(getClass().getResourceAsStream("column.fxml"));
                HBox.setHgrow(node, Priority.ALWAYS);
                ColumnPresenter presenter = loader.getController();
                presenter.setColumn(entity);
                hbox.getChildren().add(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void removeColumn() {
        if (hbox.getChildren().size() != 0) {
            int size = hbox.getChildren().size();
            hbox.getChildren().remove(size - 1);
            columnService.removeByIndex(size - 1);
        }
    }

    @FXML
    private void openConfig() {
        FxLoader loader = new FxLoader();
        try {
            Parent parent = loader.load(getClass().getResourceAsStream(
                    "config.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("設定");
            stage.initOwner(API.getInstance().getApplication().getPrimaryStage().getScene().getWindow());
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionDialogBuilder.create(e);
        }
    }

    // ツイートの実行
    @FXML
    private void tweet() {
        getCurrentUser().ifPresent(user -> {
            TweetBuilder builder = user.createTweet()
                    .setText(tweetTextArea.getText())
                    .setOnTweetSuccess(s -> System.out.println(s.getText()))
                    .setAsync();
            appendedImagesViews.stream()
                    .filter(p -> p.getPreviewFile() != null)
                    .map(PreviewImage::getPreviewFile)
                    .forEach(builder::addMedia);
            if (isReply) {
                builder.setReplyTo(destinationId);
                isReply = false;
            }
            builder.tweet();
            Platform.runLater(() -> {
                appendedImagesViews.forEach(e -> e.setPreviewFile(null));
                tweetTextArea.setText("");
            });
        });
    }

    // Javaビームです。
    @FXML
    private void javaBeam() {
        getCurrentUser().ifPresent(user -> user.createTweet()
                .setText("Javaビームﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞwwwwww").setAsync().tweet());
    }

    // ユーザーアイコンのクリックによりツイートを行うユーザーを変更します。
    @FXML
    private void changeUser() {
        if (users.isEmpty())
            return;
        nowUserIndex = (nowUserIndex + 1) % users.size();
        clientUserImage.setImage(myProfileImage.get(nowUserIndex));
        clientUserName.setText(getCurrentUser().get().getName());
        currentUser.setValue(getCurrentUser().get());
    }

    // ドロップ前
    private void onDrop(DragEvent e) {
        Dragboard dragboard = e.getDragboard();
        long usedPreview = appendedImagesViews.stream()
                .filter(i -> i.getPreviewFile() != null)
                .count();
        if (dragboard.hasFiles() && usedPreview < 4) {
            e.acceptTransferModes(TransferMode.COPY);
        }
    }

    // ドロップ後
    private void onDroped(DragEvent e) {
        List<File> images = appendedImagesViews.stream()
                .filter(i -> i.getPreviewFile() != null)
                .map(PreviewImage::getPreviewFile)
                .collect(Collectors.toList());

        Dragboard dragboard = e.getDragboard();
        int dragboardSize = dragboard.getFiles().size();
        if (dragboard.hasFiles() && images.size() < 4) {
            int addSize = Math.min(4 - images.size(), dragboardSize);
            for (int i = 0; i < addSize; i++) {
                images.add(dragboard.getFiles().get(i));
            }
            updateTweetImagePreviews(images);

            e.setDropCompleted(true);

        } else {
            e.setDropCompleted(false);
        }
    }

    private void updateTweetImagePreviews(List<File> images) {
        // サイズチェック
        if (images.size() > appendedImagesViews.size()) {
            throw new IllegalArgumentException("images size > apendedImageViews size. images size: "
                    + images.size() + "appendedImageViews size: " + appendedImagesViews.size());
        }
        Platform.runLater(() -> {
            appendedImagesViews.forEach(p -> p.setPreviewFile(null));
            for (int i = 0; i < images.size(); i++) {
                appendedImagesViews.get(i).setPreviewFile(images.get(i));
            }
        });
    }

    @Override
    public void setReply(long id, String destinationName) {
        isReply = true;
        destinationId = id;
        setTweetText(destinationName);
    }

    @Override
    public List<File> getAppendedImages() {
        return appendedImagesViews.stream()
                .filter(p -> p.getPreviewFile() != null)
                .map(PreviewImage::getPreviewFile).collect(Collectors.toList());
    }


    @Override
    public void assignUser(ClientUser user) {
        boolean unFindedUser = false;
        int currentUser = nowUserIndex;
        if (users.isEmpty()) {
            while (users.get(nowUserIndex) != user) {
                nowUserIndex = (nowUserIndex + 1) % users.size();
                if (nowUserIndex == currentUser) {
                    unFindedUser = true;
                    break;
                }
            }
            if (unFindedUser) {
                // なんらかの理由でユーザーが見つからなかった旨のlogを表示
                nowUserIndex = currentUser;
                clientUserImage.setImage(myProfileImage.get(nowUserIndex));
                clientUserName.setText(getCurrentUser().get().getName());
            } else {
                clientUserImage.setImage(myProfileImage.get(nowUserIndex));
                clientUserName.setText(getCurrentUser().get().getName());
            }
        }
    }

    @Override
    public String getTweetText() {
        return tweetTextArea.getText();
    }

    @Override
    public void setTweetText(String text) {
        tweetTextArea.setText(text);
    }

    @Override
    public Property<ClientUser> getCurrentUserProperty() {
        return currentUserProperty;
    }

    @Override
    public StringProperty getTweetTextProperty() {
        return tweetTextArea.textProperty();
    }

}
