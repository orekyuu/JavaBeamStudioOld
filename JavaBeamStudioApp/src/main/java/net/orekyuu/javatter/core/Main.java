package net.orekyuu.javatter.core;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import javax.inject.Inject;
import javax.inject.Singleton;

import net.orekyuu.javatter.api.column.ColumnRegister;
import net.orekyuu.javatter.api.notification.NotificationSender;
import net.orekyuu.javatter.api.notification.NotificationTypeRegister;
import net.orekyuu.javatter.api.userprofile.UserProfileRegister;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import net.orekyuu.javatter.core.column.ColumnManager;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;
import net.orekyuu.javatter.core.notification.NotificationManager;
import net.orekyuu.javatter.core.notification.NotificationTypeManager;
import net.orekyuu.javatter.core.userprofile.UserProfileTabManager;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main extends Application {

    @Inject
    private net.orekyuu.javatter.api.Application application;
    private static Injector injector;

    @Override
    public void start(Stage stage) throws Exception {
        initializeInjector();
        Main.injector.injectMembers(this);
        setupApplication(stage);
    }

    private void setupApplication(Stage stage) throws ExecutionException, InterruptedException {
        net.orekyuu.javatter.api.Application app = this.application;
        app.onStart(new String[]{""});
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                app.onLoad();
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                app.onCreate(stage);
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

    public static Injector getInjector() {
        return injector;
    }

    private void initializeInjector() {
        injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(ColumnRegister.class).to(ColumnManager.class);
                bind(NotificationSender.class).to(NotificationManager.class).in(Singleton.class);
                bind(NotificationTypeRegister.class).to(NotificationTypeManager.class).in(Singleton.class);
                bind(UserProfileRegister.class).to(UserProfileTabManager.class).in(Singleton.class);
            }

            @Provides
            @Singleton
            private net.orekyuu.javatter.api.Application getApplication() {
                return new ApplicationImpl(Main.this);
            }
        });
    }
}
