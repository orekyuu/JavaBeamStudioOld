package net.orekyuu.javatter.core.service;

import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.entity.Account;
import net.orekyuu.javatter.core.entity.UserEntity;
import net.orekyuu.javatter.core.models.UserModel;
import net.orekyuu.javatter.api.service.UserService;
import net.orekyuu.javatter.core.jpa.JavatterEntityManagerFactory;
import twitter4j.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private EntityManager em;

    public UserServiceImpl() {
        JavatterEntityManagerFactory factory = new JavatterEntityManagerFactory();
        em = factory.create();
    }

    @Override
    public net.orekyuu.javatter.api.models.User createIfNotExist(User user, ClientUser account) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        net.orekyuu.javatter.api.models.User result = null;
        try {
            Optional<net.orekyuu.javatter.api.models.User> id = findByID(user.getId(), account);
            if (id.isPresent()) {
                result = id.get();
            } else {
                UserEntity entity = new UserEntity(account, user);
                em.persist(entity);
                result = new UserModel(entity);
            }
        } finally {
            transaction.commit();
        }

        return result;
    }

    @Override
    public net.orekyuu.javatter.api.models.User update(net.orekyuu.javatter.api.models.User user, ClientUser account) {
        return null;
    }

    @Override
    public Optional<net.orekyuu.javatter.api.models.User> findByScreenName(String screenName, ClientUser account) {

        return null;
    }

    @Override
    public Optional<net.orekyuu.javatter.api.models.User> findByID(long id, ClientUser account) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Optional<net.orekyuu.javatter.api.models.User> findUser = Optional.empty();
        try {
            List<UserEntity> resultList = em.createNamedQuery(UserEntity.FIND_BY_ID_AND_ACCOUNT, UserEntity.class)
                    .setParameter("id", id)
                    .setParameter("account", account)
                    .getResultList();
            if (resultList.size() == 1) {
                findUser = Optional.of(new UserModel(resultList.get(0)));
            }
        } finally {
            transaction.commit();
        }
        return findUser;
    }
}
