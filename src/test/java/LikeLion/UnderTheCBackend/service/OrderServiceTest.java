package LikeLion.UnderTheCBackend.service;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        basePackages = {"LikeLion.UnderTheCBackend"}
)
class PaymentServiceAppConfig {};

public class OrderServiceTest {

    @Test
    void makeMerchantUid() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(PaymentServiceAppConfig.class);
        OrderService orderService =  ac.getBean("orderService", OrderService.class);
//        System.out.println("merchant_uid = " + paymentService.makeMerchantUid());
    }
}