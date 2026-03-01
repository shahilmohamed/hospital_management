package com.project.hospitalReport.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.project.hospitalReport.entity.MedicalHistory;
import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.entity.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PdfService {

    @Autowired
    private HistoryService historyService;

    public byte[] generatePrescriptionPdf(Long historyId) throws Exception {
        MedicalHistory history = historyService.getHistoryById(historyId);
        if (history == null) {
            throw new RuntimeException("Medical history not found");
        }

        Patient patient = history.getPatient();
        Doctor doctor = history.getDoctor();
        List<Prescription> prescriptions = history.getPrescriptions();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Fonts
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Header Section - Doctor Info and Date
        Table headerTable = new Table(2).useAllAvailableWidth();
        
        // Left side - Doctor Info
        Paragraph doctorInfo = new Paragraph()
                .add(new Paragraph("Dr. " + capitalize(doctor.getFirstname()) + " " + capitalize(doctor.getLastname())+"  ")
                        .setFont(boldFont)
                        .setFontSize(12))
                .add(new Paragraph((doctor.getSpecialization() != null ? doctor.getSpecialization().toUpperCase() : "") +
                        " | Reg. No: " + doctor.getId())
                        .setFont(normalFont)
                        .setFontSize(10));
        
        Cell leftCell = new Cell().add(doctorInfo).setBorder(Border.NO_BORDER).setPadding(5);
        headerTable.addCell(leftCell);
        
        // Right side - Contact and Date
        Paragraph rightInfo = new Paragraph()
                .add(new Paragraph("Ph: " + (doctor.getPhoneNumber() != null ? doctor.getPhoneNumber() : "N/A")+"  ")
                        .setFont(normalFont)
                        .setFontSize(10))
                .add(new Paragraph("Date: " + formatDate(history.getDiagnosisDate()))
                        .setFont(normalFont)
                        .setFontSize(10)
                        .setTextAlignment(TextAlignment.RIGHT));
        
        Cell rightCell = new Cell().add(rightInfo).setBorder(Border.NO_BORDER).setPadding(5);
        headerTable.addCell(rightCell);
        
        document.add(headerTable);
        
        // Horizontal line separator
        Div lineDiv = new Div();
        lineDiv.setBorder(new SolidBorder(ColorConstants.BLACK, 0.5f));
        lineDiv.setHeight(0.5f);
        lineDiv.setMarginTop(5);
        lineDiv.setMarginBottom(10);
        document.add(lineDiv);

        // Patient Information Section
        String age = calculateAge(patient.getDob());
        String gender = patient.getGender() != null ? patient.getGender().substring(0, 1).toUpperCase() : "N/A";
        
        // Patient ID, Name, Gender, Age in one line
        Paragraph patientInfo = new Paragraph("ID: " + patient.getId() + " - " +
                capitalize(patient.getFirstname()) + " " + capitalize(patient.getLastname()) +
                " (" + gender + ") / " + age + " Y")
                .setFont(normalFont)
                .setFontSize(11)
                .setMarginBottom(5);
        document.add(patientInfo);
        
        // Address
        if (patient.getAddress() != null && !patient.getAddress().isEmpty()) {
            Paragraph address = new Paragraph("Address: " + capitalize(patient.getAddress()))
                    .setFont(normalFont)
                    .setFontSize(11)
                    .setMarginBottom(5);
            document.add(address);
        }
        
        // Diagnosis
        Paragraph diagnosis = new Paragraph("Diagnosis: * " + history.getDiagnosis())
                .setFont(normalFont)
                .setFontSize(11)
                .setMarginBottom(10);
        document.add(diagnosis);
        
        // Horizontal line separator
        Div lineDiv2 = new Div();
        lineDiv2.setBorder(new SolidBorder(ColorConstants.BLACK, 0.5f));
        lineDiv2.setHeight(0.5f);
        lineDiv2.setMarginTop(5);
        lineDiv2.setMarginBottom(15);
        document.add(lineDiv2);

        // Prescription Title
        Paragraph prescriptionTitle = new Paragraph("PRESCRIPTION")
                .setFont(boldFont)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15);
        document.add(prescriptionTitle);

        // Prescribed Medicines Table
        if (prescriptions != null && !prescriptions.isEmpty()) {
            Table medicineTable = new Table(3).useAllAvailableWidth();
            
            // Header row
            medicineTable.addCell(createHeaderCell("Medicine Name", boldFont));
            medicineTable.addCell(createHeaderCell("Dosage", boldFont));
            medicineTable.addCell(createHeaderCell("Duration", boldFont));

            // Data rows
            for (Prescription prescription : prescriptions) {
                String medicineName = prescription.getStocks() != null ? prescription.getStocks().getName() : "N/A";
                
                // Build dosage string
                StringBuilder dosageBuilder = new StringBuilder();
                if (prescription.getDosageMorning()) {
                    dosageBuilder.append("1 Morning");
                }
                if (prescription.getDosageAfternoon()) {
                    if (dosageBuilder.length() > 0) dosageBuilder.append(", ");
                    dosageBuilder.append("1 Afternoon");
                }
                if (prescription.getDosageNight()) {
                    if (dosageBuilder.length() > 0) dosageBuilder.append(", ");
                    dosageBuilder.append("1 Night");
                }
                String dosageStr = dosageBuilder.length() > 0 ? dosageBuilder.toString() : "-";
                
                // Duration string
                int days = prescription.getDurationDays();
                int totalDoses = 0;
                if (prescription.getDosageMorning()) totalDoses++;
                if (prescription.getDosageAfternoon()) totalDoses++;
                if (prescription.getDosageNight()) totalDoses++;
                int totalQuantity = totalDoses * days;
                String durationStr = days + " days (Total: " + totalQuantity + " doses)";
                
                medicineTable.addCell(createDataCell(medicineName, normalFont));
                medicineTable.addCell(createDataCell(dosageStr, normalFont));
                medicineTable.addCell(createDataCell(durationStr, normalFont));
            }
            document.add(medicineTable);
        } else {
            Paragraph noMedicine = new Paragraph("No medicines prescribed.")
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setMarginBottom(20);
            document.add(noMedicine);
        }

        // Review/Advice
        if (history.getReview() != null && !history.getReview().isEmpty()) {
            Paragraph advice = new Paragraph("Advice Given: * " + history.getReview())
                    .setFont(normalFont)
                    .setFontSize(11)
                    .setMarginTop(15)
                    .setMarginBottom(10);
            document.add(advice);
        }

        // Next Appointment (Revisit Date)
        if (history.getRevisitDate() != null && !history.getRevisitDate().isEmpty()) {
            Paragraph nextAppointment = new Paragraph("Next Appointment: " + history.getRevisitDate())
                    .setFont(normalFont)
                    .setFontSize(11)
                    .setMarginTop(10)
                    .setMarginBottom(20);
            document.add(nextAppointment);
        }

        // Footer - Doctor Signature
        Table footerTable = new Table(1).useAllAvailableWidth();
        
        Paragraph signatureLine = new Paragraph("_________________________")
                .setFont(normalFont)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(5);
        document.add(signatureLine);
        
        Paragraph doctorName = new Paragraph("Dr. " + capitalize(doctor.getFirstname()) + " " + capitalize(doctor.getLastname()))
                .setFont(normalFont)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(3);
        document.add(doctorName);
        
        Paragraph specialization = new Paragraph(doctor.getSpecialization() != null ? doctor.getSpecialization().toUpperCase() : "")
                .setFont(normalFont)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(10);
        document.add(specialization);

        // Generated date at bottom
        Paragraph generatedDate = new Paragraph("Generated on: " + 
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
                .setFont(normalFont)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20);
        document.add(generatedDate);

        document.close();
        return baos.toByteArray();
    }

    private Cell createHeaderCell(String text, PdfFont font) {
        Cell cell = new Cell().add(new Paragraph(text).setFont(font));
        cell.setPadding(8);
        cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setBorder(new SolidBorder(ColorConstants.BLACK, 0.5f));
        return cell;
    }

    private Cell createDataCell(String text, PdfFont font) {
        Cell cell = new Cell().add(new Paragraph(text).setFont(font));
        cell.setPadding(8);
        cell.setBorder(new SolidBorder(ColorConstants.BLACK, 0.5f));
        return cell;
    }

    private String formatDate(java.time.LocalDate date) {
        if (date == null) {
            return "N/A";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date dateObj = java.sql.Date.valueOf(date);
            return sdf.format(dateObj);
        } catch (Exception e) {
            return date.toString();
        }
    }

    private String calculateAge(Date dob) {
        if (dob == null) {
            return "N/A";
        }
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(dob);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return String.valueOf(age);
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
