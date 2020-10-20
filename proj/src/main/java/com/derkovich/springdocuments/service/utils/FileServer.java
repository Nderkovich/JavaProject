package com.derkovich.springdocuments.service.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServer {
    private final String filePath = "D:\\Лабы\\Java\\documents\\";

    public FileServer(){};

    public boolean fileUpload(MultipartFile file){
        File newFile = new File(filePath + file.getOriginalFilename());
        if (newFile.exists()){
            return false;
        }
        try (FileOutputStream fos = new FileOutputStream(filePath + file.getOriginalFilename())){
            newFile.createNewFile();
            InputStream inputStream = file.getInputStream();
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                fos.write(bytes, 0, read);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //TODO
    public String getFilePath(String filename){
        return filePath + filename;
    }

    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = Paths.get(getFilePath(fileName));
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

    public void deleteFile(String filename){
        File file = new File(filePath + filename);
        file.delete();
    }
}
