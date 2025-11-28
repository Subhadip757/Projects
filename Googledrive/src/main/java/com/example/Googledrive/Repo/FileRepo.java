package com.example.Googledrive.Repo;

import com.example.Googledrive.Entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepo extends JpaRepository<FileEntity, Long> {

}
