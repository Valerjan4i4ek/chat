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
    private static String userName;
    static Map<List<String>, List<String>> checkMap = new LinkedHashMap<>();


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

                int finalI = i;

                new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()){
                        try {

                            Thread.sleep(3000);
                            checkLastMessage(finalI +1);

                        } catch (InterruptedException | IOException | NotBoundException e) {
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

            }
        }
    }

    public static void sendMessage(Integer room, String message, String userInMethod) throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);
        String m = chat.sendMessage(room, message, userInMethod);
//        SimpleDateFormat date = new SimpleDateFormat("HH:mm");
//        System.out.println(user.getUserName() + " " + date.format(new Date()) + ": " + message);
        
    }

    public static void checkMessage(Integer room) throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);
        Map<List<String>, List<String>> map = chat.checkMessage(room);

        checkMap = chat.checkMessage(room);

        int count = 0;

        System.out.println();

        for(Map.Entry<List<String>, List<String>> entry : map.entrySet()){
            List<String> list1 = new ArrayList<>(entry.getKey());
            List<String> list2 = new ArrayList<>(entry.getValue());
            Collections.reverse(list1);
            Collections.reverse(list2);
            Iterator<String> it1 = list1.iterator();
            Iterator<String> it2 = list2.iterator();
            while (it1.hasNext() && it2.hasNext()){
                count++;
                if(count == 10){
                    break;
                }
                System.out.println(it1.next() + ": " + it2.next());
            }

        }

    }


    public static void checkLastMessage(Integer room) throws IOException, NotBoundException, RemoteException{
        final Registry registry = LocateRegistry.getRegistry("127.0.0.1",2732);

        Chat chat = (Chat) registry.lookup(UNIQUE_BINDING_NAME);
        Map<List<String>, List<String>> map = chat.checkMessage(room);
        SimpleDateFormat date = new SimpleDateFormat("HH:mm");

        Set<List<String>> set1 = new TreeSet<List<String>>(map.keySet());
        Set<List<String>> set2 = new TreeSet<List<String>>(checkMap.keySet());
        set1.removeAll(set2);

        for(Map.Entry<List<String>, List<String>> pair : map.entrySet()){
            Iterator<String> it1 = pair.getKey().iterator();
            Iterator<String> it2 = pair.getValue().iterator();
            while (it1.hasNext() && it2.hasNext()){
                System.out.println(date.format(new Date()) + " " + it1.next() + ": " + it2.next());
            }
        }


//        map.remove(checkMap);
//        for(Map.Entry<List<String>, List<String>> pair : checkMap.entrySet()){
//            Iterator<String> it1 = pair.getKey().iterator();
//            Iterator<String> it2 = pair.getValue().iterator();
//            if(!map.containsKey(it1) && !map.containsValue(it2)){
//                while (it1.hasNext() && it2.hasNext()){
//                    System.out.println(date.format(new Date()) + " " + it1.next() + ": " + it2.next());
//                }
//            }
//
//        }


//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(user.getUserName() + " " + date.format(new Date()) + ": " + list.get(i));
//            checkList.add(list.get(i));
//            list.remove(list.get(i));
//        }
    }

}
