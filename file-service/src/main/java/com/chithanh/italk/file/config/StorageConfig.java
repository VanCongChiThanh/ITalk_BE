package com.chithanh.italk.file.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class StorageConfig {
  private final FileServiceConfigProperties properties;

  @Bean
  public Storage storage() throws IOException {
    return StorageOptions.newBuilder()
        .setProjectId(properties.getProjectId())
        .setCredentials(
            GoogleCredentials.fromStream(new FileInputStream(properties.getCredentialPath())))
        .build()
        .getService();
  }
}