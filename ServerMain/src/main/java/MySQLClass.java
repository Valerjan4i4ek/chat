import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MySQLClass {

    public MySQLClass(){
        baseCreate();
        tableMessageCreate();
        tableAuthorizationCreate();
        rooms();
        tableUserInRoom();
        tablePrivateMessages();
    }

    public Connection getConnection(String dbName) throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String url = "jdbc:mysql://localhost/" + ((dbName != null)? (dbName) : (""));
        String username = "root";
        String password = "1234";
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

        return DriverManager.getConnection(url, username, password);
    }

    public void baseCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection(null);
                st = conn.createStatement();
                st.executeUpdate("CREATE DATABASE IF NOT EXISTS chat");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void rooms(){
        for (int i = 1; i <= 4; i++) {
            createRooms(i);
        }
    }

    public void createRooms(int i){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection("chat");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS chat.room" + i + "(id INT NOT NULL, message VARCHAR(100) NOT NULL, user VARCHAR(100) NOT NULL)");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tableMessageCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection("chat");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS chat.message " +
                        "(id INT NOT NULL, room INT NOT NULL, message VARCHAR(100) NOT NULL)");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tableUserInRoom(){
        try{
            Connection conn = null;
            Statement st = null;
            try{
                conn = getConnection("chat");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS chat.userInRoom " +
                        "(lustTimeClientUpdate BIGINT NOT NULL, roomId INT NOT NULL, " +
                        "userId INT NOT NULL, userName VARCHAR(20) NOT NULL)");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tablePrivateMessages(){
        try{
            Connection conn = null;
            Statement st = null;
            try{
                conn = getConnection("chat");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS chat.privateMessages " +
                        "(lustTimeClientUpdate BIGINT NOT NULL, message VARCHAR(20) NOT NULL, " +
                        "userSender VARCHAR(20) NOT NULL, userTaker VARCHAR(20) NOT NULL)");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tableAuthorizationCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection("chat");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS chat.authorization " +
                        "(id INT NOT NULL, login VARCHAR(20) NOT NULL, password VARCHAR(20) NOT NULL)");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addUserInRoom(UserInRoom userInRoom){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("chat");
                ps = conn.prepareStatement("INSERT INTO userInRoom (lustTimeClientUpdate, roomId, userId, userName) VALUES (?, ?, ?, ?)");
                ps.setLong(1, userInRoom.getLustTimeClientUpdate());
                ps.setInt(2, userInRoom.getRoomId());
                ps.setInt(3, userInRoom.getUserId());
                ps.setString(4, userInRoom.getUserName());
                ps.executeUpdate();
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addPrivateMessage(String message, String userSender, String userTaker){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("chat");
                ps = conn.prepareStatement("INSERT INTO privateMessages (lustTimeClientUpdate, message, userSender, userTaker) VALUES (?, ?, ?, ?)");
                ps.setLong(1, System.currentTimeMillis());
                ps.setString(2, message);
                ps.setString(3, userSender);
                ps.setString(4, userTaker);

                ps.executeUpdate();
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Message addMessage(Integer room, Integer id, String message, String user){
        Message m = null;
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("chat");
                ps = conn.prepareStatement("INSERT INTO room" + room + " (id, message, user) VALUES (?, ?, ?)");
                ps.setInt(1, id);
                ps.setString(2, message);
                ps.setString(3, user);
                m = new Message(id, message, user);

                ps.executeUpdate();
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return m;
    }

    public void addAuthorization(User user){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("chat");
                ps = conn.prepareStatement("INSERT INTO authorization (id, login, password) VALUES (?, ?, ?)");
                ps.setInt(1, user.getId());
                ps.setString(2, user.getUserName());
                ps.setString(3, user.getUserPassword());
                ps.executeUpdate();
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteUserInRoom(int userId){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("chat");
                ps = conn.prepareStatement("DELETE FROM userInRoom WHERE userId = ?");
                ps.setInt(1, userId);
                ps.executeUpdate();
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<User> checkAuthorization (){
        List<User> list = new LinkedList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("chat");
                String query = "SELECT * FROM authorization";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    try{
                        int id = rs.getInt("id");
                        String login = rs.getString("login");
                        String password = rs.getString("password");
                        User user = new User(id, login, password);
                        list.add(user);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public Map<String, String> check (){
        Map<String, String> map = new HashMap<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("chat");
                String query = "SELECT * FROM authorization";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    try{
                        String login = rs.getString("login");
                        String password = rs.getString("password");

                        map.put(login, password);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

    public Map<List<Integer>, Map<List<Integer>, String>> checkUsersList(){
        Map<List<Integer>, Map<List<Integer>, String>> returnMap = new HashMap<>();
        Map<List<Integer>, String> map = new HashMap<>();
        List<Integer> roomIdList = new ArrayList<>();
        List<Integer> userIdList = new ArrayList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("chat");
                String query = "SELECT * FROM userInRoom";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    try{
                        int roomId = rs.getInt("roomId");
                        int userId = rs.getInt("userId");
                        String userName = rs.getString("userName");

                        roomIdList.add(roomId);
                        userIdList.add(userId);
                        map.put(userIdList, userName);
                        returnMap.put(roomIdList, map);

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return returnMap;
    }

    public List<UserInRoom> checkUserInRoom(){
        List<UserInRoom> list = new LinkedList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("chat");
                String query = "SELECT * FROM userInRoom";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    try{
                        long lustTimeClientUpdate = rs.getLong("lustTimeClientUpdate");
                        int roomId = rs.getInt("roomId");
                        int userId = rs.getInt("userId");
                        String userName = rs.getString("userName");
                        UserInRoom u = new UserInRoom(lustTimeClientUpdate, roomId, userId, userName);
                        list.add(u);

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public List<Message> checkMessage (Integer room){
        List<Message> list = new LinkedList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("chat");
                String query = "SELECT * FROM room" + room;
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    try{
                        int id = rs.getInt("id");
                        String user = rs.getString("user");
                        String message = rs.getString("message");
                        Message m = new Message(id, user, message);
                        list.add(m);


                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;

    }
}
