package com.example;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.api.CurrentWindow;
import net.orekyuu.javatter.api.plugin.OnLoad;
import net.orekyuu.javatter.api.plugin.OnPreLoad;

public class Main {

    @OnPreLoad
    private void onPreLoad() {
        System.out.println("pre load");
    }

    @OnLoad
    private void onLoad() {
        System.out.println("load");
        CurrentWindow currentWindow = API.getInstance().getApplication().getCurrentWindow();
        StringProperty textProperty = currentWindow.getTweetTextProperty();
        textProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (textProperty.getValue().equals("/hoge")) {
                    textProperty.setValue("ほげらりおん");
                }
            }
        });
    }
}
