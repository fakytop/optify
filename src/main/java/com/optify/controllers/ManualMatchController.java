package com.optify.controllers;

import com.optify.dto.MatchesPendingDto;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matching")
public class ManualMatchController {
    @Autowired
    private Facade instance;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPendingMatches() {
        List<MatchesPendingDto> matchesPending = instance.getAllPendingMatches()
                .stream().map(MatchesPendingDto::new).toList();
        if(matchesPending == null) {
            return ResponseEntity.badRequest().body("No se encontraron matches pendientes de realizar.");
        }
        return ResponseEntity.ok(matchesPending);
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmMatch(@RequestParam int id) {
        try {
            instance.confirmMatch(id);
            return ResponseEntity.ok("Productos matcheados con éxito.");
        } catch (DataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
