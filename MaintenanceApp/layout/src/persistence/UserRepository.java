package persistence;

public interface UserRepository {
    String fetchHashFromDB(String username);
}
