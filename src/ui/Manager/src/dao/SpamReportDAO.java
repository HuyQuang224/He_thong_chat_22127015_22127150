package dao;

import datastructure.SpamReport;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DatabaseConnection;

public class SpamReportDAO {
    private Connection connection;

    public SpamReportDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<SpamReport> getAllReports() throws SQLException {
        List<SpamReport> reports = new ArrayList<>();
        // Câu lệnh SQL sử dụng JOIN để lấy thông tin email từ bảng USER_ACCOUNT
        String query = """
            SELECT 
                sr.REPORT_ID, 
                sr.REPORTER_ID, 
                sr.REPORTED_USER_ID, 
                sr.MESSAGE_ID, 
                sr.REPORT_REASON, 
                sr.CREATED_AT, 
                sr.STATUS, 
                ua.EMAIL AS REPORTER_EMAIL
            FROM SPAM_REPORT sr
            JOIN USER_ACCOUNT ua ON sr.REPORTER_ID = ua.ID
        """;
    
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int reportId = rs.getInt("REPORT_ID");
                int reporterId = rs.getInt("REPORTER_ID");
                int reportedUserId = rs.getInt("REPORTED_USER_ID");
                long messageId = rs.getLong("MESSAGE_ID");
                String reportReason = rs.getString("REPORT_REASON");
                Timestamp createdAt = rs.getTimestamp("CREATED_AT");
                String status = rs.getString("STATUS");
                String reporterEmail = rs.getString("REPORTER_EMAIL"); // Lấy email từ kết quả truy vấn
    
                // Tạo đối tượng SpamReport bao gồm email của người báo cáo
                SpamReport report = new SpamReport(reportId, reporterId, reportedUserId, messageId, 
                                                   reportReason, createdAt, status, reporterEmail);
                reports.add(report);
            }
        }
        return reports;
    }
    
    public boolean updateReportStatus(int reportId, String status) {
        String sql = "UPDATE spam_report SET status = ? WHERE report_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, reportId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
