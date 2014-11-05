package net.orekyuu.javatter.core.config;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    private TextField pincode;
    @FXML
    private Button submitButton;
    private Twitter twitter;
    private RequestToken token;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("rMvLmU5qMgbZwg92Is5g", "RD28Uuu44KeMOs90UuqXAAoVTWXRTmD4H8xYKZSgBk");
        try {
            token = twitter.getOAuthRequestToken();
            Desktop.getDesktop().browse(new URL(token.getAuthorizationURL()).toURI());
        } catch (TwitterException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setThisStage(Stage stage) {
        this.stage = stage;
    }

    public void submit() {
        try {
            AccessToken accessToken = twitter.getOAuthAccessToken(token, pincode.getText());
            LocalClientUser localClientUser = new LocalClientUser(accessToken);
            localClientUser.save();
            stage.close();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
