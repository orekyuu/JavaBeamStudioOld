package net.orekyuu.javatter.core.twitter;

import net.orekyuu.javatter.api.twitter.ClientUser;
import twitter4j.auth.AccessToken;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

/**
 * ローカルに保存されているユーザー
 */
public class LocalClientUser implements ClientUser {

	private AccessToken token;
	private static Dao<TokenTable, Integer> dao=null;
	private static final String DATABASE_NAME = "users.db";

	public LocalClientUser(AccessToken token) {
		this.token = token;
	}

	@Override
	public AccessToken getAccessToken() {
		return token;
	}

	@Override
	public String getName() {
		return token.getScreenName();
	}

	/**
	 * ローカルのデータベースへユーザーを保存します。
	 */
	public void save() {
		ConnectionSource connectionSource = null;
		TokenTable table = new TokenTable();
		table.token = token.getToken();
		table.tokenSecret = token.getTokenSecret();
		try {
			makeFile(DATABASE_NAME);
			connectionSource = makeConnectionSource(DATABASE_NAME);
			setUpDataBase(connectionSource);
			dao.create(table);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			if(dao != null)
				dao = null;
			if (connectionSource != null) {
				try {
					connectionSource.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ローカルのデータベースからユーザーを読み込みます。
	 *
	 * @return データベースから読み込んだユーザーのリスト
	 */
	public static List<ClientUser> loadClientUsers() {
		LinkedList<ClientUser> users = new LinkedList<>();
		List<TokenTable> tokenList = new ArrayList<>();
		ConnectionSource connectionSource = null;
		try {
			connectionSource = makeConnectionSource(DATABASE_NAME);
			setUpDataBase(connectionSource);
			tokenList = getDataBase();
			for (int i = 0; i < tokenList.size(); i++) {
				users.add(new LocalClientUser(new AccessToken(tokenList.get(i).token, tokenList.get(i).tokenSecret)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(dao != null)
			dao = null;
			if (connectionSource != null) {
				try {
					connectionSource.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return users;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("LocalClientUser{");
		sb.append("token=").append(token);
		sb.append('}');
		return sb.toString();
	}

	private static void makeFile(String s) throws IOException {
		
		File file = new File(s);

		if (!file.exists()) {
			file.createNewFile();
		}

	}

	private static List<TokenTable> getDataBase() throws SQLException {
		return dao.queryForAll();
	}

	private static ConnectionSource makeConnectionSource(String source) throws SQLException {
		
		File file = new File(source);
		String absolutePath = file.getAbsolutePath();
		ConnectionSource connectionSource = 
				new JdbcPooledConnectionSource(
						"jdbc:sqlite:" + absolutePath);
		return connectionSource;
	}

	private static void setUpDataBase(ConnectionSource connctionSource) throws SQLException {
	
		if (dao == null){
			dao = DaoManager.createDao(connctionSource, TokenTable.class);
		}
		TableUtils.createTableIfNotExists(connctionSource, TokenTable.class);

	}

	@DatabaseTable(tableName = "tokenData")
	private static class TokenTable {
		
		@DatabaseField(columnName = "token", canBeNull = false)
		String token;
		@DatabaseField(columnName = "tokenSecret,", canBeNull = false)
		String tokenSecret;
	
	}

}
