package net.orekyuu.javatter.api.service;

import net.orekyuu.javatter.api.entity.UserEntity;
import twitter4j.User;

/**
 * Userの情報の操作を行うためのサービスです。
 */
public interface UserService {

    UserEntity create(User user);
}
