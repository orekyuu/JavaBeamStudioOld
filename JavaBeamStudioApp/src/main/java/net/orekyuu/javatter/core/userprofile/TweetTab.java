package net.orekyuu.javatter.core.userprofile;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import net.orekyuu.javatter.api.GlobalAccess;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.models.UserModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.userprofile.UserProfileTabBase;
import net.orekyuu.javatter.core.column.TweetCell;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

public class TweetTab extends UserProfileTabBase {
    @FXML
    private ListView<StatusModel> listView;
    private ClientUser clientUser;

    private int page = 1;
    private static final int REQUEST_COUNT = 40;

    @Override
    protected void initializeBackground(UserModel user) {
        this.clientUser = GlobalAccess.getInstance().getApplication().getCurrentWindow().getCurrentUserProperty().getValue();
        try {
            ResponseList<Status> statuses = clientUser.getTwitter().getUserTimeline(user.getId(), new Paging(page, REQUEST_COUNT));
            statuses.stream().map(StatusModel.Builder::build).forEach(listView.getItems()::add);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        page++;
    }

    @Override
    protected void initializeUI(UserModel user) {
        listView.setCellFactory(e -> new TweetCell(clientUser));
        StackPane stack = (StackPane) listView.getParent();
        listView.prefWidthProperty().bind(stack.widthProperty());
        listView.prefHeightProperty().bind(stack.heightProperty());
    }

    @Override
    public String getTabName() {
        return "ツイート";
    }
}
