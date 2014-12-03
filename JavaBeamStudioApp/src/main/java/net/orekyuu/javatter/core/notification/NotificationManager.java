package net.orekyuu.javatter.core.notification;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.orekyuu.javatter.api.notification.Notification;
import net.orekyuu.javatter.api.notification.NotificationSender;
import net.orekyuu.javatter.core.Main;

import java.io.IOException;
import java.util.concurrent.*;

public class NotificationManager implements NotificationSender {
    private BlockingQueue<Notification> notificationQueue = new LinkedBlockingQueue<>();
    private NotificationPopupPresenter presenter;

    @Override
    public void sendNotification(Notification notification) {
        try {
            notificationQueue.put(notification);
            presenter.update();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initializeNotificationManager() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(Main.class.getResourceAsStream("notification.fxml"));
        presenter = loader.getController();
        presenter.setNotificationQueue(notificationQueue);
        Scene scene = new Scene(root);
        scene.setFill(new Color(0, 0, 0, 0));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        Rectangle2D screen = Screen.getPrimary().getBounds();
        stage.setX(0);
        stage.setY(0);
        stage.show();

        Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("JavaBeamStudio-Notification-Worker-Thread");
            return thread;
        }).execute(presenter);
    }
}
