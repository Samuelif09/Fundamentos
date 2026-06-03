package com.openlib.market.infrastructure.wishlist;

import com.openlib.market.application.wishlist.AgregarAWishlistInteractor;
import com.openlib.market.application.wishlist.RemoverDeWishlistInteractor;
import com.openlib.market.application.wishlist.VerWishlistInteractor;
import com.openlib.market.application.wishlist.WishlistResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios/{idUsuario}/wishlist")
public class WishlistController {

    private final AgregarAWishlistInteractor agregarInteractor;
    private final RemoverDeWishlistInteractor removerInteractor;
    private final VerWishlistInteractor verInteractor;

    public WishlistController(AgregarAWishlistInteractor agregarInteractor,
                              RemoverDeWishlistInteractor removerInteractor,
                              VerWishlistInteractor verInteractor) {
        this.agregarInteractor = agregarInteractor;
        this.removerInteractor = removerInteractor;
        this.verInteractor = verInteractor;
    }

    @PostMapping
    public ResponseEntity<Void> agregarItem(@PathVariable String idUsuario, @RequestBody Map<String, String> body) {
        String isbn = body.get("isbn");
        agregarInteractor.ejecutar(idUsuario, isbn);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<WishlistResponseDto> verWishlist(@PathVariable String idUsuario) {
        WishlistResponseDto wishlist = verInteractor.ejecutar(idUsuario);
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> removerItem(@PathVariable String idUsuario, @PathVariable String isbn) {
        removerInteractor.ejecutar(idUsuario, isbn);
        return ResponseEntity.ok().build();
    }
}
