package LikeLion.UnderTheCBackend.service;

import LikeLion.UnderTheCBackend.dto.PaymentRequest;
import LikeLion.UnderTheCBackend.dto.WebHookJson;
import LikeLion.UnderTheCBackend.entity.*;
import LikeLion.UnderTheCBackend.repository.PaymentRepository;
import LikeLion.UnderTheCBackend.repository.ProductRepository;
import LikeLion.UnderTheCBackend.repository.ShoppingHistoryRepository;
import LikeLion.UnderTheCBackend.repository.ShoppingListRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PaymentService {
    private final String impKey;
    private final String impSecret;
    private final IamportClient client;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final ShoppingHistoryRepository shoppingHistoryRepository;
    private final ShoppingListRepository shoppingListRepository;

    @Autowired
    PaymentService(@Value("impKey") String impKey, @Value("impSecretKey") String impSecret, PaymentRepository paymentRepository, ProductRepository productRepository,
                   ShoppingListRepository shoppingListRepository, ShoppingHistoryRepository shoppingHistoryRepository) {
        this.impKey = impKey;
        this.impSecret = impSecret;
        this.client = new IamportClient(this.impKey, this.impSecret);
        this.paymentRepository = paymentRepository;
        this.productRepository = productRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingHistoryRepository = shoppingHistoryRepository;
    }

    @Transactional
    public String makeMerchantUid(User user) throws IamportResponseException, IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd-");
        String merchantUid = formatter.format(date) + UUID.randomUUID();

        List<ShoppingList> list = shoppingListRepository.findByUserId_Id(user.getId());
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user_id가 올바르지 않습니다.");
        }

        BigDecimal priceSum = new BigDecimal("0");
        for (ShoppingList li : list) {
            /* 장바구니에서 꺼낸 정보를 바로 등록하게 바꿔서 아래는 주석 처리 */
            Optional<Product> opt = productRepository.findById(li.getProductId().getId());
            log.info("li.getProductId(): " + li.getProductId().getId());
            Product product = opt.orElseThrow((() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "product_id가 잘못되었습니다.")));

            log.info("product.getPrice() = " + product.getPrice());
            priceSum.add(new BigDecimal(product.getPrice().toString()));
            log.info("priceSum = " + priceSum);

            /* 결제 상품 하나씩 등록 */
            ShoppingHistory shoppingHistory = new ShoppingHistory(li.getUserId(), li.getProductId(), li.getCount(), "결제대기");
            log.info("shoppingHistory = " + shoppingHistory.getUserId().getId() + " " + shoppingHistory.getProductId().getId() + " " + shoppingHistory.getCount() + " " + shoppingHistory.getStatus());
            shoppingHistoryRepository.save(shoppingHistory);

            /* 장바구니에서 상품 지우기 */
//            shoppingListRepository.delete(li);

            /** 여기에 상품 수량 확인하는 코드가 필요할 듯 */
        }

        PrepareData prepareData = new PrepareData(merchantUid, priceSum);
        client.postPrepare(prepareData);

        B_Payment BPayment = new B_Payment(merchantUid, priceSum, user);
        paymentRepository.save(BPayment);

        return merchantUid;
    }

    public boolean checkData(PaymentRequest data) {
//        if
        return true;
    }

    @Transactional
    public IamportResponse<Payment> completePayment(User user, PaymentRequest data) throws IamportResponseException, IOException {
        /* imp_uid 값으로 결제내역 확인 */
        IamportResponse<Payment> iRPayment = client.paymentByImpUid(data.getImp_uid());
        Payment payment = iRPayment.getResponse();

        /* 입력받은 merchant_uid와 실제 결제 내역에 merchant_uid를 비교 */
        if (!payment.getMerchantUid().equals(data.getMerchant_uid())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "위조된 결제 시도입니다.");
        }

        /* DB에 저장된 정보를 가져옴 */
        Optional<B_Payment> opt = paymentRepository.findByMerchantUid(data.getMerchant_uid());
        B_Payment order = opt.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 merchant_uid 입니다."));

        /* 중복 결제 방지 */
        if (!order.getStatus().equals("결제대기")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 결제된 주문입니다.");
        }

        /* DB에 저장된 가격과 실제 결제된 가격을 비교 */
        if (payment.getAmount() != order.getAmount()) {
            /* 결제 취소 */
            client.cancelPaymentByImpUid(new CancelData(data.getImp_uid(), true));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "결제 금액이 잘못 되었습니다.");
        }

        order.setName(data.getName());
        order.setCallNumber(data.getCallNumber());
        order.setAddress(data.getAddress());
        order.setStatus("결제완료");
        paymentRepository.save(order);

        /* 상품 구매내역 저장 */
        List<ShoppingHistory> historyList = shoppingHistoryRepository.findByUserId_Id(user.getId());
        if (historyList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user_id가 올바르지 않습니다.");
        }

        for (ShoppingHistory history : historyList) {
            history.setStatus("결제완료");
            history.setImpUid(data.getImp_uid());
            history.setMerchantUid(data.getMerchant_uid());
            shoppingHistoryRepository.save(history);
        }

        return iRPayment;
    }

    @Transactional
    public IamportResponse<Payment> cancelPayment(User user, PaymentRequest data) throws IamportResponseException, IOException {
        List<ShoppingHistory> historyList = shoppingHistoryRepository.findByUserId_IdAndImpUid(user.getId(), data.getImp_uid());
        if (historyList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "data가 올바르지 않습니다.");
        }

        /* 구매 기록을 전부 결제 취소로 변경 */
        for (ShoppingHistory history : historyList) {
            history.setStatus("결제취소");
            shoppingHistoryRepository.save(history);
        }

        /* B_Payment 상태를 결제취소로 변경 */
        Optional<B_Payment> optOrder = paymentRepository.findByMerchantUid(data.getMerchant_uid());
        B_Payment BPayment = optOrder.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "merchant_uid가 올바르지 않습니다."));
        BPayment.setStatus("결제취소");
        paymentRepository.save(BPayment);

        return client.cancelPaymentByImpUid(new CancelData(data.getImp_uid(), true));
    }

    public void webHookCheck(WebHookJson data) throws IamportResponseException, IOException {
        Optional<B_Payment> OptOrder = paymentRepository.findByMerchantUid(data.getMerchant_uid());
        B_Payment BPayment = OptOrder.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "merchant_uid가 올바르지 않습니다."));

        /* 결제 대기상태인지 확인 후 결제 처리 진행 */
        if (BPayment.getStatus().equals("결제대기") && data.getStatus().equals("paid")) {
            completePayment(BPayment.getUserId(), data);
        }
    }

}
