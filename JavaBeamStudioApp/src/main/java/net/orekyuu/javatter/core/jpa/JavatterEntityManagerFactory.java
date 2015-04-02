package net.orekyuu.javatter.core.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class JavatterEntityManagerFactory {

    private File file;
    private static Map<String, EntityManager> managerHashMap = new HashMap<>();
    public JavatterEntityManagerFactory(File file) {
        this.file = file;
    }

    public JavatterEntityManagerFactory() {
        this(new File("javatter.db"));
    }

    public EntityManager create() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        EntityManager manager = managerHashMap.get(file.getPath());
        if (manager != null) {
            return manager;
        }

        Map<String, String> prop = new HashMap<>();
        try {
            prop.put("javax.persistence.jdbc.url", "jdbc:sqlite:" + String.valueOf(file.toURI().toURL()).substring(5));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("javatter", prop);
        EntityManager entityManager = factory.createEntityManager();
        managerHashMap.put(file.getPath(), entityManager);
        return entityManager;
    }
}
