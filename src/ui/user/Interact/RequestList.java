import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class RequestList extends JFrame {

    public RequestList() {
        setTitle("Friend Request List");
        setSize(700, 600);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Back button
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(backButton, gbc);

        // Friend Request table
        String[] columnNames = {"User Name", "Request Status", "Actions"};
        Object[][] data = {
                {"Alice", "Pending", ""},
                {"Bob", "Pending", ""},
                {"Charlie", "Pending", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Chỉ cột Actions có thể chỉnh sửa
            }
        };

        JTable requestTable = new JTable(model);
        requestTable.setRowHeight(27);
        requestTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        requestTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane tableScrollPane = new JScrollPane(requestTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Friend Requests"));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(tableScrollPane, gbc);

        add(mainPanel);

        setVisible(true);
    }

    // Custom cell renderer to display button in table
    static class ButtonRenderer extends JPanel implements TableCellRenderer {

        private final JButton acceptButton = new JButton("Accept");
        private final JButton declineButton = new JButton("Decline");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(acceptButton);
            add(declineButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom cell editor to handle click button in table
    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

        private final JPanel panel = new JPanel();
        private final JButton acceptButton = new JButton("Accept");
        private final JButton declineButton = new JButton("Decline");

        public ButtonEditor(JCheckBox checkBox) {
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(acceptButton);
            panel.add(declineButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RequestList::new);
    }
}
