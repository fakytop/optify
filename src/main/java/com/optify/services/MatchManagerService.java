package com.optify.services;

import com.optify.domain.ManualMatchPending;
import com.optify.dto.ProductImportDto;
import com.optify.exceptions.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchManagerService {

    @Autowired
    private DataImportService dataImportService;
    @Autowired
    private ManualMatchService manualMatchService;
    @Autowired
    private DiscardReferenceService discardReferenceService;

    @Transactional(rollbackFor = Exception.class)
    public void confirmMatch(int id) throws DataException {
        ManualMatchPending manualMatchPending = manualMatchService.getMatchById(id);
        ProductImportDto dto = new ProductImportDto();
        dto.setIdWeb(manualMatchPending.getIdWeb());
        dto.setUrlProduct(manualMatchPending.getUrlProduct());
        dto.setProductPrice(manualMatchPending.getProductPrice());
        dataImportService.saveStoreProduct(manualMatchPending.getProduct(),manualMatchPending.getStore(),dto);
        manualMatchService.deleteMatch(manualMatchPending.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void declineMatch(int id) throws DataException {
        ManualMatchPending manualMatchPending = manualMatchService.getMatchById(id);
        discardReferenceService.addDiscardedReference(manualMatchPending.getUrlProduct());
        manualMatchService.deleteMatch(manualMatchPending.getId());
    }
}
