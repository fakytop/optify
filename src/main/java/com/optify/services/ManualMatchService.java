package com.optify.services;

import com.optify.domain.ManualMatchPending;
import com.optify.domain.Store;
import com.optify.exceptions.DataException;
import com.optify.repository.ManualMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public ManualMatchPending getMatchById(int id) throws DataException {
        Optional<ManualMatchPending> optionalManualMatchPending = manualMatchRepository.findById(id);
        if(!optionalManualMatchPending.isPresent()){
            throw new DataException("No se encontró match con id {" + id + "}");
        }
        return optionalManualMatchPending.get();
    }

    public void deleteMatch(int id) {
        manualMatchRepository.deleteById(id);
    }

    public List<ManualMatchPending> getMatchesByProduct(int productId) {
        List<ManualMatchPending> manualMatchPendings = manualMatchRepository.findByProductId(productId);
        if(manualMatchPendings.isEmpty()) {
            return null;
        }
        return manualMatchPendings;
    }

    public void addOrUpdate(ManualMatchPending manualMatchPending) {
        manualMatchRepository.save(manualMatchPending);
    }
}
