package com.optify.services;


import com.optify.domain.*;
import com.optify.exceptions.DataException;
import com.optify.repository.ProductRepository;
import com.optify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class CartService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private StoreProductService storeProductService;

    @Transactional(rollbackFor=Exception.class)
    public void addProductToCart(String username, int id, double quant) throws DataException {
        if(!userRepository.findByUsername(username).isPresent()) {
            throw new DataException("[DataException] No existe el nombre de usuario: " + username);
        }
        if(!productRepository.findById(id).isPresent()) {
            throw new DataException("[DataException] No se encuentra el producto con código: " + id);
        }
        User user = userRepository.findByUsername(username).get();
        Product product = productRepository.findById(id).get();

        CartItem cartItem = new CartItem(user.getCart(), product, quant);
        user.addItemToCart(cartItem);

        userRepository.save(user);
    }

    @Transactional(rollbackFor=Exception.class)
    public void removeProductFromCart(String username, int id) throws DataException {
        if(!userRepository.findByUsername(username).isPresent()) {
            throw new DataException("[DataException] No existe el nombre de usuario: " + username);
        }
        if(!productRepository.findById(id).isPresent()) {
            throw new DataException("[DataException] No se encuentra el producto con código: " + id);
        }
        User user = userRepository.findByUsername(username).get();
        Product product = productRepository.findById(id).get();
        user.removeItemFromCart(product);
        userRepository.save(user);
    }

    @Transactional(rollbackFor=Exception.class)
    public List<CartItem> getProductsCart(String username) throws DataException {
        if(!userRepository.findByUsername(username).isPresent()) {
            throw new DataException("No se encontró el usuario con nombre de usuario {" + username + "}");
        }
        User user = userRepository.findByUsername(username).get();
        Cart cart = user.getCart();
        return cart.getItems();
    }

    @Transactional(rollbackFor=Exception.class)
    public void addUnitProductCart(String username, int id) throws DataException {
        if(!userRepository.findByUsername(username).isPresent()) {
            throw new DataException("No se encontró el usuario con nombre de usuario {" + username + "}");
        }
        User user = userRepository.findByUsername(username).get();
        CartItem item = new CartItem(user.getCart().getId(),id);
        CartItem cartItem = user.getCart().getItem(item);
        if(cartItem == null) {
            throw new DataException("No se encontró el producto: {" + id+ "} para el usuario: {" + username + "}");
        }
        cartItem.setQuant(cartItem.getQuant() + 1);
        userRepository.save(user);
    }

    @Transactional(rollbackFor=Exception.class)
    public void subtractUnitProductCart(String username, int id) throws DataException {
        if(!userRepository.findByUsername(username).isPresent()) {
            throw new DataException("No se encontró el usuario con nombre de usuario {" + username + "}");
        }
        User user = userRepository.findByUsername(username).get();
        CartItem item = new CartItem(user.getCart().getId(),id);
        CartItem cartItem = user.getCart().getItem(item);
        if(cartItem == null) {
            throw new DataException("No se encontró el producto: {" + id + "} para el usuario: {" + username + "}");
        }
        if(cartItem.getQuant() <= 1) {
            throw new DataException("Unidad mínima. No se puede restar.");
        }
        cartItem.setQuant(cartItem.getQuant() - 1);
        userRepository.save(user);
    }

    public HashMap<String,CartSimulation> getHashCartValues(List<Integer> productIds, User user) throws DataException {

        List<StoreProduct> storeProducts = storeProductService.getStoreProductsByProductIds(productIds);
        HashMap<String,CartSimulation> resultsMap = new HashMap<>();
        Timestamp systemDate = new Timestamp(System.currentTimeMillis());
        CartSimulation optimalCart = new CartSimulation("Resultado compra óptima",user, systemDate);

        for(StoreProduct storeProduct : storeProducts) {
            //Controlo si ya creé el carro de la tienda específica.
            if(!resultsMap.containsKey(storeProduct.getStore().getFantasyName())) {
                CartSimulation storeCartSimulation = new CartSimulation(storeProduct.getStore().getFantasyName(),user,systemDate);
                resultsMap.put(storeProduct.getStore().getFantasyName(), storeCartSimulation);
            }
            CartSimulation cartSimulation = resultsMap.get(storeProduct.getStore().getFantasyName());

            CheapestProductInfo productInfo = new CheapestProductInfo(storeProduct.getProduct(),storeProduct.getPrice());
            productInfo.addStore(storeProduct.getStore());
            if(!cartSimulation.getCheapestProducts().containsKey(storeProduct.getProduct().getId())) {
                cartSimulation.getCheapestProducts().put(storeProduct.getProduct().getId(), productInfo);
            }

            optimalCart.saveCheaperStoreProduct(productInfo);


        }
        resultsMap.put("Optimal Cart",optimalCart);
        return resultsMap;
    }

    @Transactional(rollbackFor=Exception.class)
    public List<CartSimulation> getCheapestResults(String username) throws DataException {
        if(!userRepository.findByUsername(username).isPresent()) {
            throw new DataException("No se encontró el usuario con nombre de usuario: {" + username + "}");
        }

        User user = userRepository.findByUsername(username).get();
        List<CartItem> cartItems = user.getCart().getItems();
        if(cartItems == null || cartItems.isEmpty()) {
            throw new DataException("No hay productos cargados en el carrito.");
        }

        List<Integer> productIds = new ArrayList<>();

        for(CartItem cartItem : cartItems) {
            productIds.add(cartItem.getIdProduct());
        }

        HashMap<String,CartSimulation> hashCartValues = getHashCartValues(productIds,user);
        Set<String> allKeys = hashCartValues.keySet();
        List<String> storeNames = allKeys.stream().filter(key -> !key.equals("Optimal Cart")).toList();
        List<Integer> productIdsTransactional = new ArrayList<>();

        for(int id : productIds) {
            if(isTransactional(storeNames, id, hashCartValues)) {
                productIdsTransactional.add(id);
            }
        }

        setTotalValuesToCartSimulation(hashCartValues,storeNames,productIdsTransactional);

        List<CartSimulation> finalResults = new ArrayList<>();
        finalResults.add(hashCartValues.get("Optimal Cart"));
        CartSimulation preferred = hashCartValues.get(user.getPreferredStore().getFantasyName());
        preferred.setName("Super preferido: " + preferred.getName());
        finalResults.add(preferred);
        CartSimulation cheapest = getCheapestSupermarket(hashCartValues,storeNames);
        cheapest.setName("Super más barato: " + cheapest.getName());
        finalResults.add(cheapest);
        return finalResults;
    }

    private CartSimulation getCheapestSupermarket(HashMap<String, CartSimulation> hashCartValues, List<String> storeNames) {
        double cheapestSupermarketValue = Double.MAX_VALUE;
        CartSimulation cheapestCart = null;
        for(String storeName : storeNames) {
            CartSimulation cartSimulation = hashCartValues.get(storeName);
            double total = cartSimulation.getTotalTransactionalCartValue();
            if(total < cheapestSupermarketValue) {
                cheapestSupermarketValue = total;
                cheapestCart = cartSimulation;
            }
        }
        return cheapestCart;
    }

    private void setTotalValuesToCartSimulation(HashMap<String, CartSimulation> hashCartValues, List<String> storeNames, List<Integer> productIdsTransactional) {
        for(String storeName : storeNames) {
            CartSimulation cartSimulation = hashCartValues.get(storeName);
            cartSimulation.setTotalValues(productIdsTransactional);
        }

        CartSimulation optimal = hashCartValues.get("Optimal Cart");
        optimal.setTotalValues(productIdsTransactional);
    }

    private boolean isTransactional(List<String> storeNames, int id, HashMap<String, CartSimulation> hashCartValues) {
        for(String storeName : storeNames) {
            CartSimulation cartStoreSimulation = hashCartValues.get(storeName);
            if(!cartStoreSimulation.getCheapestProducts().containsKey(id)) {
                return false;
            }
        }
        return true;
    }
}
