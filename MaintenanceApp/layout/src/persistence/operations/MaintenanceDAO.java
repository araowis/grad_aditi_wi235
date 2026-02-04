package persistence.operations;

import persistence.MaintenanceRepository;
import model.site.occupancy.OccupancyStatus;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import persistence.dto.*;;

public class MaintenanceDAO implements MaintenanceRepository {

    private final Connection conn;

    public MaintenanceDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void generateMonthlyMaintenance() {
        String currentMonth = LocalDate.now().getMonth().name().substring(0, 3);
        String sqlSites = "SELECT site_number, length_in_feet, breadth_in_feet, ownership_status FROM SITE WHERE owner_id IS NOT NULL";

        try (PreparedStatement ps = conn.prepareStatement(sqlSites);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int siteId = rs.getInt("site_number");
                int length = rs.getInt("length_in_feet");
                int breadth = rs.getInt("breadth_in_feet");
                boolean ownershipStatus = rs.getBoolean("ownership_status");

                OccupancyStatus status = ownershipStatus
                        ? OccupancyStatus.OCCUPIED
                        : OccupancyStatus.OPEN;

                long amount = status.calculateMaintenance(length * breadth);
                insertMaintenance(siteId, amount, currentMonth);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertMaintenance(int siteId, long amount, String month) throws SQLException {
        String sqlInsert = "INSERT INTO MAINTENANCE(site_number, maintenance_amount, maintenance_month) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            ps.setInt(1, siteId);
            ps.setLong(2, amount);
            ps.setString(3, month);
            ps.executeUpdate();
        }
    }

    @Override
    public void payMaintenance(int siteId, long amount) {
        String sql = """
                    UPDATE MAINTENANCE
                    SET maintenance_amount =
                        GREATEST(maintenance_amount - ?, 0)
                    WHERE site_number = ?
                      AND maintenance_month = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, amount);
            ps.setInt(2, siteId);
            ps.setString(3, LocalDate.now().getMonth().name().substring(0, 3));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MaintenanceRecord> getPendingMaintenance() {
        List<MaintenanceRecord> pending = new ArrayList<>();
        String sql = """
                    SELECT site_number, maintenance_amount, maintenance_month
                    FROM MAINTENANCE
                    WHERE maintenance_amount > 0
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pending.add(new MaintenanceRecord(
                        rs.getInt("site_number"),
                        rs.getLong("maintenance_amount"),
                        rs.getString("maintenance_month")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pending;
    }

    @Override
    public List<MaintenanceRecord> getPendingMaintenanceBySite(int siteId) {
        List<MaintenanceRecord> pending = new ArrayList<>();
        String sql = """
                SELECT maintenance_amount, maintenance_month FROM MAINTENANCE
                WHERE site_number = ? AND maintenance_amount > 0
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pending.add(new MaintenanceRecord(
                        siteId,
                        rs.getLong("maintenance_amount"),
                        rs.getString("maintenance_month")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pending;
    }

    @Override
    public void approveMaintenancePayment(int siteId) {

        String updateOwner = """
                    UPDATE OWNER_USER
                    SET maintenance_paid = true
                    WHERE owner_id = (
                        SELECT owner_id FROM SITE WHERE site_number = ?
                    )
                """;

        String clearMaintenance = """
                    UPDATE MAINTENANCE
                    SET maintenance_amount = 0
                    WHERE site_number = ?
                      AND maintenance_month = ?
                """;

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement psOwner = conn.prepareStatement(updateOwner);
                    PreparedStatement psMaint = conn.prepareStatement(clearMaintenance)) {
                psOwner.setInt(1, siteId);
                psOwner.executeUpdate();
                psMaint.setInt(1, siteId);
                psMaint.setString(2, java.time.LocalDate.now().getMonth().name().substring(0, 3));
                psMaint.executeUpdate();
                conn.commit();
            }

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}