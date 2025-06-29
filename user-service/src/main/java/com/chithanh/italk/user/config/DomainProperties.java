package com.chithanh.italk.user.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ConfigurationProperties(prefix = "italk-mail")
public class DomainProperties {
    @NotBlank
    private String domain;
}