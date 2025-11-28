package com.example.Googledrive.Controller;

import com.example.Googledrive.Entity.FileEntity;
import com.example.Googledrive.Services.FileServiceStorage;
import jakarta.annotation.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {
    private final FileServiceStorage fileServiceStorage;

    public FileController(FileServiceStorage fileServiceStorage) {
        this.fileServiceStorage = fileServiceStorage;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file,
                                             @RequestParam(value = "parentFolderId", required = false)
                                             Long parentFolderId)
    {
        try{
            String response = fileServiceStorage.saveFile(file, parentFolderId);
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body("File upload failed");
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id){
        try{
            // my sql meta data
            FileEntity fileEntity = fileServiceStorage.getFileById(id);
            Path path = Paths.get(fileEntity.getPath());
            UrlResource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .header("content-Disposition", "attachment; filename=\"" + fileEntity.getName() + "\"")
                    .body((Resource) resource);
        }
        catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileEntity>> listFiles(
            @RequestParam(value = "parentFolderId", required = false) Long parentFolderId
    ){
        List<FileEntity> files;
        if(parentFolderId == null){
            files = fileServiceStorage.getFilesInFolder(null);
        }
        else{
            files = fileServiceStorage.getFilesInFolder(parentFolderId);
        }

        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id){
        try{
            FileEntity fileEntity = fileServiceStorage.getFileById(id);
            Path path = Paths.get(fileEntity.getPath());

            Files.deleteIfExists(path);
            fileServiceStorage.deleteById(id);

            return ResponseEntity.ok("File deleted successfully");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to delete file");
        }
    }

}
