package com.learn.block_academy.controller;

import com.learn.block_academy.dto.FileUploadResponse;
import com.learn.block_academy.dto.LessonProgressDto;
import com.learn.block_academy.exception.FileStorageException;
import com.learn.block_academy.service.FileStorageService;
import com.learn.block_academy.service.LessonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final FileStorageService fileStorageService;

    public ContentController(FileStorageService fileStorageService, LessonService lessonService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) throws FileStorageException {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/content/download/")
                .path(fileName)
                .toUriString();

        return ResponseEntity.ok(new FileUploadResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize()));
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws FileNotFoundException {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // default content type
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

//    @PostMapping("/lessons/{lessonId}/progress")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<LessonProgressDto> updateLessonProgress(
//            @PathVariable Long lessonId,
//            @Valid @RequestBody LessonProgressRequest progressRequest) {
//        LessonProgressDto progress = lessonService.updateLessonProgress(lessonId, progressRequest);
//        return ResponseEntity.ok(progress);
//    }
}
