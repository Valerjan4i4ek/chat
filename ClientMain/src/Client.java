import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Client {

    public static final String UNIQUE_BINDING_NAME = "server.chat";
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException, NotBoundException, RemoteException {

        authorization();

    }

    public static void authorization() throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);

        System.out.println("Enter you login and password");
        String result = chat.checkAuthorization(reader.readLine(), reader.readLine());
        System.out.println(result);
        if(!result.equals("incorrect password")){
            chooseRoom();
        }
    }

    public static void chooseRoom() throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);

        System.out.println("Please! Choose oom:");
        List<String> list = chat.chooseRoom();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        for (int i = 0; i < list.size(); i++) {
            if(reader.readLine().equals(list.get(i))){
                System.out.println("Welcome to " + (i + 1) + " room");
            }
        }
    }

    public static void writeMessage() throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);

        System.out.println("Write your message end press Enter:");
        String message = chat.sendMessage(reader.readLine());

        System.out.println(message);
    }
}
