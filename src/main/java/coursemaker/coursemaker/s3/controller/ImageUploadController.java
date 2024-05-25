package coursemaker.coursemaker.s3.controller;

import coursemaker.coursemaker.s3.service.ImageUploadService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImageUploadController {
    private final ImageUploadService imageUploadService;

    public ImageUploadController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("images") List<MultipartFile> images, Model model, HttpServletRequest request) throws IOException {
        // 1. 세션에서 이미지 URL 목록을 가져옴
        // 2. 없으면 새로운 리스트를 생성함.
        List<String> imageUrls = (List<String>) request.getSession().getAttribute("imageUrls");
        if (imageUrls == null) {
            imageUrls = new ArrayList<>();
        }
        // 업로드된 이미지를 반복 처리함.
        for (MultipartFile image : images) {
            // 이미지가 비어있지 않다면 업로드 진행함.
            if (!image.isEmpty()) {
                String imageUrl = imageUploadService.upload(image);
                // 업로드 된 이미지의 Url을 목록에 추가함.
                imageUrls.add(imageUrl);
            }
        }
        // 업데이트된 이미지 Url 목록을 세션에 저장함.
        request.getSession().setAttribute("imageUrls", imageUrls);
        // 모델에 이미지 Url 목록을 추가하여 뷰에서 보여짐.
        model.addAttribute("imageUrls", imageUrls);
        // 이미지 추가 상태 메시지 추가
        model.addAttribute("status", "added");
        return "image";
    }

    @GetMapping("/upload")
    public String uploadForm(Model model, HttpServletRequest request) {
        // 세션에서 이미지 URL 목록을 가져와 모델에 추가
        // 그리고 폼 페이지에서 업로드 된 이미지 목록을 보여줌.
        List<String> imageUrls = (List<String>) request.getSession().getAttribute("imageUrls");
        model.addAttribute("imageUrls", imageUrls);
        return "index";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("url") String imageUrl, HttpServletRequest request, Model model) {
        // Url에서 파일 이름을 추출합니다.
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        // 파일 이름을 사용하여 S3에서 해당 이미지를 삭제함.
        boolean isDeleted = imageUploadService.delete(fileName);
        List<String> imageUrls = (List<String>) request.getSession().getAttribute("imageUrls");
        if (isDeleted && imageUrls != null) {
            // 삭제가 성공하면 이미지 URL 목록에서 해당 URL을 제거함.
            imageUrls.remove(imageUrl);
            // 삭제 성공 메시지를 모델에 추가하고
            model.addAttribute("status", "deleted");
        } else {
            // 삭제 실패시 에러 메세지
            model.addAttribute("status", "error");
        }
        // 업데이트된 이미지 URL 목록을 모델에 추가
        model.addAttribute("imageUrls", imageUrls);
        return "image";
    }
}


