package service.users.login;

import persistence.UserInterface;
import service.handlers.LoginHandler;
import utils.PasswordHashing;

public class OwnerLogin implements LoginHandler {

    public boolean login(String username, String password, UserInterface userInterface) throws Exception {
        String storedHash = userInterface.fetchHashFromDB(username);

        if (storedHash == null) 
            return false;

        boolean ok = PasswordHashing.verifyPassword(password, storedHash);
        System.out.println((ok)?"Owner logged in":"Invalid owner credentials");
        return ok;
    }

    private String fetchHashFromDB(String username) {
        return null;
    }
}

