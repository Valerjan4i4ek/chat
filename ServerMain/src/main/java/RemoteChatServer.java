import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RemoteChatServer implements Chat{
    MySQLClass sql = new MySQLClass();
    int countMessage;
    int countAuthorization;
    private final static String JSON_FILE_NAME = "C:\\Users\\Philosoph\\IdeaProjects\\chat\\ServerMain\\src\\rooms.json";

    private static List<String> jsonToRooms(String fileName) throws FileNotFoundException {
        return Arrays.asList(new Gson().fromJson(new FileReader(fileName), String[].class));
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

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public void incrementMessage(){
        countMessage++;
    }
    public void incrementAuthorization(){countAuthorization++;}

    @Override
    public String sendMessage(String message) throws RemoteException {
        sql.addMessage(countMessage, message);
        incrementMessage();
        return message;
    }

    @Override
    public void chooseRoom() throws RemoteException {
        List<String> roomList = null;
        try {
            roomList = jsonToRooms(JSON_FILE_NAME);
            for (int i = 0; i < roomList.size(); i++) {
                System.out.println((i + 1) + " " + roomList.get(i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
