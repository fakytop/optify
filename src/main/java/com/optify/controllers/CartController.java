package com.optify.controllers;

import com.optify.domain.CartItem;
import com.optify.domain.Product;
import com.optify.dto.ProductCartDto;
import com.optify.dto.ProductDto;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private Facade instance;

    @SecurityRequirement(name = "ApiKeyAuth")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/addProduct")
    public ResponseEntity<?> addProductToCart(Authentication auth, @RequestParam int id, @RequestParam double quant) {

        try {
            String username = auth.getName();
            instance.addProductToCart(username, id, quant);
            return ResponseEntity.ok("[ADDED] Producto agregado: {Codigo Producto: " + id + ", Cantidad: "+ quant + "}");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @SecurityRequirement(name = "ApiKeyAuth")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/removeProduct")
    public ResponseEntity<?> removeProductFromCart(Authentication auth, @RequestParam int id) {
        try {
            String username = auth.getName();
            instance.removeProductFromCart(username,id);
            return ResponseEntity.ok("[DELETED] Producto: "+ id + " borrado del carrito.");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @SecurityRequirement(name = "ApiKeyAuth")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/getProductsCart")
    public ResponseEntity<?> getProductsCart(Authentication auth) {
        String username = auth.getName();
        List<CartItem> cartItems = null;
        try {
            cartItems = instance.getProductsCart(username);
            List<ProductCartDto> productCartDtos = cartItems.stream()
                    .map(ProductCartDto::new)
                    .toList();
            return ResponseEntity.ok(productCartDtos);
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @SecurityRequirement(name = "ApiKeyAuth")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/addUnitProductCart")
    public ResponseEntity<?> addUnitProductCart(Authentication auth, @RequestParam int id) {

        String username = auth.getName();
        try {
            instance.addUnitProductCart(username,id);
            return ResponseEntity.ok("[CART] Unidad agregada.");
        } catch (DataException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @SecurityRequirement(name = "ApiKeyAuth")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/subtractUnitProductCart")
    public ResponseEntity<?> subtractUnitProductCart(Authentication auth, @RequestParam int id) {

        String username = auth.getName();
        try {
            instance.subtractUnitProductCart(username,id);
            return ResponseEntity.ok("[CART] Unidad quitada.");
        } catch (DataException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
