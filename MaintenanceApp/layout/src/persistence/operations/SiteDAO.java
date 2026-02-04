package persistence.operations;

import model.site.*;
import persistence.*;
import java.sql.*;

public class SiteDAO implements SiteInterface {

    private final Connection conn;
    private final SiteFactory siteFactory;

    public SiteDAO(Connection conn) {
        this.conn = conn;
        this.siteFactory = SiteFactory.getInstance();
    }

    @Override
    public int addSite() {
        // real site creation
        Site site = siteFactory.createSite();
        String sql = "INSERT INTO SITE(ownership_status, length_in_feet, breadth_in_feet) VALUES(false, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, site.getLengthInFeet());
            ps.setInt(2, site.getBreadthInFeet());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Adding site failed, no rows affected.");
                return -1;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    site.setId(generatedId); // keep track in the object
                    System.out.println("Site added using SiteFactory with ID " + generatedId);
                    return generatedId;
                } else {
                    System.out.println("Adding site failed, no ID obtained.");
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void updateSite(Site site) {
        try {
            // Determine owner ID safely
            Integer ownerId = null;
            if (site instanceof OwnedSite ownedSite) {
                ownerId = ownedSite.getOwnerId(); // safe
            }

            // Update SITE table
            String sql = "UPDATE SITE SET ownership_status = ?, length_in_feet = ?, breadth_in_feet = ?, owner_id = ? WHERE site_number = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBoolean(1, site.isOccupied());
                ps.setInt(2, site.getLengthInFeet());
                ps.setInt(3, site.getBreadthInFeet());
                if (ownerId != null) {
                    ps.setInt(4, ownerId);
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                ps.setInt(5, site.getId());
                ps.executeUpdate();
            }

            // Only add to OCCUPIED_SITE if it’s actually an OccupiedSite
            if (site instanceof OccupiedSite occupiedSite && occupiedSite.getOccupiedHouseType() != null) {
                addOccupiedSite(occupiedSite);
            }

            System.out.println("Site updated successfully (ID: " + site.getId() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateOwnedSite(OwnedSite site) {
        String sql = "UPDATE SITE SET ownership_status = ?, length_in_feet = ?, breadth_in_feet = ?, owner_id = ? WHERE site_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, site.isOccupied());
            ps.setInt(2, site.getLengthInFeet());
            ps.setInt(3, site.getBreadthInFeet());
            ps.setInt(4, site.getOwnerId());
            ps.setInt(5, site.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addOccupiedSite(OccupiedSite occupiedSite) {
        String sql = "INSERT INTO OCCUPIED_SITE (site_number, occupied_site_type, owner_id) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, occupiedSite.getId());
            ps.setString(2, occupiedSite.getOccupiedHouseType().name());
            ps.setInt(3, occupiedSite.getOwnerId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Adding occupied site failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int occupiedSiteNumber = generatedKeys.getInt(1);
                    occupiedSite.setOccupiedSiteNumber(occupiedSiteNumber);
                } else {
                    throw new SQLException("Adding occupied site failed, no ID obtained.");
                }
            }

            System.out.println("Occupied site added with ID: " + occupiedSite.getOccupiedSiteNumber());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeSite(int siteId) {
        String sql = "DELETE FROM SITE WHERE site_number=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateOwnershipStatus(int siteId, boolean isOccupied) {
        String sql = "UPDATE SITE SET ownership_status=? WHERE site_number=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isOccupied);
            ps.setInt(2, siteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getOccupancyCount(int siteId) {
        String sql = "SELECT COUNT(*) AS count FROM OCCUPIED_SITE WHERE site_number=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean isMaintenancePaid(int siteId) {
        String sql = "SELECT maintenance_paid FROM OWNER_USER WHERE owner_id IN " +
                "(SELECT owner_id FROM SITE WHERE site_number=?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            // if multiple owners, return true only if all have paid
            while (rs.next()) {
                if (!rs.getBoolean("maintenance_paid"))
                    return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Site getSiteById(int siteId) {
        String sql = "SELECT * FROM SITE WHERE site_number=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Create site using factory
                Site site = siteFactory.createTemporarySite(rs.getInt("length_in_feet"), rs.getInt("breadth_in_feet"));

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
    public OwnedSite getOwnedSiteById(int siteId) {
        String sql = "SELECT * FROM SITE WHERE site_number=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                OwnedSite ownedSite = new OpenSite(rs.getInt("length_in_feet"), rs.getInt("breadth_in_feet"), rs.getInt("owner_id"));

                ownedSite.setId(rs.getInt("site_number"));
                ownedSite.setOccupied(rs.getBoolean("ownership_status"));

                return ownedSite;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean ownerExists(int ownerId) {
        String sql = "SELECT 1 FROM OWNER_USER WHERE owner_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
