public class UserInRoom {
    private long lustTimeClientUpdate;
    private int roomId;
    private int userId;
    private String userName;

    public UserInRoom(long lustTimeClientUpdate, int roomId, int userId, String userName) {
        this.lustTimeClientUpdate = lustTimeClientUpdate;
        this.roomId = roomId;
        this.userId = userId;
        this.userName = userName;
    }

    public long getLustTimeClientUpdate() {
        return lustTimeClientUpdate;
    }

    public void setLustTimeClientUpdate(long lustTimeClientUpdate) {
        this.lustTimeClientUpdate = lustTimeClientUpdate;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
