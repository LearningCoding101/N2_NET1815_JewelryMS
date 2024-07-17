package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.WorkArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkAreaRepository extends JpaRepository<WorkArea, Long> {
    Optional<WorkArea> findByWorkAreaID(String workAreaID);

}
