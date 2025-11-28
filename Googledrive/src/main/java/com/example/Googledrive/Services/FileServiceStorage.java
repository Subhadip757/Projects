package com.example.Googledrive.Services;

import com.example.Googledrive.Entity.FileEntity;
import com.example.Googledrive.Repo.FileRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceStorage {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FileRepo fileRepo;

    public FileServiceStorage(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }

    public String saveFile(MultipartFile file, Long parentFolderId) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectory((uploadPath));
        }

        // file name
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // meta data for db
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(fileName);
        fileEntity.setPath(filePath.toString());
        fileEntity.setSize(file.getSize());
        fileEntity.setPath("file");
        fileEntity.setParentFolderId(parentFolderId);
        fileEntity.setCreatedAt(LocalDateTime.now());

        fileRepo.save(fileEntity);

        return "File saved successfully";
    }

    public List<FileEntity> getFilesInFolder(Long parentFolderId){
        if(parentFolderId == null){
            return fileRepo.findAll()
                    .stream()
                    .filter(f -> f.getParentFolderId() == null)
                    .collect(Collectors.toList());
        }
        else{
            return fileRepo.findAll()
                    .stream()
                    .filter(f -> parentFolderId.equals(f.getParentFolderId()))
                    .collect(Collectors.toList());
        }
    }

    public FileEntity getFileById(Long id){
        return fileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }

    public void deleteById(Long id){
        fileRepo.deleteById(id);
    }
}

