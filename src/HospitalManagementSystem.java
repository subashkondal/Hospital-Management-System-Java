import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class HospitalManagementSystem {
    private ArrayList<Patient> patients = new ArrayList<>();
    private LinkedList<Appointment> appointments = new LinkedList<>();
    private int patientIdCounter = 1;
    private int appointmentIdCounter = 1;
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        HospitalManagementSystem hms = new HospitalManagementSystem();
        hms.run();
    }

    public void run() {
        while (true) {
            System.out.println("Hospital Management System");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. Schedule Appointment");
            System.out.println("4. View Appointments");
            System.out.println("5. Update Appointment");
            System.out.println("6. Cancel Appointment");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addPatient();
                    break;
                case 2:
                    viewPatients();
                    break;
                case 3:
                    scheduleAppointment();
                    break;
                case 4:
                    viewAppointments();
                    break;
                case 5:
                    updateAppointment();
                    break;
                case 6:
                    cancelAppointment();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void addPatient() {
        System.out.print("Enter patient name: ");
        String name = scanner.next();
        System.out.print("Enter patient age: ");
        int age = scanner.nextInt();
        System.out.print("Enter patient gender: ");
        String gender = scanner.next();

        Patient patient = new Patient(patientIdCounter++, name, age, gender);
        patients.add(patient);
        System.out.println("Patient added successfully.");
    }

    private void viewPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            System.out.println("List of Patients:");
            for (Patient patient : patients) {
                System.out.println(patient);
            }
        }
    }

    private void scheduleAppointment() {
        System.out.print("Enter patient ID: ");
        int patientId = scanner.nextInt();
        Patient patient = findPatientById(patientId);

        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }

        System.out.print("Enter appointment date and time (yyyy-MM-ddTHH:mm): ");
        String dateTimeString = scanner.next();
        LocalDateTime appointmentDateTime = LocalDateTime.parse(dateTimeString);

        Appointment appointment = new Appointment(appointmentIdCounter++, patient, appointmentDateTime);
        appointments.add(appointment);
        System.out.println("Appointment scheduled successfully.");
    }

    private void viewAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            System.out.println("List of Appointments:");
            for (Appointment appointment : appointments) {
                System.out.println(appointment);
            }
        }
    }

    private void updateAppointment() {
        System.out.print("Enter appointment ID: ");
        int appointmentId = scanner.nextInt();
        Appointment appointment = findAppointmentById(appointmentId);

        if (appointment == null) {
            System.out.println("Appointment not found!");
            return;
        }

        System.out.print("Enter new appointment date and time (yyyy-MM-ddTHH:mm): ");
        String dateTimeString = scanner.next();
        LocalDateTime newAppointmentDateTime = LocalDateTime.parse(dateTimeString);

        appointments.remove(appointment);
        appointment = new Appointment(appointment.getAppointmentId(), appointment.getPatient(), newAppointmentDateTime);
        appointments.add(appointment);
        System.out.println("Appointment updated successfully.");
    }

    private void cancelAppointment() {
        System.out.print("Enter appointment ID: ");
        int appointmentId = scanner.nextInt();
        Appointment appointment = findAppointmentById(appointmentId);

        if (appointment == null) {
            System.out.println("Appointment not found!");
            return;
        }

        appointments.remove(appointment);
        System.out.println("Appointment canceled successfully.");
    }

    private Patient findPatientById(int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    private Appointment findAppointmentById(int id) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId() == id) {
                return appointment;
            }
        }
        return null;
    }
}
