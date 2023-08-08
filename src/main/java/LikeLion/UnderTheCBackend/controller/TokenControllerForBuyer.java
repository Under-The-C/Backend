package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.Service.TokenServiceForBuyer;
import LikeLion.UnderTheCBackend.dto.CreateAccessTokenRequest;
import LikeLion.UnderTheCBackend.dto.CreateAccessTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "index", description = "메인화면 API")
@RestController
@RequestMapping("/")
public class TokenControllerForBuyer {

    private final TokenServiceForBuyer tokenServiceForBuyer;

    @PostMapping("/api/vi/token/buyer")
    @Operation(summary = "토큰", description = "토큰API", responses = {
            @ApiResponse(responseCode = "200", description = "야호! 성공!!!")
    })
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenServiceForBuyer.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
