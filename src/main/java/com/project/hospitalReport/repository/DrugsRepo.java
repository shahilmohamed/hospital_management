package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.DrugsStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrugsRepo extends JpaRepository<DrugsStock, Long> {

    Optional<DrugsStock> findByName(String name);

    Page<DrugsStock> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
