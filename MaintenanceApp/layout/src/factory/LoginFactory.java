package factory;

import service.auth.*;
import service.users.login.*;

public class LoginFactory {

    public static LoginHandler create(String role) {
        return switch (role) {
            case "-a" -> new AdminLogin();
            case "-o" -> new OwnerLogin();
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }
}
