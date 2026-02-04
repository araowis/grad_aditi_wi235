package service.handlers;

import persistence.UserInterface;

public interface LoginHandler {
    boolean login(String username, String password, UserInterface userInterface) throws Exception;
}
