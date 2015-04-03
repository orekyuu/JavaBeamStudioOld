package net.orekyuu.javatter.core.service;

import net.orekyuu.javatter.core.entity.Account;
import net.orekyuu.javatter.api.service.AccountService;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.jpa.JavatterEntityManagerFactory;
import net.orekyuu.javatter.core.twitter.ClientUserImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NonUniqueResultException;
import java.util.*;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {

    private final EntityManager entityManager;

    public AccountServiceImpl() {
        JavatterEntityManagerFactory factory = new JavatterEntityManagerFactory();
        entityManager = factory.create();
    }

    @Override
    public ClientUser createAccount(String name, String token, String tokenSecret) {
        Account account = new Account(name, token, tokenSecret);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(account);
        } finally {
            transaction.commit();
        }
        return new ClientUserImpl(account);
    }

    @Override
    public Optional<ClientUser> findByScreenName(String screenName) {
        List<Account> accounts = entityManager.createNamedQuery(Account.FIND_BY_SCREEN_NAME, Account.class)
                .setParameter("name", screenName).getResultList();
        if (accounts.isEmpty()) {
            return Optional.empty();
        }
        if (accounts.size() == 1) {
            return Optional.of(new ClientUserImpl(accounts.get(0)));
        }
        throw new NonUniqueResultException(screenName);
    }

    @Override
    public List<ClientUser> findAll() {
        List<Account> result = Collections.emptyList();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            result = entityManager.createNamedQuery(Account.FIND_ALL, Account.class)
                    .getResultList();
        } finally {
            transaction.commit();
        }
        return result.stream().map(ClientUserImpl::new).collect(Collectors.toList());
    }

    @Override
    public void removeAccount(ClientUser clientUser) {
        Account account = new Account(clientUser.getName(), clientUser.getAccessToken().getToken(), clientUser.getAccessToken().getTokenSecret());
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(account);
        } finally {
            transaction.commit();
        }
    }
}
