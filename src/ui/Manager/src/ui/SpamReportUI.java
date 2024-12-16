package ui;

import dao.SpamReportDAO;
import dao.UserAccountDAO;
import datastructure.SpamReport;
import java.awt.*;
import java.security.Timestamp;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


public class SpamReportUI extends JFrame {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField searchField;
    private JComboBox<String> filterCriteria;
    private JComboBox<String> timeFilter;
    private UserAccountDAO userAccountDAO;
    private SpamReportDAO spamReportDAO;

    public SpamReportUI() {
        this.spamReportDAO = new SpamReportDAO();
        this.userAccountDAO = new UserAccountDAO();
        initUI();
        loadData();
        startAutoRefresh();
    }

    private void initUI() {
        setTitle("Spam Report Management");
        setSize(1000, 600); // Tăng kích thước để phù hợp thêm cột email
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tạo bảng hiển thị dữ liệu
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
                "Report ID", "Reporter ID", "Reported User ID", "Message ID",
                "Report Reason", "Created At", "Status", "Reporter Email" // Thêm cột email
        });

        reportTable = new JTable(tableModel);

        // Thêm TableRowSorter để hỗ trợ sắp xếp
        rowSorter = new TableRowSorter<>(tableModel);
        reportTable.setRowSorter(rowSorter);

        // Thêm bảng vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Tạo panel cho việc lọc và sắp xếp
        JPanel controlPanel = new JPanel(new FlowLayout());

        filterCriteria = new JComboBox<>(new String[]{"Reason", "Status", "Reporter Email"});
        controlPanel.add(new JLabel("Filter by:"));
        controlPanel.add(filterCriteria);

        searchField = new JTextField(15);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                applyFilter(searchField.getText());
            }
        });
        controlPanel.add(new JLabel("Search:"));
        controlPanel.add(searchField);

        // Lọc theo thời gian
        timeFilter = new JComboBox<>(new String[]{"All", "Last 24 Hours", "Last 7 Days", "Last Month"});
        timeFilter.addActionListener(e -> applyTimeFilter());
        controlPanel.add(new JLabel("Time Filter:"));
        controlPanel.add(timeFilter);

        // Nút khóa tài khoản
        JButton banUserButton = new JButton("Ban User");
        banUserButton.addActionListener(e -> banSelectedUser());
        controlPanel.add(banUserButton);

        add(controlPanel, BorderLayout.NORTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadData() {
        try {
            List<SpamReport> reports = spamReportDAO.getAllReports();

            tableModel.setRowCount(0); // Clear table
            for (SpamReport report : reports) {
                tableModel.addRow(new Object[]{
                        report.getReportId(),
                        report.getReporterId(),
                        report.getReportedUserId(),
                        report.getMessageId(),
                        report.getReportReason(),
                        report.getCreatedAt(),
                        report.getStatus(),
                        report.getReporterEmail() // Thêm email vào bảng
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilter(String filterText) {
        String filterType = (String) filterCriteria.getSelectedItem();

        if (filterText.trim().isEmpty()) {
            rowSorter.setRowFilter(null); // Hiển thị tất cả nếu không nhập filter
        } else {
            int columnIndex = switch (filterType) {
                case "Reason" -> 4;
                case "Status" -> 6;
                case "Reporter Email" -> 7; // Lọc theo email (cột mới thêm)
                default -> -1;
            };
            if (columnIndex != -1) {
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + filterText, columnIndex));
            }
        }
    }

    private void applyTimeFilter() {
    String selectedFilter = (String) timeFilter.getSelectedItem();

    // Không áp dụng lọc nếu người dùng chọn "All"
    if ("All".equalsIgnoreCase(selectedFilter)) {
        rowSorter.setRowFilter(null);
        return;
    }

    // Lấy thời gian hiện tại
    long currentTime = System.currentTimeMillis();
    long filterTime;

    // Tính toán khoảng thời gian dựa trên bộ lọc
    switch (selectedFilter) {
        case "Last 24 Hours":
            filterTime = currentTime - (24 * 60 * 60 * 1000L); // 24 giờ trước
            break;
        case "Last 7 Days":
            filterTime = currentTime - (7 * 24 * 60 * 60 * 1000L); // 7 ngày trước
            break;
        case "Last Month":
            filterTime = currentTime - (30L * 24 * 60 * 60 * 1000L); // 30 ngày trước
            break;
        default:
            filterTime = 0;
            break;
    }

    // Áp dụng RowFilter để lọc các dòng có thời gian >= filterTime
    rowSorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
        @Override
        public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
            int rowIndex = entry.getIdentifier();
            Object createdAtValue = tableModel.getValueAt(rowIndex, 5); // Cột "Created At"

            // Kiểm tra nếu giá trị không phải LocalDateTime hoặc Instant
            if (createdAtValue == null || !(createdAtValue instanceof java.time.LocalDateTime)) {
                return false;
            }

            // Chuyển LocalDateTime sang milliseconds
            LocalDateTime createdAt = (LocalDateTime) createdAtValue;
            long createdAtMillis = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            return createdAtMillis >= filterTime;
        }
    });
}

    
    

    private void banSelectedUser() {
        int selectedRow = reportTable.getSelectedRow();
    
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No user selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String status = (String) tableModel.getValueAt(selectedRow, 6); // Cột "Status"
    
        if ("Resolved".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(this, "This report is already resolved.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        int userId = (int) tableModel.getValueAt(selectedRow, 2); // Cột "Reported User ID"
        int reportId = (int) tableModel.getValueAt(selectedRow, 0); // Cột "Report ID"
    
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to ban this user?", "Confirm Ban", JOptionPane.YES_NO_OPTION);
    
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = userAccountDAO.updateUserBannedStatus(userId, true);
    
            if (success) {
                boolean statusUpdated = spamReportDAO.updateReportStatus(reportId, "Resolved");
    
                if (statusUpdated) {
                    tableModel.setValueAt("Resolved", selectedRow, 6); // Cập nhật trạng thái trên bảng
                    JOptionPane.showMessageDialog(this, "User has been banned and report resolved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update report status.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to ban user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void startAutoRefresh() {
        Thread autoRefreshThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000); // Lặp lại sau mỗi 5 giây
                    loadData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        autoRefreshThread.setDaemon(true); // Đảm bảo thread không giữ ứng dụng
        autoRefreshThread.start();
    }


    public void showUI() {
        setVisible(true);
    }
}
