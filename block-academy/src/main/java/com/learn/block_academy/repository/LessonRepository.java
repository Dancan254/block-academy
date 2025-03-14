package com.learn.block_academy.repository;

import com.learn.block_academy.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LessonRepository extends JpaRepository<Lesson, Long> {
}