import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQLClass {

    public MySQLClass(){
        baseCreate();
        tableMessageCreate();
        tableAuthorizationCreate();
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

    public void tableMessageCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection("chat");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS chat.message " +
                        "(id INT NOT NULL, message VARCHAR(100) NOT NULL)");
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

    public void addMessage(Integer id, String message){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("chat");
                ps = conn.prepareStatement("INSERT INTO message (id, message) VALUES (?, ?)");
                ps.setInt(1, id);
                ps.setString(2, message);
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
}
