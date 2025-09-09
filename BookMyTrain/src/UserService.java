import java.util.*;

public class UserService {
    private Map<String, User> userMap = new HashMap<>();
    private User currentUser = null;

    public boolean registerUser(String username, String password, String fullname, String contact){
        if(userMap.containsKey(username)){
            System.out.println("Username already exists, Please choose another");
            return false;
        }

        User user = new User(username,password, fullname, contact);
        userMap.put(username, user);
        System.out.println("Registration Successfull");
        return true;
    }

    public boolean login(String username, String password){
        if(!userMap.containsKey(username)){
            System.out.println("User not found");
            return false;
        }
        User user = userMap.get(username);
        if(!user.getPassword().equals(password)){
            System.out.println("Incorrect password");
            return false;
        }

        currentUser = user;
        System.out.println("Welcome " + currentUser.getFullName()+ "!");
        return true;
    }


    public void logout(){
        if(currentUser != null){
            System.out.println("Logged out successfully " + currentUser.getFullName());
        }
        currentUser = null;
    }

    public User getCurrentUser(){
        if(currentUser != null){
            return currentUser;
        }
        return null;
    }

    public boolean isLoggedIn(){
        return currentUser != null;
    }

}
