package coursemaker.coursemaker.s3.controller;

import coursemaker.coursemaker.s3.service.ImageUploadService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ImageUploadController {
    private final ImageUploadService imageUploadService;

    public ImageUploadController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @Operation(summary = "이미지 업로드", description = "여러 개 또는 하나의 이미지 파일을 업로드하고, 각 이미지 파일의 URL을 반환합니다.")
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<String> upload(@RequestParam("images") List<MultipartFile> images) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                String imageUrl = imageUploadService.upload(image);
                imageUrls.add(imageUrl);
            }
        }
        return imageUrls;
    }

    @Operation(summary = "이미지 삭제", description = "주어진 이미지 URL에서 파일 이름을 추출하여 이미지를 삭제합니다.")
    @DeleteMapping("/delete")
    public boolean delete(@RequestParam("imageUrl") String imageUrl) {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        return imageUploadService.delete(fileName);
    }
}
