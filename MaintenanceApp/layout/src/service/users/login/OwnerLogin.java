package service.users.login;

import persistence.UserRepository;
import service.auth.LoginHandler;
import utils.PasswordHashing;

public class OwnerLogin implements LoginHandler {

    public boolean login(String username, String password, UserRepository userInterface) throws Exception {
        String storedHash = userInterface.fetchHashFromDB(username);

        if (storedHash == null) 
            return false;

        boolean ok = PasswordHashing.verifyPassword(password, storedHash);
        System.out.println((ok)?"Owner logged in":"Invalid owner credentials");
        return ok;
    }
}

