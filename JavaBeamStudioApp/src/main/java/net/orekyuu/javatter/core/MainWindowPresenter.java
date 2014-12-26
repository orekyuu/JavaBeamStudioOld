package net.orekyuu.javatter.core;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.api.CurrentWindow;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.twitter.ClientUserRegister;
import net.orekyuu.javatter.api.twitter.TweetBuilder;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;
import net.orekyuu.javatter.core.util.twitter.TweetBuilderImpl;
import twitter4j.TwitterException;

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
        users = ClientUserRegister.getInstance().getUsers(s -> true);
        currentUser.setValue(getCurrentUser().orElse(null));
        currentUserProperty.bind(currentUser);

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

        }
        initPreviewImageViews();
        tweetTextArea.setOnDragOver(this::onDrop);
        tweetTextArea.setOnDragDropped(this::onDroped);
        NumberBinding length = Bindings.subtract(140, Bindings.length(tweetTextArea.textProperty()));
        remaining.textProperty().bind(Bindings.convert(length));
        tweetTextArea.setOnKeyPressed(this::tweetShortCut);
    }

    private void tweetShortCut(KeyEvent keyEvent) {
        if (!tweetTextArea.getText().isEmpty()) {
            if (keyEvent.isShortcutDown()
                    && keyEvent.getCode().equals(KeyCode.ENTER)) {
                tweet();
            }
        }
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

    public void addColumn() {
        FXMLLoader loader = new FXMLLoader();
        try {
            Node node = loader.load(getClass().getResourceAsStream(
                    "column.fxml"));
            HBox.setHgrow(node, Priority.ALWAYS);
            hbox.getChildren().add(node);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionDialogBuilder.create(e);
        }
    }

    public void removeColumn() {
        if (hbox.getChildren().size() != 0)
            hbox.getChildren().remove(hbox.getChildren().size() - 1);
    }

    public void openConfig() {
        FXMLLoader loader = new FXMLLoader();
        try {
            Parent parent = loader.load(getClass().getResourceAsStream(
                    "config.fxml"));
            Scene scene = new Scene(parent);
            scene.getStylesheets().add(
                    Main.class.getResource("javabeamstudio.css")
                            .toExternalForm());
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
    public void tweet() {
        getCurrentUser().ifPresent(user -> {
            TweetBuilder builder = new TweetBuilderImpl()
                    .setText(tweetTextArea.getText())
                    .setClientUser(user)
                    .setOnTweetSuccess(s -> System.out.println(s.getText()))
                    .setAsync();
            appendedImagesViews.stream()
                    .filter(p -> p.getPreviewFile() != null)
                    .map(PreviewImage::getPreviewFile)
                    .forEach(builder::addMedia);
            if(isReply){
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
    public void javaBeam() {
        getCurrentUser().ifPresent(user -> new TweetBuilderImpl().setClientUser(user)
                        .setText("Javaビームﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞwwwwww").tweet());
    }

    // ユーザーアイコンのクリックによりツイートを行うユーザーを変更します。
    public void changeUser() {
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
        setText(destinationName);
    }

    @Override
    public List<File> getappendedImages() {
        List<File> imageFiles = appendedImagesViews.stream()
                .filter(p -> p.getPreviewFile() != null)
                .map(PreviewImage::getPreviewFile).collect(Collectors.toList());
        if (!imageFiles.isEmpty()) {
            return null;
        } else {
            return imageFiles;
        }
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
    public String getText() {
        return tweetTextArea.getText();
    }

    @Override
    public void setText(String text) {
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
