package com.derkovich.springdocuments.service.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FileServer {
    private final String filePath = "D:\\Лабы\\Java\\documents\\";

    public FileServer(){};

    public boolean fileUpload(MultipartFile file){
        File newFile = new File(filePath + file.getOriginalFilename());
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileOutputStream fos = new FileOutputStream(filePath + file.getOriginalFilename())){
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
}
