package com.chithanh.italk.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileHandlerService {

  String uploadFile(File file, String fileName);
  String uploadFileCloudinary(File file,String fileName, String folder);
}