package net.orekyuu.javatter.core;

import com.sun.javafx.css.StyleManager;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.api.Application;
import net.orekyuu.javatter.api.CurrentWindow;
import net.orekyuu.javatter.api.application.FXApplication;
import net.orekyuu.javatter.api.column.Column;
import net.orekyuu.javatter.api.column.ColumnType;
import net.orekyuu.javatter.api.inject.Inject;
import net.orekyuu.javatter.api.inject.Injector;
import net.orekyuu.javatter.api.loader.FxLoader;
import net.orekyuu.javatter.api.notification.NotificationTypeRegister;
import net.orekyuu.javatter.api.notification.NotificationTypes;
import net.orekyuu.javatter.api.service.ColumnManager;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;
import net.orekyuu.javatter.core.notification.NotificationManager;
import net.orekyuu.javatter.core.notification.NotificationTypeManager;
import net.orekyuu.javatter.core.plugin.PluginManager;

import java.io.IOException;
import java.util.Arrays;

public class ApplicationImpl implements Application {

    public static final Column EMPTY_COLUMN = new Column("BuildIn", "empty", ColumnType.JAR, "Empty", "/column/empty.fxml");
    public static final Column HOME_TIMELINE = new Column("BuildIn", "timeline", ColumnType.JAR, "タイムライン", "/column/userstream.fxml");
    public static final Column MENTION = new Column("BuildIn", "mention", ColumnType.JAR, "メンション", "/column/mentions.fxml");
    private Main main;
    private MainWindowPresenter mainWindowPresenter;
    private Stage primaryStage;

    @Inject
    private ColumnManager columnManager;

    public ApplicationImpl(Main main) {
        this.main = main;
    }

    @Override
    public void onStart(String[] args) {
        javafx.application.Application.setUserAgentStylesheet(null);
        StyleManager.getInstance().addUserAgentStylesheet(Main.class.getResource("javabeamstudio.css").toExternalForm());
    }

    @Override
    public void onLoad() {
        FXApplication.getInjector().inject(this);

        registerColumns();
        PluginManager.getInstance().load();

        NotificationTypeRegister notificationTypeRegister = API.getInstance().getNotificationTypeRegister();
        Arrays.stream(NotificationTypes.values()).forEach(notificationTypeRegister::register);

        ((NotificationTypeManager) notificationTypeRegister).initialize();
    }

    @Override
    public void restart() {
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

    private void registerColumns() {
        columnManager.registerColumn(EMPTY_COLUMN);
        columnManager.registerColumn(HOME_TIMELINE);
        columnManager.registerColumn(MENTION);
    }

    @Override
    public void onCreate(Stage primaryStage) {
        this.primaryStage = primaryStage;
        FxLoader loader = new FxLoader();
        try {
            Scene scene = new Scene(loader.load(getClass().getResourceAsStream("root.fxml")));
            MainWindowPresenter presenter = loader.getController();
            mainWindowPresenter = presenter;
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PluginManager.getInstance().callOnLoad();
        primaryStage.setTitle("Javaビーム工房");
        primaryStage.centerOnScreen();
        primaryStage.show();

        try {
            ((NotificationManager) API.getInstance().getNotificationSender())
                    .initializeNotificationManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CurrentWindow getCurrentWindow() {
        if (null != mainWindowPresenter
                && mainWindowPresenter instanceof CurrentWindow) {
            return (CurrentWindow) mainWindowPresenter;
        } else {
            throw new InternalError("返されるオブジェクトがCurrentWindowを実装していない。もしくは何らかの理由でnullになっています。");
        }
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

}
