package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.service.PayService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    private PayService payService;

    @PostMapping("/verify/{imp_uid}")
    @Operation(summary = "결제 API", description = "상품이 성공적으로 결제 되었는지 확인 후 처리", responses = {
            @ApiResponse(responseCode = "200", description = "결제 확인")
    })
    public IamportResponse<Payment> verifyPayment(@PathVariable("imp_uid") String imp_uid) throws IamportResponseException, IOException {
        /* imp_uid 값으로 결제내역 확인 */
        IamportResponse<Payment> res = payService.paymentByImpUid(imp_uid);
        Payment payment = res.getResponse();
        /* 실제 물건 가격과 결제 금액이 일치하는지 확인 */
//        if (payment.getAmount() == 상품 가격)

        /* 주문 금액과 결제 금액이 다르면 */
//        if (orderDTO.getTotalPrice() != Long.parseLong(amount)) {
//            // 결제 취소
//            payService.payMentCancle(token, orderDTO.getImp_uid(), amount,"결제 금액 오류");
//            return res;
//        }
//        orderService.insert_pay(orderDTO);
        return res;
    }

    @PostMapping("/web-hook")
    public String webHookCheck() {
        return "success";
    }

}
