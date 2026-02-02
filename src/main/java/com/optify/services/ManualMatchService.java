package com.optify.services;

import com.optify.domain.ManualMatchPending;
import com.optify.domain.Store;
import com.optify.repository.ManualMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
