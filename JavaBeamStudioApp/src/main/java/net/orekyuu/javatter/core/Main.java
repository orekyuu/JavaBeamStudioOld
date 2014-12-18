package net.orekyuu.javatter.core;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.GlobalAccess;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import net.orekyuu.javatter.core.column.ColumnManager;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;
import net.orekyuu.javatter.core.notification.NotificationManager;
import net.orekyuu.javatter.core.notification.NotificationTypeManager;

import java.io.IOException;
import java.lang.reflect.Field;
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
        setField("notificationTypeRegister", new NotificationTypeManager());
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
        Thread.setDefaultUncaughtExceptionHandler((thread,throwable)->{
            ExceptionDialogBuilder.create(new Exception(throwable));
        });
        launch(args);
    }
}
