import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Chat extends Remote {
    String sendMessage (String message) throws RemoteException;

    void checkAuthorization(String login, String password) throws RemoteException;

    void chooseRoom() throws RemoteException;
}
