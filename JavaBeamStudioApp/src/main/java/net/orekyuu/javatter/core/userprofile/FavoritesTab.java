package net.orekyuu.javatter.core.userprofile;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import net.orekyuu.javatter.api.Application;
import javax.inject.Inject;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.core.model.StatusModelImpl;
import net.orekyuu.javatter.api.models.UserModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.userprofile.UserProfileTabBase;
import net.orekyuu.javatter.core.column.TweetCell;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

public class FavoritesTab extends UserProfileTabBase {
    @FXML
    private ListView<StatusModel> listView;
    private ClientUser clientUser;

    private int page = 1;
    private static final int REQUEST_COUNT = 40;

    @Inject
    private Application application;

    @Override
    protected void initializeBackground(UserModel user) {
        this.clientUser = application.getCurrentWindow().getCurrentUserProperty().getValue();
        try {
            ResponseList<Status> favorites = clientUser.getTwitter().getFavorites(user.getId(), new Paging(page, REQUEST_COUNT));
            favorites.stream().map(StatusModelImpl.Builder::build).forEach(listView.getItems()::add);
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
        return "お気に入り";
    }
}
