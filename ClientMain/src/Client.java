import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.*;

public class Client {

    public static final String UNIQUE_BINDING_NAME = "server.chat";
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static User user;
    private static int maxId;

    public static void main(String[] args) throws IOException, NotBoundException, RemoteException {

        authorization();

    }

    public static void authorization() throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);

        System.out.println("Enter you login and password");
        String name = reader.readLine();
        String password = reader.readLine();
        user = new User(name, password);
        String result = chat.checkAuthorization(name, password);

        System.out.println(result);
        if(!result.equalsIgnoreCase("incorrect password")){
            chooseRoom();
        }
        else {authorization();}
    }



    public static void chooseRoom() throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);

        System.out.println("Please! Choose room:");
        List<String> list = chat.chooseRoom();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        String choosingRoom = reader.readLine();

        for (int i = 0; i < list.size(); i++) {
            if(choosingRoom.equals(list.get(i))){
                chat.addUserInRoom(i+1, user.getUserName());
                System.out.println();
                System.out.println(user.getUserName() + " join to chat");
                System.out.println();
                System.out.println("Welcome to " + (i + 1) + " room");

                checkMessage(i+1);

                System.out.println("Write your message end press Enter:");


                int finalI = i;

                new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()){
                        try{
                            Thread.sleep(10000);
                            chat.addUserInRoom(finalI, user.getUserName());
                        } catch (InterruptedException | RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()){
                        try {
                            sendMessage(finalI +1, reader.readLine(), user.getUserName());

                        } catch (IOException | NotBoundException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

                new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()){
                        try {
                            Thread.sleep(500);
                            chating(finalI+1);

                        } catch (InterruptedException | IOException | NotBoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        }
    }

    public static void sendMessage(Integer room, String message, String userInMethod) throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);
        String m = chat.sendMessage(room, message, userInMethod);
    }

    public static void checkMessage(Integer room) throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);
        List<Message> list = chat.checkMessage(room);

        int count = 0;
        maxId = list.get(list.size()-1).getId();

        for (int i = list.size()-1; i >= 0 ; i--) {
            if(count==10){
                break;
            }
            System.out.println(list.get(i).getUser() + ": " + list.get(i).getMessage());
            count++;
        }
    }

    public static void chating(Integer room) throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);

        SimpleDateFormat date = new SimpleDateFormat("HH:mm");
        List<Message> list = chat.chating(room, maxId);

        for (Message message : list) {
            String[] array = message.getMessage().split(" ");
            if (array[0].equals("/users")) {
                showUsersInRoom(room);
            } else if (array[0].equals("/send")) {
                if (array[1].equals(message.getUser())) {

                }
            } else {
                System.out.println(message.getUser() + " " + date.format(new Date()) + ": " + message.getMessage());
                maxId++;
            }
        }
    }

    public static void showUsersInRoom(Integer room) throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);
        List<String> usersList = chat.showUsersInRoom(room);
        int i = 0;
        for (String s : usersList) {
            System.out.println(s);
        }
    }

}
