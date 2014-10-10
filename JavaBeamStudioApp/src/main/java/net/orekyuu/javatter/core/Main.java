package net.orekyuu.javatter.core;

import javafx.application.Application;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.GlobalAccess;

import java.io.IOException;
import java.lang.reflect.Field;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        setupGrobalAccess();
    }

    private void setupGrobalAccess() throws ReflectiveOperationException {
        Class<GlobalAccess> clazz = GlobalAccess.class;
        Field f = clazz.getDeclaredField("application");
        f.setAccessible(true);
        f.set(GlobalAccess.getInstance(), new ApplicationImpl());
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}