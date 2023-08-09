package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.dto.PaymentReq;
import LikeLion.UnderTheCBackend.dto.WebHookJson;
import LikeLion.UnderTheCBackend.entity.Buyer;
import LikeLion.UnderTheCBackend.service.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@Tag(name = "Payment API", description = "결제 API")
public class PaymentController {
    private PaymentService paymentService;
    @Autowired
    PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/merchant_uid")
    @Operation(summary = "주문번호를 생성 API", description = "장바구니에 등록된 상품을 전부 구매하는 주문번호를 등록하고 반환하는 API. 결제 전에 호출해야함.", responses = {
            @ApiResponse(responseCode = "200", description = "주문번호 등록 후 전달 완료"),
            @ApiResponse(responseCode = "404", description = "상품 id가 잘못된 경우")
    })
    public String makeMerchantUid(@Parameter(hidden = true) Session session) throws IamportResponseException, IOException {
        Buyer buyer = (Buyer) session.getAttribute("user");
        return paymentService.makeMerchantUid(buyer);
    }

    @PostMapping("/complete")
    @Operation(summary = "결제 API", description = "상품이 정상적으로 결제 되었는지 확인 후 처리", responses = {
            @ApiResponse(responseCode = "200", description = "결제 완료")
    })
    public IamportResponse<Payment> completeOrder(@Parameter(hidden = true) Session session,
                                                  @RequestBody PaymentReq data) throws IamportResponseException, IOException {
        Buyer buyer = (Buyer) session.getAttribute("user");
        return paymentService.completePayment(buyer, data);
    }

    @PostMapping("/cancel")
    @Operation(summary = "결제 취소 API", description = "결제한 주문을 취소하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "결제 취소 완료")
    })
    public IamportResponse<Payment> cancelOrder(@Parameter(hidden = true) Session session,
                                                PaymentReq data) throws IamportResponseException, IOException {
        Buyer buyer = (Buyer) session.getAttribute("user");
        return paymentService.cancelPayment(buyer, data);
    }

    @PostMapping("/web-hook")
    @Operation(summary = "Iamport WebHook API", description = "Iamport WebHook API", responses = {
            @ApiResponse(responseCode = "200", description = "WebHook 처리 완료")
    })
    public void webHookCheck(@RequestBody WebHookJson data) throws IamportResponseException, IOException {
        paymentService.webHookCheck(data);
    }

}
