import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface Chat extends Remote {
    String sendMessage (Integer room, String message, String user) throws RemoteException;

    Map<List<String>, List<String>> checkMessage(Integer room) throws RemoteException;

    String checkAuthorization(String login, String password) throws RemoteException;

    List<String> chooseRoom() throws RemoteException;

}
