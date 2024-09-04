import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HospitalManagementSystemGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField nameField, ageField, genderField, appointmentField;
    private JTable patientsTable, appointmentsTable;

    public HospitalManagementSystemGUI() {
        setTitle("Hospital Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createHomePanel(), "Home");
        mainPanel.add(createAddPatientPanel(), "Add Patient");
        mainPanel.add(createScheduleAppointmentPanel(), "Schedule Appointment");
        mainPanel.add(createViewPatientsPanel(), "View Patients");
        mainPanel.add(createViewAppointmentsPanel(), "View Appointments");

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1));
        JButton addPatientButton = new JButton("Add Patient");
        JButton viewPatientsButton = new JButton("View Patients");
        JButton scheduleAppointmentButton = new JButton("Schedule Appointment");
        JButton viewAppointmentsButton = new JButton("View Appointments");

        addPatientButton.addActionListener(e -> cardLayout.show(mainPanel, "Add Patient"));
        viewPatientsButton.addActionListener(e -> cardLayout.show(mainPanel, "View Patients"));
        scheduleAppointmentButton.addActionListener(e -> cardLayout.show(mainPanel, "Schedule Appointment"));
        viewAppointmentsButton.addActionListener(e -> cardLayout.show(mainPanel, "View Appointments"));

        panel.add(addPatientButton);
        panel.add(viewPatientsButton);
        panel.add(scheduleAppointmentButton);
        panel.add(viewAppointmentsButton);

        return panel;
    }

    private JPanel createAddPatientPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        nameField = new JTextField();
        ageField = new JTextField();
        genderField = new JTextField();

        panel.add(new JLabel("name:"));
        panel.add(nameField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderField);

        JButton addPatientButton = new JButton("Add Patient");
        addPatientButton.addActionListener(new AddPatientAction());
        panel.add(addPatientButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        panel.add(backButton);

        return panel;
    }

    private JPanel createScheduleAppointmentPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        appointmentField = new JTextField();

        panel.add(new JLabel("Patient ID:"));
        JTextField patientIdField = new JTextField();
        panel.add(patientIdField);
        panel.add(new JLabel("Appointment (yyyy-MM-dd HH:mm):"));
        panel.add(appointmentField);

        JButton scheduleAppointmentButton = new JButton("Schedule Appointment");
        scheduleAppointmentButton.addActionListener(new ScheduleAppointmentAction(patientIdField));
        panel.add(scheduleAppointmentButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        panel.add(backButton);

        return panel;
    }

    private JPanel createViewPatientsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        patientsTable = new JTable();
        JButton loadButton = new JButton("Load Patients");
        loadButton.addActionListener(e -> {
            try (Connection connection = DatabaseConnection.getConnection()) {
                Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String query = "SELECT * FROM patients";
                ResultSet rs = stmt.executeQuery(query);

                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                String[] columnNames = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    columnNames[i - 1] = rsmd.getColumnName(i);
                }

                rs.last();
                int rowCount = rs.getRow();
                rs.beforeFirst();
                Object[][] data = new Object[rowCount][columnCount];
                int rowIndex = 0;
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        data[rowIndex][i - 1] = rs.getObject(i);
                    }
                    rowIndex++;
                }

                patientsTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        panel.add(new JScrollPane(patientsTable), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(loadButton);
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createViewAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        appointmentsTable = new JTable();
        JButton loadButton = new JButton("Load Appointments");
        loadButton.addActionListener(e -> {
            try (Connection connection = DatabaseConnection.getConnection()) {
                Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String query = "SELECT * FROM appointments";
                ResultSet rs = stmt.executeQuery(query);

                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                String[] columnNames = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    columnNames[i - 1] = rsmd.getColumnName(i);
                }

                rs.last();
                int rowCount = rs.getRow();
                rs.beforeFirst();
                Object[][] data = new Object[rowCount][columnCount];
                int rowIndex = 0;
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        data[rowIndex][i - 1] = rs.getObject(i);
                    }
                    rowIndex++;
                }

                appointmentsTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        panel.add(new JScrollPane(appointmentsTable), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(loadButton);
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Action to add a patient to the database
    private class AddPatientAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String gender = genderField.getText();

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, name);
                ps.setInt(2, age);
                ps.setString(3, gender);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Patient added successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Action to schedule an appointment
    private class ScheduleAppointmentAction implements ActionListener {
        private JTextField patientIdField;

        public ScheduleAppointmentAction(JTextField patientIdField) {
            this.patientIdField = patientIdField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int patientId = Integer.parseInt(patientIdField.getText());
            String appointmentDateTimeStr = appointmentField.getText();
            LocalDateTime appointmentDateTime = LocalDateTime.parse(appointmentDateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO appointments (patient_id, appointment_datetime) VALUES (?, ?)";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, patientId);
                ps.setTimestamp(2, Timestamp.valueOf(appointmentDateTime));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Appointment scheduled successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new HospitalManagementSystemGUI();
    }
}


