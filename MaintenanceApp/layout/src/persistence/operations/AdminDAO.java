package persistence.operations;

import persistence.AdminInterface;

import java.sql.*;

import model.user.Owner;

public class AdminDAO implements AdminInterface {

    private final Connection conn;

    public AdminDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public String fetchHashFromDB(String username) {
        String sql = "SELECT password_string FROM ADMIN_USER au " +
                     "JOIN USERS u ON au.user_id = u.user_id " +
                     "WHERE u.user_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("password_string");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveUserToDB(String username, String hash) {
        String insertUser = "INSERT INTO USERS(user_name) VALUES(?) RETURNING user_id";
        String insertAdmin = "INSERT INTO ADMIN_USER(user_id, password_string) VALUES(?, ?)";

        try (PreparedStatement psUser = conn.prepareStatement(insertUser);
             PreparedStatement psAdmin = conn.prepareStatement(insertAdmin)) {

            psUser.setString(1, username);
            ResultSet rs = psUser.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                psAdmin.setInt(1, userId);
                psAdmin.setString(2, hash);
                psAdmin.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addOwner(Owner owner) {
        String sqlUser = "INSERT INTO USERS(user_name) VALUES(?) RETURNING user_id";
        String sqlOwner = "INSERT INTO OWNER_USER(user_id, maintenance_paid) VALUES(?, false)";
        try (PreparedStatement psUser = conn.prepareStatement(sqlUser);
                PreparedStatement psOwner = conn.prepareStatement(sqlOwner)) {
            psUser.setString(1, owner.getName());
            ResultSet rs = psUser.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt(1);
                psOwner.setInt(1, userId);
                psOwner.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveOwnerUserToDB(String username, String hash) {
        String insertUser = "INSERT INTO USERS(user_name) VALUES(?) RETURNING user_id";
        String insertAdmin = "INSERT INTO OWNER_USER(user_id, password_string) VALUES(?, ?)";

        try (PreparedStatement psUser = conn.prepareStatement(insertUser);
             PreparedStatement psAdmin = conn.prepareStatement(insertAdmin)) {

            psUser.setString(1, username);
            ResultSet rs = psUser.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                psAdmin.setInt(1, userId);
                psAdmin.setString(2, hash);
                psAdmin.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateOwner(Owner owner) {
        String sql = "UPDATE OWNER_USER SET maintenance_paid=? WHERE owner_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, owner.isMaintenancePaid());
            ps.setInt(2, owner.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeOwner(int ownerId) {
        String sql = "DELETE FROM OWNER_USER WHERE owner_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void approvePayment(int siteId) {
        String sql = "UPDATE OWNER_USER SET maintenance_paid = true WHERE owner_id = (SELECT owner_id FROM SITE WHERE site_number = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
