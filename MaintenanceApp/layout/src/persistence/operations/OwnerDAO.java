package persistence.operations;

import model.site.*;
import model.user.Owner;
import persistence.*;

import java.sql.*;

public class OwnerDAO implements OwnerInterface {

    private final Connection conn;
    private final SiteFactory siteFactory;

    public OwnerDAO(Connection conn) {
        this.conn = conn;
        this.siteFactory = SiteFactory.getInstance();
    }

    @Override
    public String fetchHashFromDB(String username) {
        String sql = "SELECT password_string FROM OWNER_USER ou " +
                "JOIN USERS u ON ou.user_id = u.user_id " +
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
    public Owner viewOwnerDetailsByUsername(String username) {
        String sql = """
                SELECT o.owner_id,
                       u.user_id,
                       u.user_name,
                       o.maintenance_paid
                FROM OWNER_USER o
                JOIN USERS u ON o.user_id = u.user_id
                WHERE u.user_name = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Owner owner = new Owner();
                owner.setOwnerId(rs.getInt("owner_id"));
                owner.setId(rs.getInt("user_id"));
                owner.setName(rs.getString("user_name"));
                owner.setMaintenancePaid(rs.getBoolean("maintenance_paid"));
                owner.setRole(model.user.Role.OWNER);
                return owner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Owner viewOwnerDetails(int ownerId) {
        String sql = "SELECT o.owner_id, u.user_id, u.user_name, o.maintenance_paid " +
                "FROM OWNER_USER o JOIN USERS u ON o.user_id = u.user_id " +
                "WHERE o.owner_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Owner owner = new Owner();
                owner.setOwnerId(rs.getInt("owner_id"));
                owner.setId(rs.getInt("user_id"));
                owner.setName(rs.getString("user_name"));
                owner.setMaintenancePaid(rs.getBoolean("maintenance_paid"));
                owner.setRole(model.user.Role.OWNER);
                return owner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateOwnerDetails(Owner owner) {
        try {
            // Update USERS table for name
            String sqlUser = "UPDATE USERS SET user_name=? WHERE user_id=?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUser)) {
                ps.setString(1, owner.getName());
                ps.setInt(2, owner.getId());
                ps.executeUpdate();
            }

            // Update OWNER_USER table for maintenance flag
            String sqlOwner = "UPDATE OWNER_USER SET maintenance_paid=? WHERE owner_id=?";
            try (PreparedStatement ps = conn.prepareStatement(sqlOwner)) {
                ps.setBoolean(1, owner.isMaintenancePaid());
                ps.setInt(2, owner.getOwnerId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OwnedSite viewOwnSite(int ownerId) {
        String sql = """
                SELECT s.*
                FROM SITE s
                WHERE s.owner_id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                OwnedSite site = siteFactory.createTemporaryOwnedSite(
                    rs.getInt("length_in_feet"),
                    rs.getInt("breadth_in_feet"),
                    rs.getInt("owner_id"));
                site.setId(rs.getInt("site_number"));
                site.setOccupied(rs.getBoolean("ownership_status"));
                return site;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void requestSiteUpdate(Site site) {
        String sql = "UPDATE SITE SET length_in_feet=?, breadth_in_feet=? WHERE site_number=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, site.getLengthInFeet());
            ps.setInt(2, site.getBreadthInFeet());
            ps.setInt(3, site.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void payMaintenance(OwnedSite site, double amount) {
        try {
            // Find occupied_site_number for this site
            String sqlGetOccupied = "SELECT site_number FROM SITE WHERE site_number=?";
            int occupiedSiteId = -1;
            try (PreparedStatement ps = conn.prepareStatement(sqlGetOccupied)) {
                ps.setInt(1, site.getId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    occupiedSiteId = rs.getInt("occupied_site_number");
                }
            }

            if (occupiedSiteId == -1) {
                throw new SQLException("Occupied site not found for site id " + site.getId());
            }

            // Insert into MAINTENANCE table
            String sqlInsert = "INSERT INTO MAINTENANCE (occupied_site_number, maintenance_amount, maintenance_month) "
                    +
                    "VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setInt(1, occupiedSiteId);
                ps.setDouble(2, amount);
                ps.setString(3, java.time.LocalDate.now().getMonth().name().substring(0, 3));
                ps.executeUpdate();
            }

            // Update OWNER_USER.maintenance_paid flag
            String sqlUpdateOwner = "UPDATE OWNER_USER SET maintenance_paid=true WHERE owner_id IN " +
                    "(SELECT owner_id FROM OCCUPIED_SITE WHERE site_number=?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateOwner)) {
                ps.setInt(1, site.getId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePassword(String username, String newHash) {
        String sql = "UPDATE ADMIN_USER SET password_string=? " +
                "WHERE user_id = (SELECT user_id FROM USERS WHERE user_name=?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
