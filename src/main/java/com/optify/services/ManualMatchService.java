package com.optify.services;

import com.optify.domain.ManualMatchPending;
import com.optify.domain.Store;
import com.optify.dto.ProductImportDto;
import com.optify.exceptions.DataException;
import com.optify.repository.ManualMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManualMatchService {

    @Autowired
    private ManualMatchRepository manualMatchRepository;

    public boolean existsByStoreAndIdWeb(Store store, long idWeb) {
        return manualMatchRepository.existsByStoreAndIdWeb(store,idWeb);
    }

    public void addManualMatch(ManualMatchPending manualMatchPending) {
        manualMatchRepository.save(manualMatchPending);
    }

    public List<ManualMatchPending> getAll() {
        return manualMatchRepository.findAll();
    }

    public ManualMatchPending findById(int id) throws DataException {
        if(!manualMatchRepository.findById(id).isPresent()){
            throw new DataException("No se encontró match con id {" + id + "}");
        }
        return manualMatchRepository.findById(id).get();
    }

    public void deleteMatchConfirmed(int id) {
        manualMatchRepository.deleteById(id);
    }

    public List<ManualMatchPending> getMatchesByProduct(int productId) {
        if(manualMatchRepository.findByProductId(productId).isEmpty()) {
            return null;
        }
        return manualMatchRepository.findByProductId(productId);
    }

    public void addOrUpdate(ManualMatchPending manualMatchPending) {
        manualMatchRepository.save(manualMatchPending);
    }
}
