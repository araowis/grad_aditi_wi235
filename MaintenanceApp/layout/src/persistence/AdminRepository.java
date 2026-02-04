package persistence;

public interface AdminRepository extends UserRepository {    
    void createAdmin(String username, String hash);
}
