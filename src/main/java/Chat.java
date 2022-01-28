import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Chat extends Remote {

    String sendMessage (Integer room, String message, String user) throws RemoteException;

    String sendPrivateMessage (String message, String userSender, String userTaker) throws RemoteException;

    List<Message> checkMessage(Integer room) throws RemoteException;

    List<PrivateMessage> checkPrivateMessage() throws RemoteException;

    List<Message> chating(Integer room, Integer maxId) throws RemoteException;

    List<PrivateMessage> privateChating(Integer maxPrivateId) throws RemoteException;

    String checkAuthorization(String login, String password) throws RemoteException;

    List<String> chooseRoom() throws RemoteException;

    String addUserInRoom(Integer roomId, String userName) throws RemoteException;

    List<String> showUsersInRoom(Integer room) throws RemoteException;

}
