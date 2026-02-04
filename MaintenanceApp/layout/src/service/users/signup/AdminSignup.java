package service.users.signup;

import service.handlers.SignupHandler;
import utils.PasswordHashing;

public class AdminSignup implements SignupHandler {

    public void signup(String username, String password) throws Exception {
        String hash = PasswordHashing.hashPassword(password);
        saveUserToDB(username, hash);
        System.out.println("Admin account created");
    }

    private void saveUserToDB(String username, String hash) {
    }
}

