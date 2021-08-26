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
    private static Rooms rooms;

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
        if(!result.equals("incorrect password")){
            chooseRoom();
        }
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
                System.out.println();
                System.out.println(user.getUserName() + " join to chat");
                System.out.println();
                System.out.println("Welcome to " + (i + 1) + " room");

                checkMessage(i+1);

                System.out.println("Write your message end press Enter:");
//                sendMessage(i+1, reader.readLine());
                while (true){
                    sendMessage(i+1, reader.readLine());
                    new ChatThread(i+1).start();
                }
            }
        }
    }

    public static void sendMessage(Integer room, String message) throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);
        String m = chat.sendMessage(room, message);
        SimpleDateFormat date = new SimpleDateFormat("HH:mm");
        System.out.println(user.getUserName() + " " + date.format(new Date()) + ": " + message);
    }

    public static void checkMessage(Integer room) throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);
        List<String> list = chat.checkMessage(room);

        int count = 0;


        System.out.println();
        for(int i = list.size()-1; i>0; i--){
            count++;
            if(count == 10){
                break;
            }
            System.out.println(list.get(i));
        }
    }

//    public static void checkLastMessage(Integer room) throws IOException, NotBoundException, RemoteException{
//        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);
//
//        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);
//        List<String> list = chat.checkMessage(room);
//        System.out.println();
//
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(list.size() - 1));
//        }
//    }

    static class ChatThread extends Thread{
        private final int room;

        ChatThread (int room){
            this.room = room;
        }

        public void run(){


            try{
                Thread.sleep(500);
                final Registry registry;
                Chat chat;
                try {
                    registry = LocateRegistry.getRegistry("127.0.0.1",2732);
                    chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);
                    List<String> list = chat.checkMessage(room);
                    System.out.println();

                    for (int i = 0; i < list.size(); i++) {
                        System.out.println(list.get(list.size() - 1));
                    }
                } catch (RemoteException | NotBoundException e) {
                    e.printStackTrace();
                }

            }
            catch(InterruptedException e){
                System.out.println("Thread has been interrupted");
            }

        }
    }
}
