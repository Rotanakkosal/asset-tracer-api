package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.response.FileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String fileUpload(MultipartFile file) throws IOException;
    ResponseEntity<?> getSvgFile(String fileName) throws IOException;
    ResponseEntity<?> getFile(String fileName) throws IOException;
    FileResponse<?> uploadMultiFile(MultipartFile[] files) throws IOException;
}
