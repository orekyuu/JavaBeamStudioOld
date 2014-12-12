package net.orekyuu.javatter.core.column;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.JavatterColumn;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

public class MentionsController implements JavatterColumn {
    @FXML
    ListView<StatusModel> mentionsList;
    private static final int INITIALIZING_LIMIT = 30;

    @Override
    public void setClientUser(ClientUser clientUser) {
        Task<List<Status>> initializing = new Task<List<Status>>() {
            @Override
            protected List<Status> call() {
                List<Status> mtl = new ArrayList<Status>();
                try {
                    mtl = clientUser.getTwitter().getMentionsTimeline(
                            new Paging(1, INITIALIZING_LIMIT));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return mtl;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                List<Status> mentionsTimeline = getValue();
                mentionsList.setCellFactory(cell -> new TweetCell(clientUser));
                mentionsTimeline.stream()
                        .map(status -> StatusModel.Builder.build(status))
                        .forEachOrdered(mentionsList.getItems()::add);
                clientUser.getStream().addOnStatus(
                        status -> {
                            Platform.runLater(() -> {
                                mentionsList.getItems().add(0,
                                        StatusModel.Builder.build(status));
                                if (mentionsList.getItems().size() > 100)
                                    mentionsList.getItems().remove(100);
                            });
                        });
            }
        };
        TaskUtil.startTask(initializing);
    }

    private long getUserId(ClientUser clientUser) {
        long userId = 0;
        try {
            userId = clientUser.getTwitter().getId();
            return userId;
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
