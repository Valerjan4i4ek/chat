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
    List<User> listUsers;
    List<PrivateMessage> listPrivateMessage;
    User user;
    int countMessage;
    int countAuthorization;
    int countPrivateMessage;
    private final static String JSON_FILE_NAME = "C:\\Users\\Philosoph\\IdeaProjects\\chat\\ServerMain\\src\\rooms.json";

    public RemoteChatServer(){
//        checkUserInRoom();
    }

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
                    incrementAuthorization();
                    sql.addAuthorization(new User(countAuthorization, login, password));
                    System.out.println("NEW REGISTRATION");
                    return "NEW REGISTRATION";
                }
            }
            else{
                incrementAuthorization();
                sql.addAuthorization(new User(countAuthorization, login, password));
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

    @Override
    public String addUserInRoom(Integer roomId, String userName) throws RemoteException {
        listUsers = sql.checkAuthorization();

        for (int i = 0; i < listUsers.size(); i++) {
            if(listUsers.get(i).getUserName().equals(userName)){
                sql.addUserInRoom(new UserInRoom(System.currentTimeMillis(), roomId, listUsers.get(i).getId(), userName));
            }
        }
        return "";
    }

    @Override
    public List<String> showUsersInRoom(Integer room) throws RemoteException {
        Map<List<Integer>, Map<List<Integer>, String>> map = sql.checkUsersList();
        List<String> returnList = new ArrayList<>();

        if(map != null && !map.isEmpty()){
            for(Map.Entry<List<Integer>, Map<List<Integer>, String>> entry : map.entrySet()){
                if(entry.getKey().contains(room)){
                    for(Map.Entry<List<Integer>, String> pair : entry.getValue().entrySet()){
                        if(!returnList.contains(pair.getValue())){
                            returnList.add(pair.getValue());
                        }
                    }
                }
            }
        }
        return returnList;
    }

    public void checkUserInRoom(){
        List<UserInRoom> list = sql.checkUserInRoom();

        for (int i = list.size()-1; i > 0; i--) {
            if(System.currentTimeMillis() - list.get(i).getLustTimeClientUpdate() > 10000){
                sql.deleteUserInRoom(list.get(i).getUserId());
            }
        }
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
    public void incrementAuthorization(){
        listUsers = sql.checkAuthorization();
        if(listUsers != null && !listUsers.isEmpty()){
            countAuthorization = listUsers.get(listUsers.size()-1).getId();
            countAuthorization++;
        }
        else{
            countAuthorization++;
        }
    }

    public void incrementPrivateMessage(String userTaker){
        listPrivateMessage = sql.checkPrivateMessage(userTaker);
        if(listPrivateMessage != null && !listPrivateMessage.isEmpty()){
//            countPrivateMessage = listPrivateMessage.get(listPrivateMessage.size()-1).getId();
            countPrivateMessage = listPrivateMessage.size();
            countPrivateMessage++;
        }
        else{
            countPrivateMessage++;
        }
    }

    @Override
    public String sendMessage(Integer room, String message, String user) throws RemoteException {

        incrementMessage(room);
        sql.addMessage(room, countMessage, message, user);

        return "";
    }

    @Override
    public String sendPrivateMessage(String message, String userSender, String userTaker) throws RemoteException {

        incrementPrivateMessage(userTaker);
        sql.addPrivateMessage(countPrivateMessage, message, userSender, userTaker);
        return "";
    }

    @Override
    public List<Message> checkMessage(Integer room) throws RemoteException{
        List<Message> list = sql.checkMessage(room);
        return list;
    }

    @Override
    public List<PrivateMessage> checkPrivateMessage(String user) throws RemoteException {
        List<PrivateMessage> list = sql.checkPrivateMessage(user);
//        List<PrivateMessage> list2 = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            if(list.get(i).getUserTaker().equals(user)){
//                list2.add(list.get(i));
//            }
//        }
//        return list2;
        return list;
    }

    @Override
    public List<PrivateMessage> privateChating(Integer maxPrivateId, String user) throws RemoteException{
        List<PrivateMessage> list = sql.checkPrivateMessage(user);
        List<PrivateMessage> list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getId() > maxPrivateId && list.get(i).getUserTaker().equals(user)){
                list2.add(list.get(i));
            }
//            if(list.get(i).getId() > maxPrivateId){
//                list2.add(list.get(i));
//            }
        }
        return list2;
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
