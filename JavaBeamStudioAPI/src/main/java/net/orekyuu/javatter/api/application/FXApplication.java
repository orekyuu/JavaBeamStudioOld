package net.orekyuu.javatter.api.application;

import javafx.application.Application;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.inject.InjectProperty;
import net.orekyuu.javatter.api.inject.Injector;

/**
 * JavaFXのアプリケーション
 */
public abstract class FXApplication extends Application {
    private static final InjectProperty injectProperty = new InjectProperty();
    private static final Injector INJECTOR = new Injector(injectProperty);

    @Override
    public void start(Stage primaryStage) {
        setup(injectProperty);
        onStart(primaryStage);
    }

    public abstract void setup(InjectProperty property);

    public abstract void onStart(Stage primaryStage);

    public static Injector getInjector() {
        return INJECTOR;
    }
}
