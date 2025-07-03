package com.chithanh.italk.file.service.impl;

import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.BadRequestException;
import com.chithanh.italk.file.config.FileServiceConfigProperties;
import com.chithanh.italk.file.service.FileHandlerService;
import com.chithanh.italk.file.util.FileUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@EnableConfigurationProperties(FileServiceConfigProperties.class)
public class FileHandlerServiceImpl implements FileHandlerService {

  private final FileServiceConfigProperties properties;
  private final Storage storage;
  private final Cloudinary cloudinary;

  @Override
  public String uploadFile(File file, String fileName) {
    String fileUrl = "";
    try {
      String mimeType = FileUtils.getMineType(file);
      if (this.checkMimeType(mimeType)) {
        fileName = FileUtils.generateFileName(fileName);
        fileUrl = this.uploadGCS(fileName, file);
      } else {
        FileUtils.deleteFile(file);
        throw new BadRequestException(MessageConstant.FILE_NOT_FORMAT);
      }
    } catch (BadRequestException ex) {
      throw ex;
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    return fileUrl;
  }

  @Override
  public String uploadFileCloudinary(File file, String fileName,String folder) {
    String fileUrl = "";
    try {
      String mimeType = FileUtils.getMineType(file);
      if (this.checkMimeType(mimeType)) {
        fileName = FileUtils.generateFileName(fileName);
        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "public_id", fileName,
                "folder", folder,
                "resource_type", "auto"
        );
        Map uploadResult = cloudinary.uploader().upload(file, uploadParams);
        fileUrl = (String) uploadResult.get("secure_url");
      } else {
        throw new BadRequestException(MessageConstant.FILE_NOT_FORMAT);
      }
    } catch (BadRequestException ex) {
      throw ex;
    } catch (Exception e) {
      log.error("Unexpected error during Cloudinary upload: {}", e.getMessage());
    }
    return fileUrl;
  }

  private boolean checkMimeType(String mimeType) {
    return mimeType.startsWith("application")
        || mimeType.equals("application/msword")
        || mimeType.startsWith("image")
        || mimeType.startsWith("video")
        || mimeType.startsWith("audio")
        || mimeType.equals("text/plain")
        || mimeType.equals("text/html")
        || mimeType.equals("text/csv");
  }

  public String uploadGCS(String fileName, File file) throws IOException {
    BlobId blobId = BlobId.of(properties.getBucketName(), fileName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
    Blob blob = storage.create(blobInfo, Files.readAllBytes(Paths.get(file.getPath())));
    return blob.getMediaLink();
  }
}