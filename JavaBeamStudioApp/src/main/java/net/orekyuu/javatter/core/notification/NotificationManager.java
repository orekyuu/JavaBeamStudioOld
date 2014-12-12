package net.orekyuu.javatter.core.notification;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.orekyuu.javatter.api.GlobalAccess;
import net.orekyuu.javatter.api.notification.Notification;
import net.orekyuu.javatter.api.notification.NotificationBuilder;
import net.orekyuu.javatter.api.notification.NotificationSender;
import net.orekyuu.javatter.api.notification.NotificationTypes;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.twitter.ClientUserRegister;
import net.orekyuu.javatter.core.Main;
import net.orekyuu.javatter.core.cache.IconCache;
import net.orekyuu.javatter.core.models.StatusModel;
import net.orekyuu.javatter.core.models.UserModel;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

public class NotificationManager implements NotificationSender {
    private BlockingQueue<Notification> notificationQueue = new LinkedBlockingQueue<>();
    private NotificationPopupPresenter presenter;

    @Override
    public void sendNotification(Notification notification) {
        NotificationTypeManager manager = (NotificationTypeManager) GlobalAccess.getInstance().getNotificationTypeRegister();
        if (!manager.isNotice(notification.getType()))
            return;
        try {
            notificationQueue.put(notification);
            presenter.update();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initializeNotificationManager() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        VBox root = loader.load(Main.class.getResourceAsStream("notification.fxml"));
        presenter = loader.getController();
        presenter.setNotificationQueue(notificationQueue);
        Scene scene = new Scene(root);
        scene.setFill(new Color(0, 0, 0, 0));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        stage.setX(screen.getWidth() - root.getPrefWidth());
        stage.setY(screen.getHeight() - root.getPrefHeight());
        stage.initOwner(GlobalAccess.getInstance().getApplication().getPrimaryStage());
        stage.show();

        setupNotification();

        Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("JavaBeamStudio-Notification-Worker-Thread");
            return thread;
        }).execute(presenter);
    }

    private void setupNotification() {
        NotificationSender sender = GlobalAccess.getInstance().getNotificationSender();
        List<ClientUser> users = ClientUserRegister.getInstance().getUsers(s -> true);
        users.stream().map(ClientUser::getStream).forEach(stream -> {
            stream.addOnFavorite((user1, user2, status) -> {
                StatusModel model = StatusModel.Builder.build(status);
                UserModel userModel = UserModel.Builder.build(user1);
                javafx.scene.image.Image image = IconCache.getImage(userModel.getProfileImageURL());
                Notification notification = new NotificationBuilder(NotificationTypes.FAVORITE)
                        .setSubTitleImage(image).setSubTitle(userModel.getName())
                        .setMessage(model.getText()).build();
                sender.sendNotification(notification);
            }).addOnFollow((user, user2) -> {
                UserModel model = UserModel.Builder.build(user);
                Image image = IconCache.getImage(model.getProfileImageURL());
                Notification notification = new NotificationBuilder(NotificationTypes.FOLLOW)
                        .setSubTitleImage(image).setSubTitle(model.getName())
                        .setMessage(model.getDescription()).build();
                sender.sendNotification(notification);
            }).addOnStatus(status->{
                if(status.isRetweet()){
                    UserModel userModel = UserModel.Builder.build(status.getRetweetedStatus().getUser());
                    if(users.stream().anyMatch(user->user.getAccessToken().getUserId() == userModel.getId())){
                        StatusModel statusModel = StatusModel.Builder.build(status);
                        Image image = IconCache.getImage(statusModel.getOwner().getProfileImageURL());
                        Notification notification = new NotificationBuilder(NotificationTypes.RETWEET)
                            .setSubTitleImage(image).setSubTitle(statusModel.getOwner().getName())
                            .setMessage(statusModel.getOwner().getName()+"さんにリツイートされました"+statusModel.getText()).build();
                        sender.sendNotification(notification);
                    }
                }
                if(users.stream().anyMatch(user -> user.getAccessToken().getUserId() == status.getInReplyToUserId())){
                    StatusModel statusModel = StatusModel.Builder.build(status);
                    Image image = IconCache.getImage(statusModel.getOwner().getProfileImageURL());
                    Notification notification = new NotificationBuilder(NotificationTypes.MENTION)
                        .setSubTitleImage(image).setSubTitle(statusModel.getOwner().getName())
                        .setMessage(statusModel.getOwner().getName()+"さんからのリプライ"+statusModel.getText()).build();
                    sender.sendNotification(notification);
                }
            });
        });
    }
}
