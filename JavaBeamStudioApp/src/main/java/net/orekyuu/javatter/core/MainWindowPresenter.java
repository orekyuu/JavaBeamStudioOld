package net.orekyuu.javatter.core;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;
import net.orekyuu.javatter.core.twitter.LocalClientUser;
import net.orekyuu.javatter.api.EditText;
import net.orekyuu.javatter.api.twitter.ClientUser;

import java.util.List;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import twitter4j.TwitterException;

public class MainWindowPresenter implements Initializable, EditText,
        OnDropListener, OnHoverListener, OnClickListener {
    @FXML
    private HBox hbox;
    @FXML
    private CustomTextArea tweetTextArea;
    @FXML
    private Text remaining;
    @FXML
    private ImageView clientUserImage;
    @FXML
    private ImageView tweetImage1;
    @FXML
    private ImageView tweetImage2;
    @FXML
    private ImageView tweetImage3;
    @FXML
    private ImageView tweetImage4;
    @FXML
    private CustomImageView hoverImageView1;
    @FXML
    private CustomImageView hoverImageView2;
    @FXML
    private CustomImageView hoverImageView3;
    @FXML
    private CustomImageView hoverImageView4;

    private static int nowUserIndex = 0;
    private List<Image> myProfileImage = new ArrayList<>();
    private List<ClientUser> users = new ArrayList<>();
    private static List<File> imageFiles = new ArrayList<>();
    private static List<ImageView> appendedImages = new ArrayList<>();
    private static List<CustomImageView> hoverImageViews = new ArrayList<>();
    private static Image hoverImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hoverImage = new Image(
                new File(
                        "src\\main\\resources\\net\\orekyuu\\javatter\\core\\pict\\pictDelete.png")
                        .toURI().toString());
        if (LocalClientUser.loadClientUsers().size() > 0) {
            users.addAll(LocalClientUser.loadClientUsers());
            Platform.runLater(() -> {
                try {
                    for (int i = 0; i < users.size(); i++) {
                        myProfileImage.add(new Image(users.get(i).getTwitter()
                                .verifyCredentials().getProfileImageURL()));
                    }
                    clientUserImage.setImage(myProfileImage.get(nowUserIndex));
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            });
        }

        hoverImageViews.add(hoverImageView1);
        hoverImageViews.add(hoverImageView2);
        hoverImageViews.add(hoverImageView3);
        hoverImageViews.add(hoverImageView4);
        appendedImages.add(tweetImage1);
        appendedImages.add(tweetImage2);
        appendedImages.add(tweetImage3);
        appendedImages.add(tweetImage4);

        tweetTextArea.addChangeTextListener(this);
        tweetTextArea.addOnDropListener(this);

        hoverImageViews.forEach(t -> {
            t.addOnHoverListener(this);
            t.addOnClickListener(this);
        });

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
        if (getText().length() > 0 && imageFiles.size() == 0) {
            new TweetBuilder().setClientUser(users.get(nowUserIndex))
                    .setText(getText()).tweet();
            setText("");
        } else if (getText().length() > 0 && imageFiles.size() > 0) {
            TweetBuilder tweetBuilder = new TweetBuilder();
            for (int i = 0; i < imageFiles.size(); i++) {
                tweetBuilder.addMedia(imageFiles.get(i));
            }
            tweetBuilder.setClientUser(users.get(nowUserIndex))
                    .setText(getText()).tweet();
            setText("");
            Platform.runLater(() -> {
                for (int i = 0; i < imageFiles.size(); i++) {
                    appendedImages.get(i).setImage(null);
                }
                imageFiles.clear();
            });
        }
    }
  
    // Javaビームです。
    public void javaBeam() {
        new TweetBuilder().setClientUser(users.get(nowUserIndex))
                .setText("Javabeamです").tweet();
    }

    // ユーザーアイコンのクリックによりツイートを行うユーザーを変更します。
    public void changeUser() {
        if (!(users.size() == 1 && users.size() == 0)) {
            if (users.size() - 1 > nowUserIndex) {
                nowUserIndex++;
            } else {
                nowUserIndex = 0;
            }

            clientUserImage.setImage(myProfileImage.get(nowUserIndex));
        }
    }

    @Override
    public void setText(String s) {
        tweetTextArea.setText(s);
    }

    @Override
    public String getText() {
        return tweetTextArea.getText();
    }

    // tweetTextAreaに変更があると自動で呼ばれます。
    @Override
    public void onChanged(CharSequence cs, int count) {
        remaining.setText("" + (140 - count));
    }

    // ドロップ前
    @Override
    public void onDrop(DragEvent e) {
        Dragboard dragboard = e.getDragboard();
        if (dragboard.hasFiles() && imageFiles.size() < 4) {
            e.acceptTransferModes(TransferMode.COPY);
        }
    }

    // ドロップ後
    @Override
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
                    appendedImages.get(i).setImage(
                            new Image(imageFiles.get(i).toURI().toString()));
                }
            });

            e.setDropCompleted(true);

        } else {
            e.setDropCompleted(false);
        }
    }

    @Override
    public void onHover(Event e) {
        CustomImageView view = (CustomImageView) e.getSource();
        for (int i = 0; i < imageFiles.size(); i++) {
            if (view == hoverImageViews.get(i)) {
                Platform.runLater(() -> {
                    view.setImage(hoverImage);
                });
            }
        }
    }

    @Override
    public void onHoverd(Event e) {
        CustomImageView view = (CustomImageView) e.getSource();
        for (int i = 0; i < imageFiles.size(); i++) {
            if (view == hoverImageViews.get(i)) {
                Platform.runLater(() -> {
                    view.setImage(null);
                });
            }
        }
    }

    @Override
    public void onCLicked(Event e) {
        CustomImageView view = (CustomImageView) e.getSource();
        int imageFilesSize = imageFiles.size();
        Platform.runLater(() -> {
            for (int i = 0; i < imageFilesSize; i++) {
                if (view == hoverImageViews.get(i)) {
                    if (i != imageFilesSize - 1) {
                        for (int j = i; j < imageFilesSize - 1; j++) {
                            appendedImages.get(j + 1).setImage(null);
                            appendedImages.get(j).setImage(
                                    new Image(imageFiles.get(j + 1).toURI()
                                            .toString()));
                        }
                    } else {
                        appendedImages.get(i).setImage(null);
                    }
                    imageFiles.remove(i);
                    view.setImage(null);
                }
            }
        });
    }

}
