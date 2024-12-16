package datastructure;

import java.sql.Timestamp;

public class SpamReport {
    private int reportId;
    private int reporterId;
    private int reportedUserId;
    private long messageId;
    private String reportReason;
    private Timestamp createdAt;
    private String status;
    private String reporterEmail; // Thêm trường email của người báo cáo

    // Constructor đầy đủ với email
    public SpamReport(int reportId, int reporterId, int reportedUserId, long messageId, 
                      String reportReason, Timestamp createdAt, String status, String reporterEmail) {
        this.reportId = reportId;
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
        this.messageId = messageId;
        this.reportReason = reportReason;
        this.createdAt = createdAt;
        this.status = status;
        this.reporterEmail = reporterEmail;
    }

    // Getter and Setter methods
    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getReporterId() {
        return reporterId;
    }

    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }

    public int getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(int reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    @Override
    public String toString() {
        return "SpamReport [reportId=" + reportId + ", reporterId=" + reporterId + 
               ", reportedUserId=" + reportedUserId + ", messageId=" + messageId + 
               ", reportReason=" + reportReason + ", createdAt=" + createdAt + 
               ", status=" + status + ", reporterEmail=" + reporterEmail + "]";
    }
}
