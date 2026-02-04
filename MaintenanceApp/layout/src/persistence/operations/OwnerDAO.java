package persistence.operations;

import model.site.*;
import model.site.occupancy.OccupancyStatus;
import model.user.Owner;
import model.user.Role;
import persistence.OwnerRepository;

import java.sql.*;

public class OwnerDAO implements OwnerRepository {

    private final Connection conn;
    private final SiteFactory siteFactory;

    public OwnerDAO(Connection conn) {
        this.conn = conn;
        this.siteFactory = SiteFactory.getInstance();
    }

    @Override
    public String fetchHashFromDB(String username) {

        String sql = """
                    SELECT ou.password_string
                    FROM OWNER_USER ou
                    JOIN USERS u ON ou.user_id = u.user_id
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
    public void createOwner(String username, String passwordHash) {

        String insertUser = "INSERT INTO USERS(user_name) VALUES(?) RETURNING user_id";

        String insertOwner = "INSERT INTO OWNER_USER(user_id, maintenance_paid, password_string) VALUES(?, false, ?)";

        try (PreparedStatement psUser = conn.prepareStatement(insertUser);
                PreparedStatement psOwner = conn.prepareStatement(insertOwner)) {

            psUser.setString(1, username);
            ResultSet rs = psUser.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt(1);
                psOwner.setInt(1, userId);
                psOwner.setString(2, passwordHash);
                psOwner.executeUpdate();
            }

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

    @Override
    public Owner getOwnerById(int ownerId) {

        String sql = """
        SELECT o.owner_id, u.user_id, u.user_name, o.maintenance_paid
        FROM OWNER_USER o
        JOIN USERS u ON o.user_id = u.user_id
        WHERE o.owner_id = ?""";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapOwner(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Owner getOwnerByUsername(String username) {

        String sql = 
        """
        SELECT o.owner_id, u.user_id, u.user_name, o.maintenance_paid
        FROM OWNER_USER o
        JOIN USERS u ON o.user_id = u.user_id
        WHERE u.user_name = ?""";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapOwner(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateOwnerDetails(Owner owner) {

        String updateUser = "UPDATE USERS SET user_name=? WHERE user_id=?";
        String updateOwner = "UPDATE OWNER_USER SET maintenance_paid=? WHERE owner_id=?";

        try (PreparedStatement psUser = conn.prepareStatement(updateUser);
                PreparedStatement psOwner = conn.prepareStatement(updateOwner)) {

            psUser.setString(1, owner.getName());
            psUser.setInt(2, owner.getId());
            psUser.executeUpdate();

            psOwner.setBoolean(1, owner.isMaintenancePaid());
            psOwner.setInt(2, owner.getOwnerId());
            psOwner.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePassword(String username, String newHash) {

        String sql = """
        UPDATE OWNER_USER
        SET password_string=?
        WHERE user_id = (SELECT user_id FROM USERS WHERE user_name=?)""";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OwnedSite getOwnedSiteByOwnerId(int ownerId) {

        String sql = "SELECT * FROM SITE WHERE owner_id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                OwnedSite site = siteFactory.createTemporaryOwnedSite(rs.getInt("length_in_feet"), rs.getInt("breadth_in_feet"), ownerId, rs.getBoolean("ownership_status"));
                site.setId(rs.getInt("site_number"));
                return site;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void requestSiteUpdate(Site site) {

        String sql = """
        UPDATE SITE
        SET length_in_feet=?, breadth_in_feet=? 
        WHERE site_number=?""";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, site.getLengthInFeet());
            ps.setInt(2, site.getBreadthInFeet());
            ps.setInt(3, site.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Owner mapOwner(ResultSet rs) throws SQLException {
        Owner owner = new Owner();
        owner.setOwnerId(rs.getInt("owner_id"));
        owner.setId(rs.getInt("user_id"));
        owner.setName(rs.getString("user_name"));
        owner.setMaintenancePaid(rs.getBoolean("maintenance_paid"));
        owner.setRole(Role.OWNER);
        return owner;
    }
}
