package net.orekyuu.javatter.core.entity;

import javax.persistence.*;

@Entity
@Table(name = "ACCOUNT")
@NamedQueries({
        @NamedQuery(name = net.orekyuu.javatter.core.entity.Account.FIND_BY_SCREEN_NAME, query = "SELECT a FROM Account a WHERE a.screenName = :name"),
        @NamedQuery(name = net.orekyuu.javatter.core.entity.Account.FIND_ALL, query = "SELECT a FROM Account a")
})
public class Account {
    public static final String FIND_BY_SCREEN_NAME = "Account.findByScreenName";
    public static final String FIND_ALL = "Account.findAll";

    @Id
    private String screenName;
    @Column(nullable = false)
    private String accessToken;
    @Column(nullable = false)
    private String accessTokenSecret;

    public Account(String screenName, String accessToken, String accessTokenSecret) {
        this.screenName = screenName;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
    }

    public Account() {
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        net.orekyuu.javatter.core.entity.Account account = (net.orekyuu.javatter.core.entity.Account) o;

        if (accessToken != null ? !accessToken.equals(account.accessToken) : account.accessToken != null) return false;
        if (accessTokenSecret != null ? !accessTokenSecret.equals(account.accessTokenSecret) : account.accessTokenSecret != null)
            return false;
        if (screenName != null ? !screenName.equals(account.screenName) : account.screenName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = screenName != null ? screenName.hashCode() : 0;
        result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0);
        result = 31 * result + (accessTokenSecret != null ? accessTokenSecret.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("screenName='").append(screenName).append('\'');
        sb.append(", accessToken='").append(accessToken).append('\'');
        sb.append(", accessTokenSecret='").append(accessTokenSecret).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
