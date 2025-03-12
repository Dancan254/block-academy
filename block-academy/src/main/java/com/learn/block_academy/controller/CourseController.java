package com.learn.block_academy.controller;

import com.learn.block_academy.dto.*;
import com.learn.block_academy.exception.ResourceNotFoundException;
import com.learn.block_academy.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<CourseResponse> getAllCourses(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder,
            @RequestParam(required = false) String keyword) {
        CourseResponse response = courseService.getAllCourses(pageNumber, pageSize, sortBy, sortOrder, keyword);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) throws ResourceNotFoundException {
        CourseDto course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @PostMapping
    //@PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseCreateRequest courseRequest) throws ResourceNotFoundException {
        CourseDto createdCourse = courseService.createCourse(courseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<CourseDto> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseDto courseRequest) throws ResourceNotFoundException {
        CourseDto updatedCourse = courseService.updateCourse(id, courseRequest);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) throws ResourceNotFoundException {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{courseId}/modules")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ModuleDto> addModule(
            @PathVariable Long courseId,
            @Valid @RequestBody ModuleCreateRequest moduleRequest) throws ResourceNotFoundException {
        ModuleDto createdModule = courseService.addModule(courseId, moduleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdModule);
    }

    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentDto> enrollInCourse(@PathVariable Long courseId) throws ResourceNotFoundException {
        EnrollmentDto enrollment = courseService.enrollStudent(courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }
}
