import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class RegistrationChart extends JPanel {

    // Sample data: Number of new registrations by month for each year
    private Map<Integer, Map<String, Integer>> yearlyRegistrationData;
    private int selectedYear;

    public RegistrationChart(int selectedYear) {
        // Initialize registration data for the years
        yearlyRegistrationData = new HashMap<>();

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
        yearlyRegistrationData.put(2024, data2024);

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
        yearlyRegistrationData.put(2023, data2023);

        // Default selected year
        this.selectedYear = selectedYear;
    }

    // Draw the chart
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Size of the chart
        int width = getWidth();
        int height = getHeight();

        // Get data for the selected year
        Map<String, Integer> registrationData = yearlyRegistrationData.get(selectedYear);

        // Draw x-axis (Month)
        g2d.drawLine(50, height - 50, width - 50, height - 50); // x-axis (Month)
        g2d.drawLine(50, 50, 50, height - 50); // y-axis (Number of registrations)

        // Draw labels for x-axis and y-axis
        g2d.drawString("Month", width - 40, height - 20); // Label for x-axis
        g2d.drawString("Number of Registrations", 10, 40); // Label for y-axis

        // Draw numbers for y-axis (Number of registrations)
        for (int i = 0; i <= 10; i++) {
            int y = height - 50 - (i * 30); // Tick marks on y-axis
            g2d.drawString(String.valueOf(i * 20), 20, y); // Display the number of registrations
        }

        // Draw data
        int x = 100;
        int barWidth = 50;

        // Draw the bars for each month
        for (int month = 1; month <= 12; month++) {
            String monthName = "Month " + month;
            int registrations = registrationData.get(monthName);

            // Draw the bar for each month
            g2d.setColor(Color.BLUE);
            g2d.fillRect(x, height - 50 - registrations * 3, barWidth, registrations * 3); // Each registration = 3 pixels

            // Draw month labels
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(month), x + 15, height - 30); // Display month 1 to 12

            x += barWidth + 20; // Move x to draw the next bar
        }
    }

    public static void main(String[] args) {
        // Create window to display the chart
        JFrame frame = new JFrame("New Registration Chart");

        // Create JComboBox to select year
        String[] years = {"2023", "2024"};
        JComboBox<String> yearComboBox = new JComboBox<>(years);
        
        // Create panel object and display the chart for the default year
        final RegistrationChart chart = new RegistrationChart(2024);

        // Set event when user selects a year
        yearComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedYear = (String) yearComboBox.getSelectedItem();
                // Update selected year and redraw the chart
                chart.selectedYear = Integer.parseInt(selectedYear);
                chart.repaint();
            }
        });

        // Create JPanel containing the ComboBox and Chart
        JPanel panel = new JPanel();
        panel.add(new JLabel("Year:"));
        panel.add(yearComboBox);

        // Create main JPanel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(panel, BorderLayout.NORTH); // Add JComboBox at the top
        mainPanel.add(chart, BorderLayout.CENTER); // Add chart

        // Set up JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
