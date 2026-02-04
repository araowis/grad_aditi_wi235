package persistence.operations;

import persistence.AdminRepository;

import java.sql.*;

public class AdminDAO implements AdminRepository {

    private final Connection conn;

    public AdminDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public String fetchHashFromDB(String username) {

        String sql = """
        SELECT password_string
        FROM ADMIN_USER au
        JOIN USERS u ON au.user_id = u.user_id
        WHERE u.user_name = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            return rs.next() ? rs.getString(1) : null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createAdmin(String username, String passwordHash) {

        String insertUser =
            "INSERT INTO USERS(user_name) VALUES(?) RETURNING user_id";

        String insertAdmin =
            "INSERT INTO ADMIN_USER(user_id, password_string) VALUES(?, ?)";

        try (PreparedStatement psUser = conn.prepareStatement(insertUser);
             PreparedStatement psAdmin = conn.prepareStatement(insertAdmin)) {

            psUser.setString(1, username);
            ResultSet rs = psUser.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt(1);
                psAdmin.setInt(1, userId);
                psAdmin.setString(2, passwordHash);
                psAdmin.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
