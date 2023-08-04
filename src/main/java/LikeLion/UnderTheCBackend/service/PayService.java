package LikeLion.UnderTheCBackend.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.HashMap;

@Service
public class PayService {

    @Value("impKey")
    private String impKey;

    @Value("impSecretKey")
    private String impSecret;
    final private IamportClient client = new IamportClient(this.impKey, this.impSecret);

    public IamportResponse<Payment> paymentByImpUid(String imp_uid) throws IamportResponseException, IOException {
        return client.paymentByImpUid(imp_uid);
    }

    public void completePayment(HashMap<String, Object> body) {

    }


}
