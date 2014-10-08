package net.orekyuu.javatter.core;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Field;

import net.orekyuu.javatter.api.GlobalAccess;

public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		Class<GlobalAccess> clazz = GlobalAccess.class;
		Field f= clazz.getDeclaredField("application");
		f.setAccessible(true);
		f.set(GlobalAccess.getInstance(), new ApplicationImpl());		
	}

	public static void main(String[] args) throws IOException {
		launch(args);
	}
}