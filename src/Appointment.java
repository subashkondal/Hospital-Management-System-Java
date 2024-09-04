import java.time.LocalDateTime;

class Appointment {
    private int appointmentId;
    private Patient patient;
    private LocalDateTime appointmentDateTime;

    public Appointment(int appointmentId, Patient patient, LocalDateTime appointmentDateTime) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.appointmentDateTime = appointmentDateTime;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    @Override
    public String toString() {
        return "Appointment ID: " + appointmentId + ", Patient: [" + patient.toString() + "], Date & Time: " + appointmentDateTime.toString();
    }
}
