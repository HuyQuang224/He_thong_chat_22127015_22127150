import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class OnlFriendList extends JFrame {

    public OnlFriendList() {
        setTitle("Online Friends List");
        setSize(500, 600);
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

        // Online Friend table
        String[] columnNames = {"User Name", "Action"};
        Object[][] data = {
                {"Alice", ""},
                {"Bob", ""},
                {"Charlie", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

        JTable onlineFriendsTable = new JTable(model);
        onlineFriendsTable.setRowHeight(27); 
        onlineFriendsTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        onlineFriendsTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane tableScrollPane = new JScrollPane(onlineFriendsTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Online Friends"));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(tableScrollPane, gbc);

        // Create Group button
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton createGroupButton = new JButton("Create Group");
        actionPanel.add(createGroupButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(actionPanel, gbc);

        add(mainPanel);

        setVisible(true);
    }

    // Custom cell renderer to display button in table
    static class ButtonRenderer extends JPanel implements TableCellRenderer {

        private final JButton chatButton = new JButton("Chat");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(chatButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom cell editor to handle click button in table
    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

        private final JPanel panel = new JPanel();
        private final JButton chatButton = new JButton("Chat");

        public ButtonEditor(JCheckBox checkBox) {
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(chatButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null; // Trả về giá trị nếu cần
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OnlFriendList::new);
    }
}
