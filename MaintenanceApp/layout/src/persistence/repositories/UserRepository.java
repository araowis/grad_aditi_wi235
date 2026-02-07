package persistence.repositories;

public interface UserRepository {
    String fetchHashFromDB(String username);
}
