//package coursemaker.coursemaker.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//
//// Todo: implementation 'com.amazonaws:aws-java-sdk-s3:1.12.152' 의존성 주입 필요
//
//@Configuration
//public class S3Config {
//    @Value("${aws.credentials.access-key}")
//    private String accessKey;
//
//    @Value("${aws.credentials.secret-key}")
//    private String secretKey;
//
//    @Value("${aws.region.name}")
//    private String region;
//
//    @Bean
//    public AmazonS3 s3Client() {
//        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
//        return AmazonS3ClientBuilder.standard()
//                .withRegion(region)
//                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                .build();
//    }
//}