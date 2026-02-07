package com.optify.controllers;

import com.optify.domain.Store;
import com.optify.dto.StoreAddDto;
import com.optify.dto.StoreDto;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {
    @Autowired
    private Facade instance;

    @GetMapping("/getAllStores")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> getAllStores() {
        List<Store> stores = instance.getAllStores();
        if(stores != null && !stores.isEmpty()) {
            List<StoreDto> storesDto = stores.stream().map(StoreDto::new).toList();
            return ResponseEntity.ok(storesDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/addStore")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> addStore(@RequestBody StoreAddDto storeAddDto)  {
        try {
            Store store = new Store(
                    storeAddDto.getRut(),
                    storeAddDto.getName(),
                    storeAddDto.getFantasyName(),
                    storeAddDto.getHomePage()
            );
            instance.addStore(store);
            return ResponseEntity.ok("Supermercado " +
                    store.getFantasyName() +" agregado con éxito.");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/updateStore")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> updateStore(@RequestBody StoreAddDto storeAddDto) {
        try {
            Store store = new Store(
                    storeAddDto.getRut(),
                    storeAddDto.getName(),
                    storeAddDto.getFantasyName(),
                    storeAddDto.getHomePage()
            );
            instance.updateStore(store);
            return ResponseEntity.ok("Datos acualizados.");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteStore")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> deleteStore(@RequestParam long rut) {
        try {
            instance.deleteStore(rut);
            return ResponseEntity.ok("Supermercado eliminado. Rut: {" + rut + "}");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
