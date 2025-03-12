package com.learn.block_academy.repository;

import com.learn.block_academy.model.Enrollment;
import com.learn.block_academy.model.Lesson;
import com.learn.block_academy.model.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    Optional<LessonProgress> findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);
}
