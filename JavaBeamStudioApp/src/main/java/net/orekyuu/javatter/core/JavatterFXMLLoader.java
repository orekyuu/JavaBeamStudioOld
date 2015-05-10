package net.orekyuu.javatter.core;

import javafx.fxml.FXMLLoader;

public class JavatterFXMLLoader extends FXMLLoader {

    public JavatterFXMLLoader() {
        setControllerFactory(param -> {
            Object obj = null;
            try {
                obj = param.getConstructor().newInstance();
                Main.getInjector().injectMembers(obj);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            return obj;
        });
    }
}
