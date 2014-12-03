package net.orekyuu.javatter.core;

import javafx.application.Application;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.GlobalAccess;
import net.orekyuu.javatter.api.notification.Notification;
import net.orekyuu.javatter.api.notification.NotificationBuilder;
import net.orekyuu.javatter.core.column.ColumnManager;
import net.orekyuu.javatter.core.notification.NotificationManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        setupGlobalAccess();
        setupApplication(stage);
    }

    private void setupGlobalAccess() throws ReflectiveOperationException {
        setField("application", new ApplicationImpl(this));
        setField("columnRegister", new ColumnManager());
        setField("notificationSender", new NotificationManager());
    }

    private void setField(String fieldName, Object value) throws ReflectiveOperationException {
        Class<GlobalAccess> clazz = GlobalAccess.class;
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(GlobalAccess.getInstance(), value);
    }

    private void setupApplication(Stage stage) throws ExecutionException, InterruptedException {
        net.orekyuu.javatter.api.Application application = GlobalAccess.getInstance().getApplication();
        application.onStart(new String[]{""});
        CompletableFuture<Void> async = CompletableFuture.runAsync(application::onLoad);
        async.get();
        application.onCreate(stage);
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}