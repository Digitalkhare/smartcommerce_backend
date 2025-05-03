package smartcommerce.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    public PaymentIntent createPaymentIntent(Long amount) throws Exception {
        Stripe.apiKey = secretKey;

        PaymentIntentCreateParams params =
            PaymentIntentCreateParams.builder()
                .setAmount(amount) // amount in pence
                .setCurrency("gbp")
                .build();

        return PaymentIntent.create(params);
    }
}
