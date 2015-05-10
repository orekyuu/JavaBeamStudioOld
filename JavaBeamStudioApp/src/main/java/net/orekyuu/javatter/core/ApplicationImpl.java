package net.orekyuu.javatter.core;

import com.sun.javafx.css.StyleManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.Application;
import net.orekyuu.javatter.api.CurrentWindow;
import javax.inject.Inject;
import net.orekyuu.javatter.api.column.ColumnRegister;
import net.orekyuu.javatter.api.notification.NotificationSender;
import net.orekyuu.javatter.api.notification.NotificationTypeRegister;
import net.orekyuu.javatter.api.notification.NotificationTypes;
import net.orekyuu.javatter.api.twitter.ClientUserRegister;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;
import net.orekyuu.javatter.core.notification.NotificationManager;
import net.orekyuu.javatter.core.notification.NotificationTypeManager;
import net.orekyuu.javatter.core.plugin.PluginManager;
import net.orekyuu.javatter.core.twitter.LocalClientUser;

import java.io.IOException;
import java.util.Arrays;

public class ApplicationImpl implements Application {

    private Main main;
    private MainWindowPresenter mainWindowPresenter;
    private Stage primaryStage;
    @Inject
    private NotificationTypeRegister notificationTypeRegister;
    @Inject
    private ColumnRegister columnRegister;
    @Inject
    private NotificationSender notificationSender;

    public ApplicationImpl(Main main) {
        this.main = main;
        Main.getInjector().injectMembers(this);
    }

    @Override
    public void onStart(String[] args) {
        javafx.application.Application.setUserAgentStylesheet(null);
        StyleManager.getInstance().addUserAgentStylesheet(Main.class.getResource("javabeamstudio.css").toExternalForm());
    }

    @Override
    public void onLoad() {
        loadClientUsers();
        registerColumns();
        PluginManager.getInstance().load();

        Arrays.stream(NotificationTypes.values()).forEach(notificationTypeRegister::register);

        ((NotificationTypeManager) notificationTypeRegister).initialize();
    }

    @Override
    public void restart() {
        ClientUserRegister.getInstance().removeUsers(user -> true);
        try {
            main.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionDialogBuilder.create(e);
        }
    }

    @Override
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void loadClientUsers() {
        LocalClientUser.loadClientUsers().stream().forEach(ClientUserRegister.getInstance()::registerUser);

    }

    private void registerColumns() {
        columnRegister.registerColumn("タイムライン", Main.class, "userstream.fxml");
        columnRegister.registerColumn("Mentions", Main.class, "mentions.fxml");
    }

    @Override
    public void onCreate(Stage primaryStage) {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new JavatterFXMLLoader();
        try {
            Scene scene = new Scene(loader.load(getClass().getResourceAsStream("root.fxml")));
            mainWindowPresenter = loader.getController();
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PluginManager.getInstance().callOnLoad();
        primaryStage.setTitle("Javaビーム工房");
        primaryStage.centerOnScreen();
        primaryStage.show();

        try {
            ((NotificationManager) notificationSender)
                    .initializeNotificationManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CurrentWindow getCurrentWindow() {
        if (null != mainWindowPresenter) {
            return mainWindowPresenter;
        } else {
            throw new InternalError("返されるオブジェクトがCurrentWindowを実装していない。もしくは何らかの理由でnullになっています。");
        }
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

}
