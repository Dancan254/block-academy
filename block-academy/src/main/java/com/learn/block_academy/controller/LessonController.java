package com.learn.block_academy.controller;

import com.learn.block_academy.dto.LessonCreateRequest;
import com.learn.block_academy.dto.LessonDto;
import com.learn.block_academy.dto.LessonProgressDto;
import com.learn.block_academy.dto.LessonProgressRequest;
import com.learn.block_academy.exception.ResourceNotFoundException;
import com.learn.block_academy.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/{moduleId}")
    public ResponseEntity<LessonDto> addLesson(@PathVariable Long moduleId, @RequestBody LessonCreateRequest lessonRequest){
        try{
            LessonDto lessonDto = lessonService.addLesson(moduleId, lessonRequest);
            return ResponseEntity.ok(lessonDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{lessonId}/progress")
    public ResponseEntity<LessonProgressDto> updateLessonProgress(@PathVariable Long lessonId, @RequestBody LessonProgressRequest progressRequest){
        try{
            LessonProgressDto lessonDto = lessonService.updateLessonProgress(lessonId, progressRequest);
            return ResponseEntity.ok(lessonDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
