package persistence.postgres;

import model.site.*;
import model.site.occupancy.OccupancyStatus;
import model.site.type.HouseType;
import persistence.repositories.SiteRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                SET ownership_status = ?, owner_id = ?, house_type = ?, maintenance_paid = false
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

    @Override
    public void assignOwnerToSite(int siteId, int ownerId, OccupancyStatus status, HouseType houseType) {
        String sql = "UPDATE SITE SET owner_id = ?, ownership_status=?, house_type = ?, maintenance_paid = false WHERE site_number=?";
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
            boolean maintenancePaid = rs.getBoolean("maintenance_paid");

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
            site.setMaintenancePaid(maintenancePaid);
            return site;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Site> getAllSites() {
        String sql = "SELECT * FROM SITE";
        List<Site> sites = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {
            while(rs.next()) {
                OwnedSite site = siteFactory.createTemporaryOwnedSite(
                    rs.getInt("length_in_feet"), 
                    rs.getInt("breadth_in_feet"), 
                    rs.getInt("owner_id"), 
                    rs.getBoolean("ownership_status"));
                site.setId(rs.getInt("site_number"));
                site.setHouseType(HouseType.fromString(rs.getString("house_type")));
                site.setMaintenancePaid(rs.getBoolean("maintenance_paid"));
                sites.add(site);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sites;
    }

    @Override
    public boolean isMaintenancePaid(int siteId) {
        String sql = """
                SELECT maintenance_paid
                FROM SITE
                WHERE site_number = ?""";
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