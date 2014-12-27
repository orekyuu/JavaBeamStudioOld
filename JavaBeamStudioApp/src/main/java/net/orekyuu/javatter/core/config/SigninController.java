package net.orekyuu.javatter.core.config;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
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
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    twitter = new TwitterFactory().getInstance();
                    twitter.setOAuthConsumer("rMvLmU5qMgbZwg92Is5g", "RD28Uuu44KeMOs90UuqXAAoVTWXRTmD4H8xYKZSgBk");
                    token = twitter.getOAuthRequestToken();
                    Desktop.getDesktop().browse(new URL(token.getAuthorizationURL()).toURI());
                } catch (TwitterException | URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void succeeded() {
                indicatorPane.setVisible(false);
            }
        };
        TaskUtil.startTask(task);

    }

    public void setThisStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void submit() {
        indicatorPane.setVisible(true);
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    AccessToken accessToken = twitter.getOAuthAccessToken(token, pincode.getText());
                    LocalClientUser localClientUser = new LocalClientUser(accessToken);
                    localClientUser.save();
                    return true;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void succeeded() {
                if (getValue()) {
                    stage.close();
                    indicatorPane.setVisible(true);
                } else {
                    indicatorPane.setVisible(false);
                    error.setText("認証に失敗しました");
                    error.setVisible(true);
                }
            }
        };

        TaskUtil.startTask(task);
    }
}
