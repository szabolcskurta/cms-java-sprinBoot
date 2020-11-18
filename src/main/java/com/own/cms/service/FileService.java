package com.own.cms.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.own.cms.exception.FileStorageException;


@Service
public class FileService {
	

	@Value("${upload.path}")
    private String uploadDir;

    public void uploadFile(MultipartFile file) {

        if (file.isEmpty()) {

            throw new FileStorageException("Failed to store empty file");
        }

        try {
        	Path copyLocation = Paths
                    .get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
                Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {

            String msg = String.format("Failed to store file %f", file.getName());

            throw new FileStorageException(msg, e);
        }
    }
	
}
