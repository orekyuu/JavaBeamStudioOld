package net.orekyuu.javatter.core.userprofile;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.api.models.User;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.userprofile.UserProfileTabBase;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import twitter4j.Relationship;
import twitter4j.TwitterException;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class UserProfileInfoPresenter extends UserProfileTabBase {
    @FXML
    private Text tweet;
    @FXML
    private Text follow;
    @FXML
    private Text follower;
    @FXML
    private Text list;
    @FXML
    private Text favorite;
    @FXML
    private Text isFriends;
    @FXML
    private ToggleButton followButton;
    @FXML
    private Text location;
    @FXML
    private Hyperlink webSite;
    @FXML
    private Text description;
    @FXML
    private VBox rightPane;

    private Relationship friendship;
    private User user;

    @Override
    protected void initializeBackground(User user) {
        this.user = user;
        ClientUser currentUser = API.getInstance().getApplication().getCurrentWindow().getCurrentUserProperty().getValue();
        try {
            friendship = currentUser.getTwitter().showFriendship(currentUser.getTwitter().getId(), user.getId());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initializeUI(User user) {

        tweet.setText(String.valueOf(user.getTweetCount()));
        follow.setText(String.valueOf(user.getFriendsCount()));
        follower.setText(String.valueOf(user.getFollowersCount()));
        list.setText(String.valueOf(user.getListedCount()));
        favorite.setText(String.valueOf(user.getFavCount()));

        if (friendship.isSourceFollowedByTarget() && friendship.isSourceFollowingTarget()) {
            isFriends.setText("相互フォロー");
            followButton.setSelected(true);
        } else if (friendship.isSourceFollowedByTarget()) {
            isFriends.setText("フォローされています");
            followButton.setSelected(false);
        } else if (friendship.isSourceFollowingTarget()) {
            isFriends.setText("フォローしています");
            followButton.setSelected(true);
        } else {
            isFriends.setText("無関心");
            followButton.setSelected(false);
        }
        followButton.textProperty().bind(Bindings.when(followButton.selectedProperty()).then("フォローを外す").otherwise("フォローする"));

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

    public void onToggleFollow() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                ClientUser clientUser = API.getInstance().getApplication().getCurrentWindow().getCurrentUserProperty().getValue();
                if (followButton.isSelected()) {
                    try {
                        clientUser.getTwitter().createFriendship(user.getId());
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    return false;
                } else {
                    try {
                        clientUser.getTwitter().destroyFriendship(user.getId());
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        };
        followButton.disableProperty().bind(task.runningProperty());
        TaskUtil.startTask(task);
    }
}
