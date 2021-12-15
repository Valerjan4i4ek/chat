import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInRoom that = (UserInRoom) o;
        return lustTimeClientUpdate == that.lustTimeClientUpdate && roomId == that.roomId && userId == that.userId && Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lustTimeClientUpdate, roomId, userId, userName);
    }
}
