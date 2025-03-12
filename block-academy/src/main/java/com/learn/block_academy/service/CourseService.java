package com.learn.block_academy.service;

import com.learn.block_academy.dto.*;
import com.learn.block_academy.enums.UserRole;
import com.learn.block_academy.exception.ResourceNotFoundException;
import com.learn.block_academy.model.Course;
import com.learn.block_academy.model.Enrollment;
import com.learn.block_academy.model.Module;
import com.learn.block_academy.model.User;
import com.learn.block_academy.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    private final ModelMapper modelMapper;

    public CourseResponse getAllCourses(int pageNumber, int pageSize, String sortBy, String sortOrder, String keyword) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Course> coursePage;
        if (keyword != null && !keyword.isEmpty()) {
            coursePage = courseRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    keyword, keyword, pageable);
        } else {
            coursePage = courseRepository.findAll(pageable);
        }

        List<CourseDto> courseDTOS = coursePage.stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());

        return new CourseResponse(courseDTOS, coursePage.getNumber(), coursePage.getSize(),
                coursePage.getTotalElements(), coursePage.getTotalPages(), coursePage.isLast());
    }

    public CourseDto getCourseById(Long id) throws ResourceNotFoundException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return mapToCourseDto(course);
    }

    @Transactional
    public CourseDto createCourse(CourseCreateRequest courseRequest) throws ResourceNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User instructor = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Course course = Course.builder()
                .title(courseRequest.getTitle())
                .description(courseRequest.getDescription())
                .instructor(instructor)
                .price(courseRequest.getPrice())
                .level(courseRequest.getLevel())
                .modules(new ArrayList<>())
                .enrollments(new ArrayList<>())
                .build();

        Course savedCourse = courseRepository.save(course);
        return mapToCourseDto(savedCourse);
    }

    @Transactional
    public CourseDto updateCourse(Long id, CourseDto courseRequest) throws ResourceNotFoundException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!course.getInstructor().getEmail().equals(email) /*&&
                !userRepository.findByEmail(email).orElseThrow().getRole().equals(UserRole.ADMIN)*/) {
            throw new IllegalStateException("You don't have permission to update this course");
        }

        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());
        course.setPrice(courseRequest.getPrice());
        course.setLevel(courseRequest.getLevel());

        Course updatedCourse = courseRepository.save(course);
        return mapToCourseDto(updatedCourse);
    }


    @Transactional
    public void deleteCourse(Long id) throws ResourceNotFoundException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!course.getInstructor().getEmail().equals(email) /*&&
                !userRepository.findByEmail(email).orElseThrow().getRole().equals(UserRole.ADMIN)*/) {
            throw new IllegalStateException("You don't have permission to delete this course");
        }

        courseRepository.delete(course);
    }

    @Transactional
    public ModuleDto addModule(Long courseId, ModuleCreateRequest moduleRequest) throws ResourceNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!course.getInstructor().getEmail().equals(email) /*&&
                !userRepository.findByEmail(email).orElseThrow().getRole().equals(UserRole.ADMIN)*/) {
            throw new IllegalStateException("You don't have permission to add modules to this course");
        }

        Module module = Module.builder()
                .title(moduleRequest.getTitle())
                .description(moduleRequest.getDescription())
                .course(course)
                .order(moduleRequest.getOrder())
                .lessons(new ArrayList<>())
                .build();

        Module savedModule = moduleRepository.save(module);
        return mapToModuleDto(savedModule);
    }
    private CourseDto mapToCourseDto(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .level(course.getLevel())
                .instructorName(course.getInstructor().getFullName())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }

    private ModuleDto mapToModuleDto(Module module) {
        return ModuleDto.builder()
                .id(module.getId())
                .title(module.getTitle())
                .description(module.getDescription())
                .order(module.getOrder())
                .lessons(module.getLessons().stream()
                        .map(lesson -> LessonDto.builder()
                                .id(lesson.getId())
                                .title(lesson.getTitle())
                                .content(lesson.getContent())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public EnrollmentDto enrollStudent(Long courseId) throws ResourceNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        Enrollment enrollment = Enrollment.builder()
                .user(student)
                .course(course)
                .enrollmentDate(LocalDateTime.now())
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return EnrollmentDto.builder()
                .id(savedEnrollment.getId())
                .userId(savedEnrollment.getUser().getId())
                .courseId(savedEnrollment.getCourse().getId())
                .enrollmentDate(savedEnrollment.getEnrollmentDate().toString())
                .build();
    }
}