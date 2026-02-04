package factory;

import service.auth.*;
import service.users.signup.*;

public class SignupFactory {

    public static SignupHandler create(String role) {
        return switch (role) {
            case "-a" -> new AdminSignup();
            // case "-o" -> new OwnerSignup(); //owner can't sign up
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }
}
