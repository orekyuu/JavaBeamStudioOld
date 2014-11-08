package net.orekyuu.javatter.core.config;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.control.animator.FadeAnimator;
import net.orekyuu.javatter.core.Main;
import net.orekyuu.javatter.core.twitter.LocalClientUser;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class SigninController implements Initializable {
    @FXML
    private Label error;
    @FXML
    private StackPane indicatorPane;
    @FXML
    private TextField pincode;
    @FXML
    private Button submitButton;
    private Twitter twitter;
    private RequestToken token;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indicatorPane.setVisible(true);
        CompletableFuture.runAsync(() -> {
            try {
                twitter = new TwitterFactory().getInstance();
                twitter.setOAuthConsumer("rMvLmU5qMgbZwg92Is5g", "RD28Uuu44KeMOs90UuqXAAoVTWXRTmD4H8xYKZSgBk");
                token = twitter.getOAuthRequestToken();
                Desktop.getDesktop().browse(new URL(token.getAuthorizationURL()).toURI());
            } catch (TwitterException | URISyntaxException | IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    error.setText("認証に失敗しました");
                    error.setVisible(true);
                    indicatorPane.setVisible(false);
                });
            }
            Platform.runLater(() -> indicatorPane.setVisible(false));
        });

    }

    public void setThisStage(Stage stage) {
        this.stage = stage;
    }

    public void submit() {
        indicatorPane.setVisible(true);
        CompletableFuture.runAsync(() -> {
            try {
                Twitter twitter = TwitterFactory.getSingleton();
                AccessToken accessToken = twitter.getOAuthAccessToken(token, pincode.getText());
                LocalClientUser localClientUser = new LocalClientUser(accessToken);
                localClientUser.save();
                Platform.runLater(() -> {
                    stage.close();
                    indicatorPane.setVisible(true);
                });
            } catch (TwitterException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    indicatorPane.setVisible(false);
                    error.setText("認証に失敗しました");
                    error.setVisible(true);
                    System.out.println("failed");
                });
            }
        });
    }
}
