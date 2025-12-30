package com.optify.controllers;

import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private Facade instance;

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProductToCart(Authentication auth, @RequestParam String ean, @RequestParam double quant) {

        try {
            String username = auth.getName();
            instance.addProductToCart(username, ean, quant);
            return ResponseEntity.ok("[ADDED] Producto agregado: {Codigo Producto: " + ean + ", Cantidad: "+ quant + "}");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/removeProduct")
    public ResponseEntity<?> removeProductFromCart(Authentication auth, @RequestParam String ean) {
        try {
            String username = auth.getName();
            instance.removeProductFromCart(username,ean);
            return ResponseEntity.ok("[DELETED] Producto: "+ ean + " borrado del carrito.");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
