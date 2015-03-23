package net.orekyuu.javatter.api.loader;

import javafx.fxml.FXMLLoader;
import net.orekyuu.javatter.api.application.FXApplication;
import net.orekyuu.javatter.api.inject.Injector;

public class FxLoader extends FXMLLoader {

    public FxLoader() {
        super();
        setControllerFactory(clazz -> {
            Object controller = null;
            try {
                controller = clazz.getConstructor().newInstance();
                Injector injector = FXApplication.getInjector();
                injector.inject(controller);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            return controller;
        });
    }
}
