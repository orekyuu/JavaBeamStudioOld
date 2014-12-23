package net.orekyuu.javatter.core.userprofile;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.api.models.UserModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.userprofile.UserProfileTabBase;
import twitter4j.Relationship;
import twitter4j.TwitterException;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class UserProfileInfoPresenter extends UserProfileTabBase {
    public Text tweet;
    public Text follow;
    public Text follower;
    public Text list;
    public Text favorite;
    public Text isFriends;
    public Button followButton;
    public Text location;
    public Hyperlink webSite;
    public Text description;
    public VBox rightPane;

    private Relationship friendship;
    private UserModel user;
    @Override
    protected void initializeBackground(UserModel user) {
        this.user = user;
        ClientUser currentUser = API.getInstance().getApplication().getCurrentWindow().getCurrentUserProperty().getValue();
        try {
            friendship = currentUser.getTwitter().showFriendship(currentUser.getTwitter().getId(), user.getId());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initializeUI(UserModel user) {

        tweet.setText(String.valueOf(user.getTweetCount()));
        follow.setText(String.valueOf(user.getFriendsCount()));
        follower.setText(String.valueOf(user.getFollowersCount()));
        list.setText(String.valueOf(user.getListedCount()));
        favorite.setText(String.valueOf(user.getFavCount()));

        if (friendship.isSourceFollowedByTarget() && friendship.isSourceFollowingTarget()) {
            isFriends.setText("相互フォロー");
        } else if (friendship.isSourceFollowedByTarget()) {
            isFriends.setText("フォローされています");
        } else if (friendship.isSourceFollowingTarget()) {
            isFriends.setText("フォローしています");
        } else {
            isFriends.setText("無関心");
        }

        location.wrappingWidthProperty().bind(rightPane.widthProperty());
        description.wrappingWidthProperty().bind(rightPane.widthProperty());
        location.setText(user.getLocation() == null ? "未設定" : user.getLocation());
        description.setText(user.getDescription() == null ? "未設定" : user.getDescription());
        webSite.setText(user.getWebSite());
    }

    @Override
    public String getTabName() {
        return "プロファイル";
    }

    @FXML
    private void openBrowser() {
        try {
            Desktop.getDesktop().browse(new URL("https://twitter.com/" + user.getScreenName()).toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openWebSite() {
        try {
            Desktop.getDesktop().browse(new URL(webSite.getText()).toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
