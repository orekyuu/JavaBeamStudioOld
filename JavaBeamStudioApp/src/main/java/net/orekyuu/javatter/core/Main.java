package net.orekyuu.javatter.core;

import javafx.concurrent.Task;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.api.application.FXApplication;
import net.orekyuu.javatter.api.inject.InjectProperty;
import net.orekyuu.javatter.api.service.*;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;
import net.orekyuu.javatter.core.notification.NotificationManager;
import net.orekyuu.javatter.core.notification.NotificationTypeManager;
import net.orekyuu.javatter.core.service.*;
import net.orekyuu.javatter.core.userprofile.UserProfileTabManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;

public class Main extends FXApplication {

    @Override
    public void setup(InjectProperty property) {
        property.bind(AccountService.class).to(AccountServiceImpl.class);
        property.bind(ColumnService.class).to(ColumnServiceImpl.class);
        property.bind(ColumnManager.class).to(ColumnManagerImpl.class);
        property.bind(StatusService.class).to(StatusServiceImpl.class);
        property.bind(UserService.class).to(UserServiceImpl.class);
    }

    @Override
    public void onStart(Stage primaryStage) {
        try {
            setupGlobalAccess();
            setupApplication(primaryStage);
        } catch (ReflectiveOperationException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void setupGlobalAccess() throws ReflectiveOperationException {
        setField("application", new ApplicationImpl(this));
        setField("notificationSender", new NotificationManager());
        setField("notificationTypeRegister", new NotificationTypeManager());
        setField("userProfileRegister", new UserProfileTabManager());
    }

    private void setField(String fieldName, Object value) throws ReflectiveOperationException {
        Class<API> clazz = API.class;
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(API.getInstance(), value);
    }

    private void setupApplication(Stage stage) throws ExecutionException, InterruptedException {
        net.orekyuu.javatter.api.Application application = API.getInstance().getApplication();
        application.onStart(new String[]{""});
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                application.onLoad();
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                application.onCreate(stage);
            }
        };
        TaskUtil.startTask(task);
    }

    public static void main(String[] args) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            ExceptionDialogBuilder.create(new Exception(throwable));
        });
        launch(args);
    }
}
