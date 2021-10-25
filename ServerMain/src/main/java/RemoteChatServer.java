import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RemoteChatServer implements Chat{
    MySQLClass sql = new MySQLClass();
    List<Message> listID;
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
            if(map != null && !map.isEmpty()){
                if(map.containsKey(login) && map.get(login).equals(password)){
                    System.out.println("AUTHORIZATION IS OK");
                    return "AUTHORIZATION IS OK";
                }
                else if(map.containsKey(login) && !map.get(login).equals(password)){
                    System.out.println("INCORRECT PASSWORD");
                    return "INCORRECT PASSWORD";
                }
                else{
                    sql.addAuthorization(countAuthorization, new User(login, password));
                    incrementAuthorization();
                    System.out.println("NEW REGISTRATION");
                    return "NEW REGISTRATION";
                }
            }
            else{
                sql.addAuthorization(countAuthorization, new User(login, password));
                incrementAuthorization();
                System.out.println("new registration");
                return "new registration";
            }

        } catch (Exception e){
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


    public void incrementMessage(int room){
        listID = sql.checkMessage(room);
        if(listID != null && !listID.isEmpty()){
            countMessage = listID.get(listID.size()-1).getId();
            countMessage++;
        }else{
            countMessage++;
        }

    }
    public void incrementAuthorization(){countAuthorization++;}

    @Override
    public String sendMessage(Integer room, String message, String user) throws RemoteException {

        incrementMessage(room);
        sql.addMessage(room, countMessage, message, user);

        return "";
    }

    @Override
    public List<Message> checkMessage(Integer room) throws RemoteException{
        List<Message> list = sql.checkMessage(room);
        return list;
    }

    @Override
    public List<Message> chating(Integer room, Integer maxId) throws RemoteException {
        List<Message> list = sql.checkMessage(room);
        List<Message> list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getId() > maxId){
                list2.add(list.get(i));
            }
        }
        return list2;
    }

}
