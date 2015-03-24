package net.orekyuu.javatter.core.service;

import net.orekyuu.javatter.api.entity.Account;
import net.orekyuu.javatter.api.service.AccountService;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.persistence.JavatterEntityManagerFactory;
import net.orekyuu.javatter.core.twitter.ClientUserImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.*;

public class AccountServiceImpl implements AccountService {

    private final EntityManager entityManager;
    private static Map<String, ClientUser> clientUserMap = new HashMap<>();

    public AccountServiceImpl() {
        JavatterEntityManagerFactory factory = new JavatterEntityManagerFactory();
        entityManager = factory.create();
    }

    @Override
    public Account createAccount(String name, String token, String tokenSecret) {
        Account account = new Account(name, token, tokenSecret);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(account);
        } finally {
            transaction.commit();
        }
        return account;
    }

    @Override
    public Optional<Account> findByScreenName(String screenName) {
        EntityTransaction transaction = entityManager.getTransaction();
        Optional<Account> result = Optional.empty();
        try {
            Account account = entityManager.createNamedQuery(Account.FIND_BY_SCREEN_NAME, Account.class)
                    .setParameter("name", screenName).getSingleResult();
            result = Optional.ofNullable(account);
        } finally {
        }
        return result;
    }

    @Override
    public List<Account> findAll() {
        List<Account> result = Collections.emptyList();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            result = entityManager.createNamedQuery(Account.FIND_ALL, Account.class)
                    .getResultList();
        } finally {
            transaction.commit();
        }
        return result;
    }

    @Override
    public void removeAccount(Account account) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(account);
        } finally {
            transaction.commit();
        }
    }

    @Override
    public ClientUser getClientUser(Account account) {
        ClientUser user = clientUserMap.get(account.getScreenName());
        if (user == null) {
            user = new ClientUserImpl(account);
            clientUserMap.put(account.getScreenName(), user);
        }
        return user;
    }
}
