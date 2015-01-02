package net.orekyuu.javatter.core.userprofile;

import net.orekyuu.javatter.api.userprofile.UserProfileRegister;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class UserProfileTabManager implements UserProfileRegister {

    private List<TabInfo> tabs = new LinkedList<>();

    public UserProfileTabManager() {
        registerUserProfileTab("net/orekyuu/javatter/core/profiletab.fxml");
        registerUserProfileTab("net/orekyuu/javatter/core/tweet_tab.fxml");
        registerUserProfileTab("net/orekyuu/javatter/core/favorites_tab.fxml");
        registerUserProfileTab("net/orekyuu/javatter/core/follow_tab.fxml");
        registerUserProfileTab("net/orekyuu/javatter/core/follower_tab.fxml");
    }

    @Override
    public void registerUserProfileTab(String path) {
        tabs.add(new StringTabInfo(path));
    }

    @Override
    public void registerUserProfileTab(Path path) {
        tabs.add(new FileTabInfo(path));
    }

    public List<TabInfo> getRegisteredTabs() {
        return tabs;
    }

    interface TabInfo {
        InputStream createInputStream();
    }

    private static class StringTabInfo implements TabInfo {

        String path;
        StringTabInfo(String path) {
            this.path = path;
        }

        @Override
        public InputStream createInputStream() {
            return ClassLoader.getSystemResourceAsStream(path);
        }
    }

    private static class FileTabInfo implements  TabInfo {
        Path path;
        FileTabInfo(Path path) {
            this.path = path;
        }

        @Override
        public InputStream createInputStream() {
            try {
                return Files.newInputStream(path);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
