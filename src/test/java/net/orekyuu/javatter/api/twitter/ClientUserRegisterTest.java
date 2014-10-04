package net.orekyuu.javatter.api.twitter;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClientUserRegisterTest {

    @Test
    //必ず1つのインスタンスを返すかのテスト
    public void testGetInstance() throws Exception {
        ClientUserRegister register1 = ClientUserRegister.getInstance();
        ClientUserRegister register2 = ClientUserRegister.getInstance();
        //シングルトンなので、必ず同じ参照でなければいけない
        assertTrue(register1 == register2);

    }

    @Test
    //正常にユーザーを登録できているか
    public void testRegisterUser(@Mocked ClientUser user1, @Mocked ClientUser user2) throws Exception {
        ClientUserRegister register = ClientUserRegister.getInstance();
        register.removeUsers(s -> true);//一度空にしておく
        register.registerUser(user1);
        assertEquals(register.registeredUserCount(), 1);
        register.registerUser(user2);
        assertEquals(register.registeredUserCount(), 2);
    }

    @Test
    //正常にユーザーリストを返せているか
    public void testRegisteredUserList(@Mocked ClientUser user1, @Mocked ClientUser user2, @Mocked ClientUser user3)
            throws Exception {
        List<ClientUser> users = new LinkedList<>();
        users.add(user1);
        users.add(user2);

        ClientUserRegister.getInstance().removeUsers(s -> true);//一度空にしておく
        users.forEach(ClientUserRegister.getInstance()::registerUser);

        List<ClientUser> clientUsers = ClientUserRegister.getInstance().registeredUserList();

        for (int i = 0; i < 2; i++) {
            assertTrue(users.get(i) == clientUsers.get(i));
        }

        //レジスタから受け取ったユーザーリストにClientUserを追加しても、レジスタに変更があってはならない
        //防御的コピーを行っているかの検査
        clientUsers.add(user3);
        assertEquals(ClientUserRegister.getInstance().registeredUserCount(), 2);
    }

    @Test
    //正常に削除が行えているか
    public void testRemoveUsers() throws Exception {
        ClientUser user1 = new MockUp<ClientUser>() {
            @Mock
            public String getName() {
                return "orekyuu";
            }
        }.getMockInstance();

        ClientUser user2 = new MockUp<ClientUser>() {
            @Mock
            public String getName() {
                return "orehachi";
            }
        }.getMockInstance();

        ClientUser user3 = new MockUp<ClientUser>() {
            @Mock
            public String getName() {
                return "nanaore";
            }
        }.getMockInstance();

        ClientUserRegister register = ClientUserRegister.getInstance();

        ClientUserRegister.getInstance().removeUsers(s -> true);//一度空にしておく
        //全削除
        register.registerUser(user1);
        register.registerUser(user2);
        register.registerUser(user3);
        register.removeUsers(u -> true);
        assertEquals(register.registeredUserCount(), 0);

        //orekyuu削除
        register.registerUser(user1);
        register.registerUser(user2);
        register.registerUser(user3);
        register.removeUsers(u -> u.getName().equals("orekyuu"));
        assertEquals(register.registeredUserCount(), 2);//ユーザー数は2
        assertTrue(register.registeredUserList().stream().noneMatch(u -> u.getName().equals("orekyuu")));//orekyuuは見つからない

        register.removeUsers(u -> true);

        //oreで始まるユーザーを削除
        register.registerUser(user1);
        register.registerUser(user2);
        register.registerUser(user3);
        register.removeUsers(u -> u.getName().startsWith("ore"));
        assertEquals(register.registeredUserCount(), 1);//ユーザー数は1
        ClientUser user = register.registeredUserList().get(0);
        assertTrue(user == user3);
    }

    @Test
    //正常に抽出が行えるか
    public void testGetUsers()
        throws Exception {
        ClientUser user1 = new MockUp<ClientUser>() {
            @Mock
            public String getName() {
                return "orekyuu";
            }
        }.getMockInstance();

        ClientUser user2 = new MockUp<ClientUser>() {
            @Mock
            public String getName() {
                return "orehachi";
            }
        }.getMockInstance();

        ClientUser user3 = new MockUp<ClientUser>() {
            @Mock
            public String getName() {
                return "nanaore";
            }
        }.getMockInstance();

        ClientUserRegister.getInstance().removeUsers(s -> true);//一度空にしておく
        ClientUserRegister register = ClientUserRegister.getInstance();
        register.registerUser(user1);
        register.registerUser(user2);
        register.registerUser(user3);

        //全取得
        assertEquals(register.getUsers(u -> true).size(), 3);

        //orekyuu抽出
        List<ClientUser> users = register.getUsers(u -> u.getName().equals("orekyuu"));
        assertEquals(users.size(), 1);
        assertTrue(users.get(0) == user1);

        //oreで始まるユーザーを抽出
        List<ClientUser> ore = register.getUsers(u -> u.getName().startsWith("ore"));
        assertEquals(ore.size(), 2);

        //防御的コピーのチェック
        ore.add(user3);
        assertEquals(register.registeredUserCount(), 3);
    }
}