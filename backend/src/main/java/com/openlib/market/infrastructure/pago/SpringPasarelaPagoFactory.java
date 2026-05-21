package com.openlib.market.infrastructure.pago;

import com.openlib.market.domain.pago.IPagoExternoGateway;
import com.openlib.market.domain.pago.IPasarelaPagoFactory;
import com.openlib.market.domain.pago.TipoMetodoPago;
import org.springframework.stereotype.Component;

@Component
public class SpringPasarelaPagoFactory implements IPasarelaPagoFactory {

    private final StripeAdapterGateway stripeAdapter;
    private final PayPalAdapterGateway payPalAdapter;
    private final CryptoAdapterGateway cryptoAdapter;
    private final TransferenciaAdapterGateway transferenciaAdapter;

    public SpringPasarelaPagoFactory(
            StripeAdapterGateway stripeAdapter,
            PayPalAdapterGateway payPalAdapter,
            CryptoAdapterGateway cryptoAdapter,
            TransferenciaAdapterGateway transferenciaAdapter) {
        this.stripeAdapter = stripeAdapter;
        this.payPalAdapter = payPalAdapter;
        this.cryptoAdapter = cryptoAdapter;
        this.transferenciaAdapter = transferenciaAdapter;
    }

    @Override
    public IPagoExternoGateway obtenerPasarela(TipoMetodoPago tipo) {
        switch (tipo) {
            case TARJETA:
                return stripeAdapter;
            case PAYPAL:
                return payPalAdapter;
            case CRYPTO:
                return cryptoAdapter;
            case TRANSFERENCIA:
                return transferenciaAdapter;
            default:
                throw new IllegalArgumentException("Método de pago no soportado por la factory: " + tipo);
        }
    }
}
