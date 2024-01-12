package com.wooyeon.yeon.chat.repository;

import com.wooyeon.yeon.chat.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findByReportUser(Long userId);
}
