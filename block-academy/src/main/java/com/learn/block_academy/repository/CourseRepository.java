package com.learn.block_academy.repository;

import com.learn.block_academy.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String keyword, String keyword1, Pageable pageable);
}