package com.chithanh.italk.file.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ConfigurationProperties(prefix = "gcs")
public class FileServiceConfigProperties {

  @NotBlank private String projectId;

  @NotBlank private String bucketName;

  @NotBlank private String credentialPath;
}