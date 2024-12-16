package ui;

import dao.LoginHistoryDAO;
import datastructure.LoginHistory;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import ui.NewUserUI.ChartPanel;

public class UserLoginHistoryUI extends JFrame {

    private JTable userTable;
    private DefaultTableModel tableModel;
    private LoginHistoryDAO loginHistoryDAO;
    private JScrollPane scrollPane;
    private TableRowSorter<TableModel> rowSorter;
    private JTextField filterTextField; 
    private JButton filterButton;   
    private MonthChartPanel monthChartPanel;
    private JButton showChartButton;
    private JComboBox<Integer> yearComboBox;
    private ChartPanel chartPanel; 

    public UserLoginHistoryUI() {
        this.loginHistoryDAO = new LoginHistoryDAO();
        initUI();
        loadData();
        startAutoRefresh();
    }

    private void initUI() {
        setTitle("User Login History");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLayout(new BorderLayout());
    
        // Tạo bảng và set model
        String[] columnNames = {"User ID", "Username", "Login Count", "Login Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
    
        // Tạo TableRowSorter và áp dụng cho JTable
        rowSorter = new TableRowSorter<>(tableModel);
        userTable.setRowSorter(rowSorter);
    
        scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
    
        // Panel để chứa JComboBox, JTextField và Button
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());
    
        // ComboBox để chọn tiêu chí lọc
        JComboBox<String> filterCriteria = new JComboBox<>(new String[]{"Username", "Login Count"});
        filterPanel.add(new JLabel("Filter by:"));
        filterPanel.add(filterCriteria);
    
        // JTextField để nhập từ khóa tìm kiếm
        JTextField searchField = new JTextField(20);
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
    
        // Lắng nghe sự kiện khi nhập từ khóa tìm kiếm
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                // Gọi phương thức lọc dựa trên tiêu chí và từ khóa
                applyFilter(searchField.getText(), filterCriteria.getSelectedItem().toString());
            }
        });

        // Thêm panel lọc vào frame
        add(filterPanel, BorderLayout.NORTH);

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
                showLoginByMonth();
            } catch (SQLException ex) {
            }
        });
        filterPanel.add(showChartButton);

        add(filterPanel, BorderLayout.NORTH);
    
        monthChartPanel = new MonthChartPanel();
        monthChartPanel.setPreferredSize(new Dimension(750, 300));
        add(monthChartPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void showLoginByMonth() throws SQLException { 
    // Tạo JComboBox cho việc chọn năm
    int[] monthPanel = new int[12];
    for (int i = 0; i <= 11; i++) {
        monthPanel[i] = 0;
    }

    int selectedYear = (Integer) yearComboBox.getSelectedItem();
    List<LoginHistory> allLoginHistory = loginHistoryDAO.getAllLoginHistory(); // Lấy tất cả lịch sử đăng nhập

    // Tạo mảng đếm số lần đăng nhập theo từng tháng (12 tháng trong năm)
    int[] loginCountByMonth = new int[12];

    // Lọc và đếm số lần đăng nhập theo tháng trong năm đã chọn
    for (LoginHistory loginHistory : allLoginHistory) {
        String loginTime = loginHistory.getLoginTime();  // Lấy thời gian đăng nhập dưới dạng String
        if (loginTime != null) {
            // Chuyển đổi chuỗi thời gian loginTime thành đối tượng Date
            try {
                // Chuyển chuỗi thành java.util.Date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
                java.util.Date date = sdf.parse(loginTime);

                // Chuyển java.util.Date thành java.sql.Date
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                // Sau đó bạn có thể sử dụng sqlDate (kiểu java.sql.Date) để lưu vào cơ sở dữ liệu hoặc tiếp tục xử lý
                Calendar cal = Calendar.getInstance();
                cal.setTime(sqlDate);  // Lúc này bạn có thể dùng cal để lấy ngày, tháng, năm...
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH); // 0-11 (tháng 0 là tháng 1)
                
                if (year == selectedYear) {
                    loginCountByMonth[month]++;
                    
                }
            } catch (ParseException e) {
                e.printStackTrace();  // Xử lý lỗi khi parse thời gian
            }
        }
    }

    monthChartPanel.setData(loginCountByMonth,monthPanel); // Cập nhật dữ liệu cho biểu đồ
    monthChartPanel.revalidate();  // Cập nhật lại giao diện
    monthChartPanel.repaint();  // Vẽ lại biểu đồ
}

    private void loadData() {
        try {
            // Lấy danh sách tất cả người dùng
            List<LoginHistory> userList = loginHistoryDAO.getAllLoginHistory();

            // Xóa dữ liệu cũ trong bảng
            tableModel.setRowCount(0);

            // Thêm dữ liệu mới vào bảng
            for (LoginHistory loginHistory : userList) {
                // Gọi phương thức đếm số lần đăng nhập cho mỗi người dùng
                int loginCount = loginHistoryDAO.getLoginCountByUserID(loginHistory.getUserID());

            // Thêm dòng mới vào bảng
            Object[] row = new Object[]{
                loginHistory.getUserID(),
                loginHistory.getUserName(),
                loginCount,
                loginHistory.getLoginTime(),
            };
            tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading login history.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilter(String keyword, String criteria) {
        if (keyword == null || keyword.isEmpty()) {
            // Nếu từ khóa trống, hiển thị toàn bộ dữ liệu
            rowSorter.setRowFilter(null);
            return;
        }
    
        // Xác định cột cần lọc dựa trên tiêu chí
        int columnIndex;
        if ("Username".equals(criteria)) {
            columnIndex = 1; // Cột "Username"
        } else if ("Login Count".equals(criteria)) {
            columnIndex = 2; // Cột "Login Count"
        } else {
            return; // Nếu tiêu chí không hợp lệ, thoát
        }
    
        // Tạo RowFilter để lọc dữ liệu
        try {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, columnIndex));
        } catch (java.util.regex.PatternSyntaxException e) {
            e.printStackTrace();
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
    
    class MonthChartPanel extends JPanel {
    private int[] userCountByMonth; // Mảng chứa số lượng người dùng hoạt động theo tháng
    private int[] months;          // Mảng chứa các tháng (1-12)

    public void setData(int[] userCountByMonth, int[] months) {
        this.userCountByMonth = userCountByMonth;
        this.months = months;
        repaint(); // Yêu cầu vẽ lại biểu đồ khi dữ liệu thay đổi
    }

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (userCountByMonth == null || months == null) return;

    Graphics2D g2d = (Graphics2D) g;
    int panelWidth = getWidth();
    int panelHeight = getHeight();
    int barWidth = panelWidth / (userCountByMonth.length + 5); // Độ rộng mỗi thanh

    // Vẽ trục X và Y
    g2d.setColor(Color.BLACK);
    g2d.drawLine(50, panelHeight - 50, panelWidth - 50, panelHeight - 50);  // Trục X
    g2d.drawLine(50, 50, 50, panelHeight - 50);  // Trục Y

    // Thêm nhãn đơn vị cho trục X và Y
    g2d.drawString("Tháng", panelWidth - 40, panelHeight - 55); // Nhãn cho trục X
    g2d.drawString("Lượt", 10, 40); // Nhãn cho trục Y

    // Hiển thị các đơn vị trên trục Y
    int yAxisMax = getMaxValue(userCountByMonth) + 5; // Tìm giá trị tối đa của dữ liệu (cộng thêm để tránh sát mép)
    int yAxisStep = Math.max(1, yAxisMax / 10);  // Đơn vị tăng của trục Y (chia nhỏ trục Y)
    for (int i = 0; i <= yAxisMax; i += yAxisStep) {
        int yPosition = panelHeight - 50 - (i * (panelHeight - 100) / yAxisMax); // Tính toán vị trí của mỗi đơn vị
        g2d.drawString(String.valueOf(i), 30, yPosition + 5);  // Hiển thị giá trị
        g2d.drawLine(45, yPosition, 50, yPosition);  // Vẽ dấu cho trục Y
    }

    // Vẽ các thanh biểu đồ cho từng tháng
    for (int i = 0; i < 12; i++) {
        int barHeight = userCountByMonth[i] * (panelHeight - 100) / yAxisMax; // Tính chiều cao thanh
        int x = 50 + i * (barWidth + 10); // Khoảng cách giữa các thanh
        int y = panelHeight - 50 - barHeight;

        g2d.setColor(Color.BLUE);
        g2d.fillRect(x, y, barWidth, barHeight);  // Vẽ thanh biểu đồ

        // Vẽ nhãn tháng dưới mỗi thanh
        g2d.setColor(Color.BLACK);
        g2d.drawString(getMonthName(months[i]), x + (barWidth / 4), panelHeight - 30); // Sử dụng tên tháng (thay vì năm)

        // Hiển thị số lượng người dùng ngay trên đỉnh mỗi thanh
        g2d.setColor(Color.RED);
        g2d.drawString(String.valueOf(userCountByMonth[i]), x + (barWidth / 2) - 10, y - 5);
    }
}


    // Hàm tính giá trị lớn nhất của mảng dữ liệu
    private int getMaxValue(int[] data) {
        int max = 0;
        for (int value : data) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    // Chuyển đổi số tháng thành tên tháng
    private String getMonthName(int month) {
        switch (month) {
            case 1: return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dec";
            default: return "";
        }
    }
}

}

