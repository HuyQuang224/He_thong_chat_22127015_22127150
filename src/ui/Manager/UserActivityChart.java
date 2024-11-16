import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserActivityChart extends JPanel {

    // Sample data: Number of users active per month in each year
    private Map<Integer, Map<String, Integer>> yearlyActivityData;
    private int selectedYear;

    public UserActivityChart(int selectedYear) {
        // Create activity data for each year
        yearlyActivityData = new HashMap<>();

        // Data for 2024
        Map<String, Integer> data2024 = new HashMap<>();
        data2024.put("Month 1", 20);
        data2024.put("Month 2", 40);
        data2024.put("Month 3", 30);
        data2024.put("Month 4", 50);
        data2024.put("Month 5", 60);
        data2024.put("Month 6", 70);
        data2024.put("Month 7", 80);
        data2024.put("Month 8", 90);
        data2024.put("Month 9", 100);
        data2024.put("Month 10", 110);
        data2024.put("Month 11", 120);
        data2024.put("Month 12", 130);
        yearlyActivityData.put(2024, data2024);

        // Data for 2023
        Map<String, Integer> data2023 = new HashMap<>();
        data2023.put("Month 1", 15);
        data2023.put("Month 2", 25);
        data2023.put("Month 3", 40);
        data2023.put("Month 4", 45);
        data2023.put("Month 5", 55);
        data2023.put("Month 6", 65);
        data2023.put("Month 7", 75);
        data2023.put("Month 8", 85);
        data2023.put("Month 9", 95);
        data2023.put("Month 10", 105);
        data2023.put("Month 11", 115);
        data2023.put("Month 12", 125);
        yearlyActivityData.put(2023, data2023);

        // Default selected year
        this.selectedYear = selectedYear;
    }

    // Draw the chart
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Chart size
        int width = getWidth();
        int height = getHeight();

        // Get the data for the selected year
        Map<String, Integer> activityData = yearlyActivityData.get(selectedYear);

        // Calculate the scaling factor for the bar heights (proportional to the number of activities)
        int maxValue = activityData.values().stream().max(Integer::compareTo).get();
        int scale = (height - 100) / maxValue; // Scale the height of the chart to the largest value

        // Draw x-axis (Months)
        g2d.drawLine(50, height - 50, width - 50, height - 50); // X axis (Months)
        g2d.drawLine(50, 50, 50, height - 50); // Y axis (Number of users active)

        // Draw unit labels for x and y axes
        g2d.drawString("Month", width - 40, height - 20); // Label for x-axis
        g2d.drawString("Numbers of activity", 10, 40); // Label for y-axis

        // Draw the title
        g2d.drawString("User Activity Chart by Month - " + selectedYear, width / 4, 30);

        // Draw numbers on the y-axis (Number of active users)
        for (int i = 0; i <= maxValue; i += maxValue / 5) {
            int yPosition = height - 50 - (i * scale);
            g2d.drawString(String.valueOf(i), 20, yPosition);
        }

        // Draw the data
        int x = 100;
        int barWidth = 50;

        // Draw bars for each month
        for (int month = 1; month <= 12; month++) {
            String monthName = "Month " + month;
            int activityCount = activityData.get(monthName);

            // Draw a bar for each month
            g2d.setColor(Color.GREEN);
            g2d.fillRect(x, height - 50 - activityCount * scale, barWidth, activityCount * scale); // Each active user = scale pixels

            // Draw month labels
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(month), x + 15, height - 30); // Display months 1 to 12

            x += barWidth + 20; // Move x to draw the next bar
        }
    }

    public static void main(String[] args) {
        // Create a window to display the chart
        JFrame frame = new JFrame("User Activity Chart");

        // Create a JComboBox to select the year
        String[] years = {"2023", "2024"};
        JComboBox<String> yearComboBox = new JComboBox<>(years);

        // Create a panel to display the chart for the default year
        final UserActivityChart chart = new UserActivityChart(2024);

        // Set up an event listener for when the user selects a year
        yearComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedYear = (String) yearComboBox.getSelectedItem();
                // Update the selected year and repaint the chart
                chart.selectedYear = Integer.parseInt(selectedYear);
                chart.repaint();
            }
        });

        // Create a panel to hold the ComboBox and the chart
        JPanel panel = new JPanel();
        panel.add(new JLabel("Select year:"));
        panel.add(yearComboBox);

        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(panel, BorderLayout.NORTH); // Add the ComboBox at the top
        mainPanel.add(chart, BorderLayout.CENTER); // Add the chart in the center

        // Set up the JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
