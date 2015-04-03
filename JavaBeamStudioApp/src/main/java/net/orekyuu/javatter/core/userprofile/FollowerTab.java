package net.orekyuu.javatter.core.userprofile;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.core.models.UserModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.userprofile.UserProfileTabBase;
import net.orekyuu.javatter.core.column.UserCell;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

public class FollowerTab extends UserProfileTabBase {
    @FXML
    private ListView<net.orekyuu.javatter.api.models.User> listView;
    private ClientUser clientUser;
    private static final int FOLLOWERS_LIMIT = 30;
    private ResponseList<User> users;

    @Override
    protected void initializeBackground(net.orekyuu.javatter.api.models.User user) {
        this.clientUser = API.getInstance().getApplication().getCurrentWindow().getCurrentUserProperty().getValue();
        try {
            users = clientUser.getTwitter().getFollowersList(user.getId(), -1L, FOLLOWERS_LIMIT);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initializeUI(net.orekyuu.javatter.api.models.User user) {
        listView.setCellFactory(e -> new UserCell(clientUser));
        users.stream().map(UserModel::new).forEach(listView.getItems()::add);
        StackPane stack = (StackPane) listView.getParent();
        listView.prefWidthProperty().bind(stack.widthProperty());
        listView.prefHeightProperty().bind(stack.heightProperty());
    }

    @Override
    public String getTabName() {
        return "フォロワー";
    }
}

