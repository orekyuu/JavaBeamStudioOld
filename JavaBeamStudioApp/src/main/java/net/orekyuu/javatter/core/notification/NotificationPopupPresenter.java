package net.orekyuu.javatter.core.notification;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.orekyuu.javatter.api.notification.Notification;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class NotificationPopupPresenter implements Initializable, Runnable {
    @FXML
    private VBox root;
    @FXML
    private Text title;
    @FXML
    private ImageView subTitleImage;
    @FXML
    private Text subTitle;
    @FXML
    private Text message;
    @FXML
    private Text notificationSize;
    private BlockingQueue<Notification> notificationQueue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setNotificationQueue(BlockingQueue<Notification> notificationQueue) {
        this.notificationQueue = notificationQueue;
    }

    @FXML
    private void clear() {
        notificationQueue.clear();
        root.setVisible(false);
    }

    //==================以下Notification用のスレッドで動作==================
    @Override
    public void run() {
        while (true) {
            try {
                Platform.runLater(() -> root.setVisible(false));
                Notification notification = notificationQueue.take();
                Platform.runLater(() -> root.setVisible(true));
                changeNotification(notification);
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void changeNotification(Notification notification) {
        Platform.runLater(() -> {
            title.setText(notification.getTitle().get());
            subTitle.setText(notification.getSubTitle().orElse(""));
            message.setText(notification.getMessage().orElse(""));
            subTitleImage.setImage(notification.getSubTitleImage().orElse(null));
            notificationSize.setText(String.valueOf(notificationQueue.size()));
        });
    }

    public void update() {
        Platform.runLater(() -> notificationSize.setText(String.valueOf(notificationQueue.size())));
    }
}
