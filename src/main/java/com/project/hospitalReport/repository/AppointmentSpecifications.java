package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.Appointment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class AppointmentSpecifications {

    public static Specification<Appointment> findByDiagnosisDateDoctorIdAndIsConsultedTrueAndNameContaining(LocalDate date, Long doctorId, String search) {
        return (root, query, criteriaBuilder) -> {
            Predicate datePredicate = criteriaBuilder.equal(root.get("diagnosisDate"), date);
            Predicate doctorPredicate = criteriaBuilder.equal(root.get("doctor").get("id"), doctorId);
            Predicate consultedPredicate = criteriaBuilder.equal(root.get("isConsulted"), true);
            
            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate firstnamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(root.get("firstname")), searchPattern);
            Predicate lastnamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(root.get("lastname")), searchPattern);
            Predicate namePredicate = criteriaBuilder.or(firstnamePredicate, lastnamePredicate);
            
            return criteriaBuilder.and(datePredicate, doctorPredicate, consultedPredicate, namePredicate);
        };
    }

    public static Specification<Appointment> findByDiagnosisDateDoctorIdAndIsConsultedFalseAndNameContaining(LocalDate date, Long doctorId, String search) {
        return(root, query, criteriaBuilder) -> {
            Predicate datePredicate = criteriaBuilder.equal(root.get("diagnosisDate"), date);
            Predicate doctorPredicate = criteriaBuilder.equal(root.get("doctor").get("id"), doctorId);
            Predicate consultedPredicate = criteriaBuilder.equal(root.get("isConsulted"),false);

            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate firstnamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("firstname")), searchPattern);
            Predicate lastnamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("lastname")), searchPattern);
            Predicate namePredicate = criteriaBuilder.or(firstnamePredicate, lastnamePredicate);

            return criteriaBuilder.and(datePredicate, doctorPredicate, consultedPredicate, namePredicate);
        };
    }
}

