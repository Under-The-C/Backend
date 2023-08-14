package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.service.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
@Tag(name = "이미지 API", description = "이미지 가져오는 API")
public class ImageController {
    private ImageService imageService;

    @Autowired
    ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

//    @GetMapping("/download")
//    public  downloadImage() {
//        return imageService.downloadImage();
//    }

}
