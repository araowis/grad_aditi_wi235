package service.users.login;

import utils.PasswordHashing;
import persistence.repositories.UserRepository;
import service.auth.*;

public class AdminLogin implements LoginHandler {

    public boolean login(String username, String password, UserRepository userInterface) throws Exception {
        String storedHash = userInterface.fetchHashFromDB(username);

        if (storedHash == null) 
            return false;

        boolean ok = PasswordHashing.verifyPassword(password, storedHash);
        System.out.println((ok)?"Admin logged in":"Invalid admin credentials");
        return ok;
    }
}
