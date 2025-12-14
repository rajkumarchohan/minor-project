package com.minorProject.carShowcase.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ColoudinaryConfig {
    @Value("${config.cloudinary.cloud.name}")
    private String cloud_name;
    @Value("${config.cloudinary.cloud.key}")
    private String cloudKey;
    @Value("${config.cloudinary.cloud.secret}")
    private String cloudSecret;
    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name",cloud_name,
                        "api_key" , cloudKey,
                        "api_secret" , cloudSecret
                )
        );
    }


}
