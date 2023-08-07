package LikeLion.UnderTheCBackend.service;

import LikeLion.UnderTheCBackend.dto.OrderReq;
import LikeLion.UnderTheCBackend.entity.Order;
import LikeLion.UnderTheCBackend.entity.Product;
import LikeLion.UnderTheCBackend.repository.OrderRepository;
import LikeLion.UnderTheCBackend.repository.ProductRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    @Value("impKey")
    private String impKey;
    @Value("impSecretKey")
    private String impSecret;
    final private IamportClient client = new IamportClient(this.impKey, this.impSecret);
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    @Autowired
    OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public String makeMerchantUid(Integer product_id) throws IamportResponseException, IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd-");
        String merchantUid = formatter.format(date) + UUID.randomUUID();

        Optional<Product> opt = productRepository.findById(product_id);
        Product product = opt.orElseThrow((() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "product_id가 잘못되었습니다.")));

        /** 여기에 상품 수량 확인하는 코드가 필요할 듯 */

        PrepareData prepareData = new PrepareData(merchantUid, product.getPrice());
        client.postPrepare(prepareData);

        Order order = new Order(merchantUid, product.getPrice(), product);
        orderRepository.save(order);

        return merchantUid;
    }

    public IamportResponse<Payment> paymentByImpUid(String imp_uid) throws IamportResponseException, IOException {
        return client.paymentByImpUid(imp_uid);
    }

    public IamportResponse<Payment> completePayment(OrderReq data) throws IamportResponseException, IOException {
        /* imp_uid 값으로 결제내역 확인 */
        IamportResponse<Payment> iRPayment = client.paymentByImpUid(data.getImp_uid());
        Payment payment = iRPayment.getResponse();

        /* 입력받은 merchant_uid와 실제 결제 내역에 merchant_uid를 비교 */
        if (!payment.getMerchantUid().equals(data.getMerchant_uid())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "위조된 결제 시도입니다.");
        }

        /* DB에 저장된 정보를 가져옴 */
        Optional<Order> opt = orderRepository.findByMerchantUid(data.getMerchant_uid());
        Order order = opt.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 merchant_uid 입니다."));

        /* DB에 저장된 가격과 실제 결제된 가격을 비교 */
        if (payment.getAmount() != order.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "위조된 결제 시도입니다.");
        }
        
        order.setStatus("결제완료");
        orderRepository.save(order);

        return iRPayment;
    }


}
