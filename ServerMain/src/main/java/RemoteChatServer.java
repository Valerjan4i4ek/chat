import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RemoteChatServer implements Chat{
    MySQLClass sql = new MySQLClass();
    int countMessage;
    int countAuthorization;
    private final static String JSON_FILE_NAME = "C:\\Users\\Philosoph\\IdeaProjects\\chat\\ServerMain\\src\\rooms.json";

    private static List<Rooms> jsonToRooms(String fileName) throws FileNotFoundException {
        return Arrays.asList(new Gson().fromJson(new FileReader(fileName), Rooms[].class));
    }

    @Override
    public String checkAuthorization(String login, String password) throws RemoteException {
        try{
            Map<String, String> map = sql.check();
            if (map != null && !map.isEmpty()){
                for (Map.Entry<String, String> entry : map.entrySet()){
                    if (login.equals(entry.getKey())){
                        if(!password.equals(entry.getValue())){
                            System.out.println("incorrect password");
                            return "incorrect password";
                        }
                        else {
                            System.out.println("Ok");
                            return "authorization is OK";
                        }
                    }
                    else{
                        sql.addAuthorization(countAuthorization, new User(login, password));
                        incrementAuthorization();
                        System.out.println("new registration");
                        return "new registration";
                    }
                }
            }
            else{
                sql.addAuthorization(countAuthorization, new User(login, password));
                incrementAuthorization();
                System.out.println("new registration");
                return "new registration";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    @Override
    public List<String> chooseRoom() throws RemoteException {
        List<Rooms> roomList = null;
        List<String> list = new CopyOnWriteArrayList<>();
        try {
            roomList = jsonToRooms(JSON_FILE_NAME);
            for (int i = 0; i < roomList.size(); i++) {
                System.out.println((i + 1) + " " + roomList.get(i));
                list.add(roomList.get(i).toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }


    public void incrementMessage(){
        countMessage++;
    }
    public void incrementAuthorization(){countAuthorization++;}

    @Override
    public String sendMessage(Integer room, String message) throws RemoteException {
        Thread run = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        sql.addMessage(countMessage, message);
                        incrementMessage();
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        run.start();

        sql.addMessage(countMessage, message);
        incrementMessage();

        return "";
    }

    @Override
    public List<String> checkMessage(Integer room) throws RemoteException {
        List<String> list = sql.checkLastMessages(room);
        return list;
    }
}
