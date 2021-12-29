public class PrivateMessage {
    private int id;
    private long lustTimeClientUpdate;
    private String message;
    private String userSender;
    private String userTaker;

    public PrivateMessage(int id, String message, String userSender, String userTaker) {
        this.id = id;
        this.message = message;
        this.userSender = userSender;
        this.userTaker = userTaker;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserSender() {
        return userSender;
    }

    public void setUserSender(String userSender) {
        this.userSender = userSender;
    }

    public String getUserTaker() {
        return userTaker;
    }

    public void setUserTaker(String userTaker) {
        this.userTaker = userTaker;
    }
}
