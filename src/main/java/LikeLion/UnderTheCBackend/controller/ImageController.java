package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/images")
@Tag(name = "이미지 API", description = "이미지 가져오는 API")
public class ImageController {
    private ImageService imageService;

    @Autowired
    ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(summary = "이미지 조회 ", description = "이미지를 반환합니다.")
    @GetMapping(value = "/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable("imageName") String imageName) throws Exception {
        return imageService.downloadImage(imageName);
    }

}
