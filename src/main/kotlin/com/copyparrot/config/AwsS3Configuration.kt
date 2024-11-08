package com.copyparrot.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.dopaminedefense.dodiserver.common.properties.S3Properties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsS3Configuration (
    val s3Properties: S3Properties
) {

    @Bean
    fun amazonS3() : AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withRegion(s3Properties.region)
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(s3Properties.accessKey, s3Properties.secretKey)))
            .build()
    }
}