package service.users.login;

import utils.PasswordHashing;
import service.handlers.*;
import persistence.AdminInterface;
import persistence.UserInterface;

public class AdminLogin implements LoginHandler {

    public boolean login(String username, String password, UserInterface userInterface) throws Exception {
        String storedHash = userInterface.fetchHashFromDB(username);

        if (storedHash == null) 
            return false;

        boolean ok = PasswordHashing.verifyPassword(password, storedHash);
        System.out.println((ok)?"Admin logged in":"Invalid admin credentials");
        return ok;
    }
}
