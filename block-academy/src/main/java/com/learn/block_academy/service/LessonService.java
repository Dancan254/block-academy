package com.learn.block_academy.service;



import com.learn.block_academy.dto.LessonProgressDto;
import com.learn.block_academy.dto.LessonProgressRequest;
import com.learn.block_academy.exception.ResourceNotFoundException;
import com.learn.block_academy.model.Enrollment;
import com.learn.block_academy.model.Lesson;
import com.learn.block_academy.model.LessonProgress;
import com.learn.block_academy.repository.EnrollmentRepository;
import com.learn.block_academy.repository.LessonProgressRepository;
import com.learn.block_academy.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressRepository lessonProgressRepository;

    @Transactional
    public LessonProgressDto updateLessonProgress(Long lessonId, LessonProgressRequest progressRequest) throws ResourceNotFoundException {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        Enrollment enrollment = enrollmentRepository.findById(progressRequest.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        LessonProgress lessonProgress = lessonProgressRepository.findByEnrollmentAndLesson(enrollment, lesson)
                .orElse(new LessonProgress());

        lessonProgress.setEnrollment(enrollment);
        lessonProgress.setLesson(lesson);
        lessonProgress.setCompleted(progressRequest.isCompleted());
        lessonProgress.setLastAccessDate(LocalDateTime.now());
        lessonProgress.setTimeSpentSeconds(progressRequest.getTimeSpentSeconds());

        lessonProgressRepository.save(lessonProgress);

        return mapToLessonProgressDto(lessonProgress);
    }

    private LessonProgressDto mapToLessonProgressDto(LessonProgress lessonProgress) {
        return LessonProgressDto.builder()
                .id(lessonProgress.getId())
                .enrollmentId(lessonProgress.getEnrollment().getId())
                .lessonId(lessonProgress.getLesson().getId())
                .completed(lessonProgress.isCompleted())
                .lastAccessDate(lessonProgress.getLastAccessDate())
                .timeSpentSeconds(lessonProgress.getTimeSpentSeconds())
                .build();
    }
}
