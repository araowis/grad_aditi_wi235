package service.auth;

import persistence.UserRepository;

public interface LoginHandler {
    boolean login(String username, String password, UserRepository userInterface) throws Exception;
}
