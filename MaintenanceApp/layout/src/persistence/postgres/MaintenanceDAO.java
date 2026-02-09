package persistence.postgres;

import model.site.occupancy.OccupancyStatus;
import persistence.dto.MaintenanceRecord;
import persistence.repositories.MaintenanceRepository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class MaintenanceDAO implements MaintenanceRepository {

    private final Connection conn;

    public MaintenanceDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void generateMonthlyMaintenance() {

        String currentMonth = LocalDate.now().getMonth().name().substring(0, 3);

        String sqlSites = """
                SELECT site_number, length_in_feet, breadth_in_feet, ownership_status FROM SITE
                WHERE owner_id IS NOT NULL
                """;

        try (PreparedStatement ps = conn.prepareStatement(sqlSites); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int siteId = rs.getInt("site_number");
                int length = rs.getInt("length_in_feet");
                int breadth = rs.getInt("breadth_in_feet");
                boolean ownershipStatus = rs.getBoolean("ownership_status");

                OccupancyStatus status = ownershipStatus ? OccupancyStatus.OCCUPIED : OccupancyStatus.OPEN;

                long amount = status.calculateMaintenance(length * breadth);

                insertMaintenance(siteId, amount, currentMonth);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertMaintenance(int siteId, long amount, String month) throws SQLException {

        String maintenanceSql = """
        INSERT INTO MAINTENANCE (site_number, maintenance_amount, maintenance_month, payment_made) VALUES (?, ?, ?, false)
        ON CONFLICT (site_number, maintenance_month) DO NOTHING
        """;
        String siteSql = "UPDATE SITE SET maintenance_paid = false WHERE site_number = ?";

        try (PreparedStatement ps = conn.prepareStatement(maintenanceSql)) {
            ps.setInt(1, siteId);
            ps.setLong(2, amount);
            ps.setString(3, month);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = conn.prepareStatement(siteSql)) {
            ps.setInt(1, siteId);
            ps.executeUpdate();
        }
    }

    @Override
    public void payMaintenance(int siteId, long amount, Date paymentDate) {

        String sql = "INSERT INTO MAINTENANCE_PAYMENT_REQUEST (site_number, maintenance_amount, maintenance_month, payment_request_date, payment_date) VALUES (?, ?, ?, NULL, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ps.setLong(2, amount);
            ps.setString(3, LocalDate.now().getMonth().name().substring(0, 3));
            // ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setDate(4, paymentDate);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MaintenanceRecord> getPendingMaintenance() {

        List<MaintenanceRecord> pending = new ArrayList<>();
        String sql = """
        SELECT site_number, maintenance_amount, maintenance_month FROM MAINTENANCE
        WHERE payment_made = false
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pending.add(new MaintenanceRecord(rs.getInt("site_number"), rs.getLong("maintenance_amount"), rs.getString("maintenance_month")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pending;
    }

    public List<MaintenanceRecord> getMaintenancePaymentRequestList() {
        List<MaintenanceRecord> requests = new ArrayList<>();
        String sql = """
        SELECT site_number, maintenance_amount, maintenance_month, payment_request_date, payment_date
        FROM MAINTENANCE_PAYMENT_REQUEST
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                requests.add(new MaintenanceRecord(rs.getInt("site_number"), rs.getLong("maintenance_amount"),rs.getString("maintenance_month")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public void approveMaintenancePayment(int siteId) {

        String updateMaintenance = """
        UPDATE MAINTENANCE
        SET payment_made = true, payment_date = ? 
        WHERE site_number = ? AND maintenance_month = ?
        """;

        String updateSite = """
        UPDATE SITE
        SET maintenance_paid = true
        WHERE site_number = ?
        """;

        String removeRequest = """
        DELETE FROM MAINTENANCE_PAYMENT_REQUEST
        WHERE site_number = ?
        """;

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement psMaint = conn.prepareStatement(updateMaintenance); PreparedStatement psOwner = conn.prepareStatement(updateSite); PreparedStatement psRemove = conn.prepareStatement(removeRequest)) {

                psMaint.setDate(1, Date.valueOf(LocalDate.now()));
                psMaint.setInt(2, siteId);
                psMaint.setString(3, LocalDate.now().getMonth().name().substring(0, 3));
                psMaint.executeUpdate();

                psOwner.setInt(1, siteId);
                psOwner.executeUpdate();

                psRemove.setInt(1, siteId);
                psRemove.executeUpdate();

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
