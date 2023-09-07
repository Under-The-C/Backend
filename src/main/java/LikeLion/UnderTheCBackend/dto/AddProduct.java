package LikeLion.UnderTheCBackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AddProduct {
    @Schema(description = "상품명")
    private String name;
    @Schema(description = "소제목")
    private String subTitle;
    @Schema(description = "가격")
    private String price;
    @Schema(description = "키워드")
    private List<String> keyword;
    @Schema(description = "설명")
    private String description;
    @Schema(description = "추가 설명")
    private String subDescription;
    @Schema(description = "메인 이미지")
    private MultipartFile mainImage;
    @Schema(description = "상세 이미지")
    private List<MultipartFile> detailImage;
    @Schema(description = "판매 시작일")
    private @DateTimeFormat(pattern = "yyyy-MM-dd") Date saleStartDate;
    @Schema(description = "판매 종료일")
    private @DateTimeFormat(pattern = "yyyy-MM-dd") Date saleEndDate;
    @Schema(description = "카테고리")
    private String category;
}
