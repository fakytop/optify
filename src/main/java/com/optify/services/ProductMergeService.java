package com.optify.services;

import com.optify.domain.*;
import com.optify.exceptions.DataException;
import com.optify.repository.ManualMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProductMergeService {

    @Autowired
    private ProductService productService;
    @Autowired
    private StoreProductService storeProductService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ManualMatchService manualMatchService;

    @Transactional
    public void mergeProducts(int keepProductId, int suprProductId) throws DataException {
        Product keepProduct = productService.getProductById(keepProductId);
        Product suprProduct = productService.getProductById(suprProductId);
        List<StoreProduct> storeProductsToDelete = storeProductService.getStoreProductsByProductId(suprProductId);
        createNewStoreProducts(storeProductsToDelete,keepProduct);
        createOrUpdateCartItem(suprProductId,keepProduct);
        updateManualMatchesPending(suprProductId,keepProduct);
        updateCartSimulationDetails(suprProductId,keepProduct);
        productService.flush();
        productService.deleteProduct(suprProduct);
    }

    private void updateCartSimulationDetails(int productId, Product product) {
        List<CartSimulationDetail> simDetails = cartService.getCartSimulationDetailsByProductId(productId);
        if(simDetails != null) {
            for(CartSimulationDetail sim : simDetails) {
                sim.setProduct(product);
                cartService.addOrUpdateCartSimulationDetail(sim);
            }
        }
    }

    private void updateManualMatchesPending(int productId, Product product) {
        List<ManualMatchPending> matches = manualMatchService.getMatchesByProduct(productId);
        if(matches != null && !matches.isEmpty()) {
            for(ManualMatchPending match : matches) {
                match.setProduct(product);
                manualMatchService.addOrUpdate(match);
            }
        }
    }

    private void createOrUpdateCartItem(int productId, Product product) {
        List<CartItem> cartItemsToDelete = cartService.getCartItemsByProductId(productId);
        if(cartItemsToDelete != null) {
            for(CartItem cartItem : cartItemsToDelete) {
                Cart cart = cartItem.getCart();
                double quant = cartItem.getQuant();
                CartItem ct = new CartItem(cart,product,quant);
                CartItem existsCi = cartService.getCartItemByPk(ct.getId());
                if(existsCi != null) {
                    existsCi.setQuant(existsCi.getQuant() + quant);
                    cartService.addCartItem(existsCi);
                } else {
                    cartService.addCartItem(ct);
                }
                cartService.deleteCartItem(cartItem);
            }
        }
    }

    private void createNewStoreProducts(List<StoreProduct> storeProducts, Product product) {
        for(StoreProduct storeProduct : storeProducts) {
            Store store = storeProduct.getStore();
            double price = storeProduct.getPrice();
            long idWeb = storeProduct.getIdWeb();
            String urlWeb = storeProduct.getUrlProduct();
            StoreProduct sp = new StoreProduct();
            sp.setProduct(product);
            sp.setStore(store);
            sp.setIdWeb(idWeb);
            sp.setPrice(price);
            sp.setUrlProduct(urlWeb);
            storeProductService.addOrUpdateStoreProduct(sp);
            storeProductService.deleteStoreProduct(storeProduct);
        }
    }
}
