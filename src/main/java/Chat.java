import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Chat extends Remote {
    String sendMessage (String message) throws RemoteException;

    String checkAuthorization(String login, String password) throws RemoteException;

    List<String> chooseRoom() throws RemoteException;
}
