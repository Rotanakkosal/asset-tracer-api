package com.kshrd.asset_tracer_api.service.serviceImp;

import com.kshrd.asset_tracer_api.model.response.FileResponse;
import com.kshrd.asset_tracer_api.service.FileService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImp implements FileService {
    @Value("${upload.path}")
    private String uploadPath;
    private Path root;// = Paths.get(uploadPath);

    @PostConstruct // this runs after the constructor
    public void postConstruct() {
        this.root = Paths.get(uploadPath);
    }

//    @Override
//    public String fileUpload(MultipartFile file) throws IOException {
//        try {
//            String fileName = file.getOriginalFilename();   //store file upload
//            assert fileName != null;
//            /***
//             * Generate new uuid file name
//             */
//            fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(fileName);
//            /***
//             * It will create an 'images' directory if not exist
//             */
//            if (!Files.exists(root)) {
//                Files.createDirectories(root);
//            }
//            /***
//             *It will replace if that file name already exist
//             */
//            Files.copy(file.getInputStream(), this.root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
//            return fileName;
//        } catch (Exception e) {
//            throw new IOException("Cannot upload file!");
//        }
//    }

    @Override
    public ResponseEntity<?> getFile(String fileName) throws IOException {
        try {
            // find where the file path is
            Path getFileName = Paths.get(root + "/" + fileName);
            // after found file path, convert to byte
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(getFileName));
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(
                    new InputStreamResource(resource.getInputStream())
            );
        } catch (Exception e) {
            throw new IOException("Cannot found this file");
        }
    }

    @Override
    public String fileUpload(MultipartFile file) throws IOException {
        try {
            String fileName = file.getOriginalFilename();   //store file upload
            assert fileName != null;
            if (
                    fileName.contains(".jpeg") ||
                    fileName.contains(".png") ||
                    fileName.contains(".jpg") ||
                    fileName.contains(".docx") ||
                    fileName.contains(".pptx") ||
                    fileName.contains(".svg") ||
                    fileName.contains(".gif")
            ){
                /***
                 * Generate new uuid file name
                 */
                fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(fileName);
                /***
                 * It will create an 'images' directory if not exist
                 */
                if (!Files.exists(root)) {
                    Files.createDirectories(root);
                }
                /***
                 *It will replace if that file name already exist
                 */
                Files.copy(file.getInputStream(), this.root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            }
        } catch (Exception e) {
            throw new IOException("Cannot upload file!");
        }
        return null;
    }

    @Override
    public FileResponse<?> uploadMultiFile(MultipartFile[] files) throws IOException {
        try {
            var res = new FileResponse<>();
            List<String> fileName = new ArrayList<>();  // store file upload
            /***
             * loop file input
             */
            for (int i = 0; i < files.length; i++) {
                String response = fileUpload(files[i]); // get input file 1 by 1
                if (fileName.contains("Incorrect file")) {
                    res.setStatus("400");
                    res.setMessage("Failed upload file");
                } else {
                    fileName.add(response);
                    res.setStatus("200");
                    res.setMessage("Upload success");
                    res.setPayload(fileName);
                }
            }
            return res;
        } catch (Exception e) {
            throw new IOException("Cannot upload file");
        }
    }

    @Override
    public ResponseEntity<?> getSvgFile(String fileName) throws IOException {
        Path getFileName = Paths.get(root + "/" + fileName);
        String str = null;
        try {
            str = Files.readString(getFileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(str);
    }
}
