package com.openlib.market.infrastructure.config;

import com.openlib.market.application.wishlist.AgregarAWishlistInteractor;
import com.openlib.market.application.wishlist.RemoverDeWishlistInteractor;
import com.openlib.market.application.wishlist.VerWishlistInteractor;
import com.openlib.market.domain.wishlist.IListaDeseosGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WishlistConfig {

    @Bean
    public AgregarAWishlistInteractor agregarAWishlistInteractor(IListaDeseosGateway wishlistGateway) {
        return new AgregarAWishlistInteractor(wishlistGateway);
    }

    @Bean
    public RemoverDeWishlistInteractor removerDeWishlistInteractor(IListaDeseosGateway wishlistGateway) {
        return new RemoverDeWishlistInteractor(wishlistGateway);
    }

    @Bean
    public VerWishlistInteractor verWishlistInteractor(IListaDeseosGateway wishlistGateway) {
        return new VerWishlistInteractor(wishlistGateway);
    }
}
