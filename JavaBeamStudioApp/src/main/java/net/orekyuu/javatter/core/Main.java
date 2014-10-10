package net.orekyuu.javatter.core;

import javafx.application.Application;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.GlobalAccess;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        setupGrobalAccess();
        setupApplication(stage);
    }

    private void setupGrobalAccess() throws ReflectiveOperationException {
        Class<GlobalAccess> clazz = GlobalAccess.class;
        Field f = clazz.getDeclaredField("application");
        f.setAccessible(true);
        f.set(GlobalAccess.getInstance(), new ApplicationImpl());
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