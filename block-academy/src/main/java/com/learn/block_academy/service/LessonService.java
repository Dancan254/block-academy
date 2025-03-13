package com.learn.block_academy.service;

import com.learn.block_academy.dto.LessonCreateRequest;
import com.learn.block_academy.dto.LessonDto;
import com.learn.block_academy.dto.LessonProgressDto;
import com.learn.block_academy.dto.LessonProgressRequest;
import com.learn.block_academy.exception.ResourceNotFoundException;
import com.learn.block_academy.model.Enrollment;
import com.learn.block_academy.model.Lesson;
import com.learn.block_academy.model.LessonProgress;
import com.learn.block_academy.model.Module;
import com.learn.block_academy.repository.EnrollmentRepository;
import com.learn.block_academy.repository.LessonProgressRepository;
import com.learn.block_academy.repository.LessonRepository;
import com.learn.block_academy.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final ModuleRepository moduleRepository;
    private final ModelMapper modelMapper;

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

        return modelMapper.map(lessonProgress, LessonProgressDto.class);
    }

    @Transactional
    public LessonDto addLesson(Long moduleId, LessonCreateRequest lessonRequest) throws ResourceNotFoundException {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + moduleId));

        Lesson lesson = Lesson.builder()
                .title(lessonRequest.getTitle())
                .content(lessonRequest.getContent())
                .order(lessonRequest.getOrder())
                .module(module)
                .build();

        Lesson savedLesson = lessonRepository.save(lesson);
        return modelMapper.map(savedLesson, LessonDto.class);
    }
}