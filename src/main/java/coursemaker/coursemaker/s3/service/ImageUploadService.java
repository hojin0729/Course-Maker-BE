package coursemaker.coursemaker.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageUploadService {
    private final AmazonS3 s3Client;

    @Autowired
    public ImageUploadService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${aws.s3.bucket}")
    private String bucket;

    // 사진 다중 선택
    public String upload(MultipartFile image) throws IOException {
        if (image.isEmpty()) return null; // 비어 있는 파일 처리

        String originalFilename = image.getOriginalFilename();
        String fileName = changeFileName(originalFilename);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(image.getSize());

        s3Client.putObject(bucket, fileName, image.getInputStream(), objectMetadata);

        return s3Client.getUrl(bucket, fileName).toString();
    }

    public boolean delete(String fileName) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String changeFileName(String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }
}
