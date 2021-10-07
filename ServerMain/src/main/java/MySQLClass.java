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

    public void addMessage(Integer room, Integer id, String message, String user){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("chat");
                ps = conn.prepareStatement("INSERT INTO room" + room + " (id, message, user) VALUES (?, ?, ?)");
                ps.setInt(1, id);
                ps.setString(2, message);
                ps.setString(3, user);
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

    public void addAuthorization(Integer id, User user){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("chat");
                ps = conn.prepareStatement("INSERT INTO authorization (id, login, password) VALUES (?, ?, ?)");
                ps.setInt(1, id);
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

    public Map<List<String>, List<String>> checkLastMessages(Integer room){
        Map<List<String>, List<String>> map = new LinkedHashMap<>();
        List<String> userList = new ArrayList<>();
        List<String> messageList = new ArrayList<>();

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
                        String user = rs.getString("user");
                        String message = rs.getString("message");


                        userList.add(user);
                        messageList.add(message);
                        map.put(userList, messageList);
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
}
