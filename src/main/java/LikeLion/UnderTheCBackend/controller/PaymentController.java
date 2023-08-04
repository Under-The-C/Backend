package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.service.PayService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    private PayService payService;

    @PostMapping("/verify/{imp_uid}")
    @Operation(summary = "결제내역 확인", description = "상품이 성공적으로 결제 되었는지 확인하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "결제내역 확인")
    })
    public IamportResponse<Payment> verifyPayment(@PathVariable("imp_uid") String imp_uid) throws IamportResponseException, IOException {
        /* imp_uid 값으로 결제내역 확인 */
        return payService.paymentByImpUid(imp_uid);
    }

    @PostMapping("/complete")
    @Operation(summary = "결제 처리", description = "실제 결제 금액이 일치하는지 확인하고 결제 처리 API", responses = {
            @ApiResponse(responseCode = "200", description = "결제 처리")
    })
    public void completePayment(@RequestBody HashMap<String, Object> body) {
        System.out.println("body = " + body.toString());
    }

    @PostMapping("/web-hook")
    public String webHookCheck() {
        return "success";
    }

}
