package persistence.operations;

import model.site.*;
import model.site.occupancy.OccupancyStatus;
import model.site.type.HouseType;
import persistence.SiteRepository;
import java.sql.*;

public class SiteDAO implements SiteRepository {

    private final Connection conn;
    private final SiteFactory siteFactory;

    public SiteDAO(Connection conn) {
        this.conn = conn;
        this.siteFactory = SiteFactory.getInstance();
    }

    @Override
    public int addSite() {
        Site site = siteFactory.createSite();
        String sql = """
                INSERT INTO SITE
                (ownership_status, length_in_feet, breadth_in_feet)
                VALUES (?, ?, ?)""";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, false);
            ps.setInt(2, site.getLengthInFeet());
            ps.setInt(3, site.getBreadthInFeet());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    site.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void updateSite(Site site) {
        String sql = """
                UPDATE SITE
                SET length_in_feet = ?, breadth_in_feet = ?
                WHERE site_number = ?""";
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
    public void updateOwnedSite(OwnedSite site) {
        String sql = """
                UPDATE SITE
                SET ownership_status = ?, owner_id = ?, house_type = ?
                WHERE site_number = ?""";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, toBoolean(site.getOccupancyStatus()));
            ps.setInt(2, site.getOwnerId());
            if (site.getHouseType() != null)
                ps.setString(3, site.getHouseType().name());
            else
                ps.setNull(3, Types.VARCHAR);
            ps.setInt(4, site.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // @Override
    // public void updateOwnershipStatus(int siteId, OccupancyStatus status) {
    //     String sql = "UPDATE SITE SET ownership_status=? WHERE site_number=?";
    //     try (PreparedStatement ps = conn.prepareStatement(sql)) {
    //         ps.setBoolean(1, toBoolean(status));
    //         ps.setInt(2, siteId);
    //         ps.executeUpdate();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    @Override
    public void assignOwnerToSite(int siteId, int ownerId, OccupancyStatus status, HouseType houseType) {
        String sql = "UPDATE SITE SET owner_id = ?, ownership_status=?, house_type = ? WHERE site_number=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ps.setBoolean(2, toBoolean(status));

            if (houseType == null) {
                ps.setNull(3, java.sql.Types.VARCHAR);
            } else {
                ps.setString(3, houseType.name());
            }
            ps.setInt(4, siteId);
            int updated = ps.executeUpdate();

            if (updated == 0) {
                throw new IllegalStateException("Site already owned or does not exist");
            }
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
    public Site getSiteById(int siteId) {
        String sql = "SELECT * FROM SITE WHERE site_number=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return null;

            int length = rs.getInt("length_in_feet");
            int breadth = rs.getInt("breadth_in_feet");
            boolean ownershipStatus = rs.getBoolean("ownership_status");
            Integer ownerId = rs.getObject("owner_id", Integer.class);
            String houseTypeStr = rs.getString("house_type");

            OccupancyStatus status = toStatus(ownershipStatus);

            if (ownerId == null) {
                Site site = siteFactory.createTemporarySite(length, breadth);
                site.setId(siteId);
                return site;
            }

            OwnedSite site = new OwnedSite(length, breadth, ownerId, status);
            if (houseTypeStr != null) {
                site.setHouseType(HouseType.fromString(houseTypeStr));
            }

            site.setId(siteId);
            return site;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isMaintenancePaid(int siteId) {
        String sql = """
                SELECT maintenance_paid
                FROM OWNER_USER
                WHERE owner_id = (SELECT owner_id FROM SITE WHERE site_number=?)""";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getBoolean("maintenance_paid");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getOccupancyCount(int siteId) {
        String sql = """
                    SELECT COUNT(*)
                    FROM SITE
                    WHERE site_number=? AND ownership_status=true
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean ownerExists(int ownerId) {
        String sql = "SELECT 1 FROM OWNER_USER WHERE owner_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private OccupancyStatus toStatus(boolean ownershipStatus) {
        return ownershipStatus ? OccupancyStatus.OCCUPIED : OccupancyStatus.OPEN;
    }

    private boolean toBoolean(OccupancyStatus status) {
        return status == OccupancyStatus.OCCUPIED;
    }
}