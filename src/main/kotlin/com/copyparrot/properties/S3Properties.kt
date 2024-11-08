package com.dopaminedefense.dodiserver.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "s3")
class S3Properties (
    var accessKey: String,
    var secretKey: String,
    var region: String,
    var url: String
)