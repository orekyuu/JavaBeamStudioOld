package net.orekyuu.javatter.core.column;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.column.ColumnController;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.UserMentionEntity;

public class MentionsController implements ColumnController {
    @FXML
    private ListView<StatusModel> mentionsList;
    private static final int INITIALIZING_LIMIT = 30;
    private static final int STATUSES_LIMIT = 100;
    private ClientUser user;

    @Override
    public void setClientUser(ClientUser clientUser) {
        user = clientUser;
        Task<List<Status>> initializing = new Task<List<Status>>() {
            @Override
            protected List<Status> call() {
                try {
                    return clientUser.getTwitter().getMentionsTimeline(
                            new Paging(1, INITIALIZING_LIMIT));
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return new ArrayList<>();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                List<Status> mentionsTimeline = getValue();
                mentionsList.setCellFactory(cell -> new TweetCell(clientUser));
                mentionsTimeline.stream()
                        .map(StatusModel.Builder::build)
                        .forEachOrdered(mentionsList.getItems()::add);
                clientUser.getStream().addOnStatus(MentionsController.this::onStatus);
            }
        };
        TaskUtil.startTask(initializing);
    }

    private void onStatus(Status status) {
        boolean match = Arrays.stream(status.getUserMentionEntities())
                .map(UserMentionEntity::getScreenName).anyMatch(name -> name.equals(user.getName()));
        if (!match) {
            return;
        }

        Platform.runLater(() -> {
            mentionsList.getItems().add(0,
                    StatusModel.Builder.build(status));
            if (mentionsList.getItems().size() > STATUSES_LIMIT)
                mentionsList.getItems().remove(STATUSES_LIMIT);
        });
    }
}
