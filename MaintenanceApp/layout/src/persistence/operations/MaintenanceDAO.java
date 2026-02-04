package persistence.operations;

import persistence.MaintenanceInterface;
import model.site.*;
import model.user.Owner;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceDAO implements MaintenanceInterface {

    private final Connection conn;
    private final SiteFactory siteFactory;

    public MaintenanceDAO(Connection conn) {
        this.conn = conn;
        this.siteFactory = SiteFactory.getInstance();
    }

    public void calculateMonthlyMaintenance() {
        // Only fetch occupied sites and their dimensions
        String sqlSites = """
                    SELECT s.site_number, s.length_in_feet, s.breadth_in_feet, o.occupied_site_type, o.owner_id
                    FROM SITE s
                    JOIN OCCUPIED_SITE o ON s.site_number = o.site_number
                """;

        try (PreparedStatement ps = conn.prepareStatement(sqlSites)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int siteId = rs.getInt("site_number");
                int length = rs.getInt("length_in_feet");
                int breadth = rs.getInt("breadth_in_feet");
                String typeStr = rs.getString("occupied_site_type");
                int ownerId = rs.getInt("owner_id");

                // Create OccupiedSite object
                OccupiedSite site = new OccupiedSite(length, breadth, ownerId);
                site.setOccupiedSiteType(typeStr);

                long maintenanceAmount = site.getMaintenancePrice(); // 9 per sqft

                // Insert maintenance record for this month
                String sqlInsert = """
                            INSERT INTO MAINTENANCE(site_number, maintenance_amount, maintenance_month)
                            VALUES (?, ?, ?)
                        """;
                try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                    psInsert.setInt(1, siteId);
                    psInsert.setLong(2, maintenanceAmount);
                    psInsert.setString(3, java.time.LocalDate.now().getMonth().name().substring(0, 3));
                    psInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void payMaintenance(int siteId, double amount) {
        String sql = "UPDATE MAINTENANCE SET maintenance_amount = maintenance_amount - ? WHERE site_number = ? AND maintenance_month = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, (long) amount);
            ps.setInt(2, siteId);
            ps.setString(3, LocalDate.now().getMonth().name().substring(0, 3));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getTotalMaintenanceDue(int ownerId) {
        String sql = """
                SELECT SUM(m.maintenance_amount) AS total_due
                FROM MAINTENANCE m
                JOIN SITE s ON s.site_number = m.site_number
                WHERE s.owner_id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("total_due");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<MaintenanceRecord> getPendingMaintenance() {
        List<MaintenanceRecord> pending = new ArrayList<>();
        String sql = "SELECT * FROM MAINTENANCE WHERE maintenance_amount > 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int siteId = rs.getInt("site_number");
                long amount = rs.getLong("maintenance_amount");
                String month = rs.getString("maintenance_month");
                pending.add(new MaintenanceRecord(siteId, amount, month));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pending;
    }

    @Override
    public void approveSiteUpdate(int siteId) {
        String sql = "UPDATE SITE SET ownership_status=true WHERE site_number=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rejectSiteUpdate(int siteId, String reason) {
        System.out.println("Rejected site " + siteId + " for reason: " + reason);
    }

    @Override
    public List<Site> getAllPendingSites() {
        List<Site> pending = new ArrayList<>();
        String sql = "SELECT * FROM SITE WHERE ownership_status=false";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Site site = siteFactory.createTemporarySite(rs.getInt("length_in_feet"), rs.getInt("breadth_in_feet"));
                site.setId(rs.getInt("site_number"));
                site.setOccupied(rs.getBoolean("ownership_status"));
                pending.add(site);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pending;
    }

    @Override
    public Site getPendingSiteById(int siteId) {
        String sql = "SELECT * FROM SITE WHERE site_number=? AND ownership_status=false";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Create site via factory
                Site site = siteFactory.createTemporarySite(rs.getInt("length_in_feet"), rs.getInt("breadth_in_feet"));
                site.setId(rs.getInt("site_number"));
                site.setOccupied(rs.getBoolean("ownership_status"));
                site.setLength(rs.getInt("length_in_feet"));
                site.setBreadth(rs.getInt("breadth_in_feet"));
                return site;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Inner class to represent maintenance records
    public static class MaintenanceRecord {
        public final int siteId;
        public final long amount;
        public final String month;

        public MaintenanceRecord(int siteId, long amount, String month) {
            this.siteId = siteId;
            this.amount = amount;
            this.month = month;
        }
    }

}
