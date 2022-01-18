import java.io.Serializable;

public class Message implements Serializable{
    private static final long serialVersionUID = 1L;

    private int id;
    private long lustTimeClientUpdate;
    private String user;
    private String message;

    public Message(int id, String user, String message) {
        this.id = id;
        this.user = user;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLustTimeClientUpdate() {
        return lustTimeClientUpdate;
    }

    public void setLustTimeClientUpdate(long lustTimeClientUpdate) {
        this.lustTimeClientUpdate = lustTimeClientUpdate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
