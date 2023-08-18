package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
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
    public ResponseEntity<byte[]> downloadImage(@PathVariable("imageName") String imageName
            , HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            log.info("로그인 되어 있지 않습니다.");
            throw new ResponseStatusException(BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }
        return imageService.downloadImage(imageName);
    }

}
