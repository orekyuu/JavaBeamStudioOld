package net.orekyuu.javatter.core;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.twitter.Tweet;
import net.orekyuu.javatter.api.twitter.ClientUserRegister;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;
import net.orekyuu.javatter.api.twitter.ClientUser;

import java.util.*;
import java.io.File;
import java.net.URL;

import net.orekyuu.javatter.core.util.twitter.TweetBuilder;
import twitter4j.TwitterException;

public class MainWindowPresenter implements Initializable {
    @FXML
    private HBox hbox;
    @FXML
    private TextArea tweetTextArea;
    @FXML
    private Text remaining;
    @FXML
    private ImageView clientUserImage;
    @FXML
    private ImageView tweetImageView1;
    @FXML
    private ImageView tweetImageView2;
    @FXML
    private ImageView tweetImageView3;
    @FXML
    private ImageView tweetImageView4;
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
    private List<File> imageFiles = new ArrayList<>();
    private List<ImageView> appendedImagesViews = new ArrayList<>();
    private List<ImageView> hoverImageViews = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users = ClientUserRegister.getInstance().getUsers(s -> true);
        Platform.runLater(() -> {
            try {
                for (ClientUser user : users) {
                    myProfileImage.add(new Image(user.getTwitter()
                            .verifyCredentials().getProfileImageURL()));
                }
                clientUserImage.setImage(myProfileImage.get(nowUserIndex));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        });
        initPreviewImageViews();
        tweetTextArea.setOnDragOver(this::onDrop);
        tweetTextArea.setOnDragDropped(this::onDroped);
        NumberBinding length = Bindings.subtract(140, Bindings.length(tweetTextArea.textProperty()));
        remaining.textProperty().bind(Bindings.convert(length));

    }

    private void initPreviewImageViews() {
        hoverImageViews.add(hoverImageView1);
        hoverImageViews.add(hoverImageView2);
        hoverImageViews.add(hoverImageView3);
        hoverImageViews.add(hoverImageView4);
        appendedImagesViews.add(tweetImageView1);
        appendedImagesViews.add(tweetImageView2);
        appendedImagesViews.add(tweetImageView3);
        appendedImagesViews.add(tweetImageView4);

        hoverImageViews.forEach(i -> {
            i.setOnMouseEntered(this::onMouseEnter);
            i.setOnMouseExited(this::onMouseExit);
        });
    }

    private void onMouseExit(MouseEvent mouseEvent) {
        System.out.println("exit");
        Node node = (Node) mouseEvent.getSource();
        node.setOpacity(0);
    }

    private void onMouseEnter(MouseEvent mouseEvent) {
        System.out.println("enter");
        Node node = (Node) mouseEvent.getSource();
        node.setOpacity(1.0);
    }

    public void addColumn(ActionEvent actionEvent) {
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

    public void removeColumn(ActionEvent actionEvent) {
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
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionDialogBuilder.create(e);
        }
    }

    // ツイートの実行
    public void tweet() {
        Tweet builder = new TweetBuilder()
                .setText(tweetTextArea.getText())
                .setClientUser(users.get(nowUserIndex))
                .setOnTweetSuccess(s -> System.out.println(s.getText()))
                .setAsync();
        imageFiles.forEach(builder::addMedia);
        builder.tweet();
        Platform.runLater(() -> {
            appendedImagesViews.forEach(e -> e.setImage(null));
            imageFiles.clear();
            tweetTextArea.setText("");
        });
    }

    // Javaビームです。
    public void javaBeam() {
        new TweetBuilder().setClientUser(users.get(nowUserIndex))
                .setText("Javaビームﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞwwwwww").tweet();
    }

    // ユーザーアイコンのクリックによりツイートを行うユーザーを変更します。
    public void changeUser() {
        if (users.isEmpty())
            return;
        nowUserIndex = (nowUserIndex + 1) % users.size();
        clientUserImage.setImage(myProfileImage.get(nowUserIndex));
    }

    // ドロップ前
    public void onDrop(DragEvent e) {
        Dragboard dragboard = e.getDragboard();
        if (dragboard.hasFiles() && imageFiles.size() < 4) {
            e.acceptTransferModes(TransferMode.COPY);
        }
    }

    // ドロップ後
    public void onDroped(DragEvent e) {
        Dragboard dragboard = e.getDragboard();
        int imageFilesSize = imageFiles.size();
        int dragboardSize = dragboard.getFiles().size();
        if (dragboard.hasFiles()
                && (imageFilesSize < 4 && dragboardSize + imageFilesSize <= 4)) {
            for (int i = 0; i < dragboardSize; i++) {
                imageFiles.add(dragboard.getFiles().get(i));
            }
            Platform.runLater(() -> {
                for (int i = imageFilesSize; i < dragboardSize + imageFilesSize; i++) {
                    appendedImagesViews.get(i).setImage(
                            new Image(imageFiles.get(i).toURI().toString()));
                }
            });

            e.setDropCompleted(true);
            imageFiles.forEach(f -> System.out.println(f.getName()));

        } else {
            e.setDropCompleted(false);
        }
    }
}
