package net.orekyuu.javatter.core.column;

import java.util.List;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.JavatterColumn;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import twitter4j.Status;
import twitter4j.TwitterException;

public class MentionsController implements JavatterColumn {
    @FXML
    ListView<StatusModel> mentionsList;
    private final int INITIALIZING_LIMIT = 30;

    @Override
    public void setClientUser(ClientUser clientUser) {
        long userId = getUserId(clientUser);
        Task initializing = new Task() {
            @Override
            protected Object call() {
                List<Status> mentionsTimeline = null;
                try {
                    mentionsTimeline = clientUser.getTwitter().getMentionsTimeline();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mentionsTimeline
                        .stream()
                        .limit(INITIALIZING_LIMIT)
                        .forEachOrdered(
                                status -> {
                                    mentionsList.getItems().add(
                                            StatusModel.Builder.build(status));
                                });
                return null;
            }
            @Override
            protected void succeeded() {
                super.succeeded();
                mentionsList.setCellFactory(cell -> new TweetCell(clientUser));
                clientUser.getStream().addOnStatus(status ->{
                    if (status.getInReplyToUserId() == userId) {
                        Task statusAdding = new Task(){
                            @Override
                            protected Object call(){
                                mentionsList.getItems().add(0,
                                        StatusModel.Builder.build(status));
                                if (mentionsList.getItems().size() > 100)
                                mentionsList.getItems().remove(100);
                                return null;
                            }
                        };
                        TaskUtil.startTask(statusAdding);
                    }
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
