package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.model.response.FileResponse;
import com.kshrd.asset_tracer_api.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return BodyResponse.getBodyResponse(fileService.fileUpload(file));
    }
    @GetMapping("/getFile")
    public ResponseEntity<?> getFile(@RequestParam("fileName") String fileName) throws IOException {
        return fileService.getFile(fileName);
    }
    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileResponse<?> uploadMultiFile(@RequestPart(value = "files" , required = false) MultipartFile[] files) throws IOException {
        return fileService.uploadMultiFile(files);
    }

    @GetMapping("/getSvgFile")
    public ResponseEntity<?> getSvgFile(@RequestParam("getSvgFile") String fileName) throws IOException {
        return fileService.getSvgFile(fileName);
    }
}
