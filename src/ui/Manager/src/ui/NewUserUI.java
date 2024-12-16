package ui;

import dao.UserAccountDAO;
import datastructure.UserAccount;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class NewUserUI extends JFrame {
    private JFrame frame;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserAccountDAO userDAO;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField searchField;
    private JComboBox<String> filterCriteria;
    private JPanel filterPanel;
    private JComboBox<Integer> yearComboBox;
    private JButton showChartButton;
    private JTextArea chartArea;
    private ChartPanel chartPanel;  // Tạo một JPanel để vẽ biểu đồ

    public NewUserUI() {
        this.userDAO = new UserAccountDAO();
        initUI();
        loadData();
        startAutoRefresh();
    }

    private void initUI() {
        setTitle("New User Registration");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLayout(new BorderLayout());

        // Tạo bảng hiển thị dữ liệu
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Username", "Full Name", "Email", "Created At"});
        userTable = new JTable(tableModel);

        // Thêm TableRowSorter để hỗ trợ sắp xếp
        rowSorter = new TableRowSorter<>(tableModel);
        userTable.setRowSorter(rowSorter);

        // Thêm bảng vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);        

        // Tạo panel cho việc lọc dữ liệu
        filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        filterCriteria = new JComboBox<>(new String[]{"Name", "Email"});
        filterPanel.add(new JLabel("Filter by:"));
        filterPanel.add(filterCriteria);

        // Tạo JTextField để nhập tên hoặc email
        searchField = new JTextField(20); // Chiều dài khung nhập liệu
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                applyFilter(searchField.getText());
            }
        });

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);

        add(filterPanel, BorderLayout.NORTH);

        // Tạo JComboBox cho việc chọn năm
        yearComboBox = new JComboBox<>();
        for (int i = 2000; i <= 2024; i++) {
            yearComboBox.addItem(i);
        }
        filterPanel.add(new JLabel("Select Year:"));
        filterPanel.add(yearComboBox);

        // Thêm nút để hiển thị biểu đồ
        showChartButton = new JButton("Show Users by Month");
        showChartButton.addActionListener(e -> {
            try {
                showUsersByMonth();
            } catch (SQLException ex) {
            }
        });
        filterPanel.add(showChartButton);

        add(filterPanel, BorderLayout.NORTH);

        chartPanel = new ChartPanel();  // Khởi tạo ChartPanel
        chartPanel.setPreferredSize(new Dimension(750, 300));  // Điều chỉnh kích thước
        add(chartPanel, BorderLayout.SOUTH);  // Thêm vào giao diện

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showUsersByMonth() throws SQLException {
        int selectedYear = (Integer) yearComboBox.getSelectedItem();
        List<UserAccount> allUsers = userDAO.getAllUsers();

        // Tạo mảng đếm số người dùng theo từng tháng (12 tháng trong năm)
        int[] userCountByMonth = new int[12];

        // Lọc và đếm số người dùng theo tháng trong năm đã chọn
        for (UserAccount user : allUsers) {
            Date createdAt = user.getCreatedAt();
            if (createdAt != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(createdAt);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH); // 0-11 (tháng 0 là tháng 1)
                if (year == selectedYear) {
                    userCountByMonth[month]++;
                }
            }
        }

        chartPanel.setData(userCountByMonth);
        chartPanel.revalidate();  // Cập nhật lại giao diện
        chartPanel.repaint();  // Vẽ lại biểu đồ

    }

    private void loadData() {
        try {
            // Lấy danh sách người dùng từ DAO
            List<UserAccount> allUsers = userDAO.getAllUsers();

            // Lọc người dùng mới đăng ký trong vòng 1 năm
            List<UserAccount> newUsers = filterNewUsers(allUsers);

            // Xóa dữ liệu cũ
            tableModel.setRowCount(0);

            // Thêm dữ liệu mới vào bảng
            for (UserAccount user : newUsers) {
                tableModel.addRow(new Object[]{
                        user.getId(),
                        user.getUsername(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getCreatedAt()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private List<UserAccount> filterNewUsers(List<UserAccount> allUsers) {
        List<UserAccount> newUsers = new ArrayList<>();
        Date currentDate = new Date();
        long oneYearMillis = 365L * 24 * 60 * 60 * 1000; // 1 năm = 365 ngày

        for (UserAccount user : allUsers) {
            Date createdAt = user.getCreatedAt();
            if (createdAt != null && (currentDate.getTime() - createdAt.getTime()) <= oneYearMillis) {
                newUsers.add(user);
            }
        }

        return newUsers;
    }

    private void applyFilter(String filterText) {
        String filterType = (String) filterCriteria.getSelectedItem();

        if (filterText.trim().isEmpty()) {
            rowSorter.setRowFilter(null); // Nếu không có filter, hiển thị tất cả
        } else {
            int columnIndex = filterType.equals("Name") ? 2 : 3; // 2 = "Full Name", 3 = "Email"

            // Tạo bộ lọc dựa trên từ khóa và cột lọc
            RowFilter<Object, Object> rf = RowFilter.regexFilter("(?i)" + filterText, columnIndex);
            rowSorter.setRowFilter(rf);
        }
    }
    
    public void showUI() {
        setVisible(true);
    }

    // JPanel để vẽ biểu đồ
    class ChartPanel extends JPanel {
        private int[] userCountByMonth;
    
        public void setData(int[] userCountByMonth) {
            this.userCountByMonth = userCountByMonth;
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (userCountByMonth == null) return;

            Graphics2D g2d = (Graphics2D) g;
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int barWidth = panelWidth / 16; // Độ rộng của mỗi cột

            // Vẽ trục X và Y
            g2d.setColor(Color.BLACK);
            g2d.drawLine(50, panelHeight - 50, panelWidth - 50, panelHeight - 50);  // Trục X
            g2d.drawLine(50, 50, 50, panelHeight - 50);  // Trục Y

            // Hiển thị nhãn đơn vị trên trục X và Y
            g2d.drawString("Tháng", panelWidth - 40, panelHeight - 55); // Nhãn trục X
            g2d.drawString("Lượt", 10, 40); // Nhãn trục Y

            // Hiển thị các đơn vị trên trục Y
            int yAxisMax = getMaxValue(userCountByMonth) + 5; // Tìm giá trị tối đa của dữ liệu (cộng thêm để tránh sát mép)
            int yAxisStep = Math.max(1, yAxisMax / 10); // Đơn vị tăng của trục Y
            for (int i = 0; i <= yAxisMax; i += yAxisStep) {
                int yPosition = panelHeight - 50 - (i * (panelHeight - 100) / yAxisMax); // Tính toán vị trí
                g2d.drawString(String.valueOf(i), 30, yPosition + 5); // Hiển thị giá trị
                g2d.drawLine(45, yPosition, 50, yPosition); // Vẽ dấu trục Y
            }

            // Vẽ các thanh biểu đồ
            for (int i = 0; i < 12; i++) {
                int barHeight = userCountByMonth[i] * (panelHeight - 100) / yAxisMax; // Chiều cao thanh
                int x = 50 + i * (barWidth + 5); // Tính vị trí X
                int y = panelHeight - 50 - barHeight; // Tính vị trí Y

                g2d.setColor(Color.BLUE);
                g2d.fillRect(x, y, barWidth, barHeight); // Vẽ thanh

                // Vẽ nhãn tháng dưới mỗi thanh
                g2d.setColor(Color.BLACK);
                g2d.drawString("Tháng " + (i + 1), x + (barWidth / 4), panelHeight - 30);

                // Hiển thị số lượng ngay trên mỗi thanh
                g2d.setColor(Color.RED);
                g2d.drawString(String.valueOf(userCountByMonth[i]), x + (barWidth / 2) - 10, y - 5);
            }
        }

    
        // Hàm tính giá trị tối đa của dữ liệu để hiển thị đơn vị trục Y
        private int getMaxValue(int[] data) {
            int max = 0;
            for (int value : data) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }
    }
    
}
