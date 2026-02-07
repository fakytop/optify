package com.optify.services;


import com.optify.domain.*;
import com.optify.exceptions.DataException;
import com.optify.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class CartService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private StoreProductService storeProductService;
    @Autowired
    private CartSimulationRepository cartSimulationRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartSimulationDetailsRepository cartSimulationDetailsRepository;

    @Transactional(rollbackFor=Exception.class)
    public void addProductToCart(String username, int id, double quant) throws DataException {
        User user = getUserByUsername(username);
        Product product = getProductById(id);
        CartItem cartItem = new CartItem(user.getCart(), product, quant);
        user.addItemToCart(cartItem);
        userRepository.save(user);
    }

    @Transactional(rollbackFor=Exception.class)
    public void removeProductFromCart(String username, int id) throws DataException {
        User user = getUserByUsername(username);
        Product product = getProductById(id);
        user.removeItemFromCart(product);
        userRepository.save(user);
    }

    private User getUserByUsername(String username) throws DataException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(!optionalUser.isPresent()) {
            throw new DataException("[DataException] No existe el nombre de usuario: " + username);
        }
        return optionalUser.get();
    }

    private Product getProductById(int productId) throws DataException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(!optionalProduct.isPresent()) {
            throw new DataException("[DataException] No se encuentra el producto con código: " + productId);
        }
        return optionalProduct.get();
    }

    @Transactional(rollbackFor=Exception.class)
    public List<CartItem> getProductsCart(String username) throws DataException {
        User user = getUserByUsername(username);
        Cart cart = user.getCart();
        return cart.getItems();
    }

    @Transactional(rollbackFor=Exception.class)
    public void addUnitProductCart(String username, int productId) throws DataException {
        User user = getUserByUsername(username);
        CartItem cartItem = getCartItemFromUser(user,productId);
        if(cartItem == null) {
            throw new DataException("No se encontró el producto: {" + productId+ "} para el usuario: {" + username + "}");
        }
        cartItem.setQuant(cartItem.getQuant() + 1);
        userRepository.save(user);
    }

    private CartItem getCartItemFromUser(User user, int id) throws DataException {
        CartItem item = new CartItem(user.getCart().getId(),id);
        return user.getCart().getItem(item);
    }

    @Transactional(rollbackFor=Exception.class)
    public void subtractUnitProductCart(String username, int productId) throws DataException {
        User user = getUserByUsername(username);
        CartItem cartItem = getCartItemFromUser(user,productId);
        if(cartItem == null) {
            throw new DataException("No se encontró el producto: {" + productId + "} para el usuario: {" + username + "}");
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

    private List<Integer> loadProductIdsFromCartItems(List<CartItem> cartItems) {
        List<Integer> productIds = new ArrayList<>();
        for(CartItem cartItem : cartItems) {
            productIds.add(cartItem.getIdProduct());
        }
        return productIds;
    }

    private List<Integer> loadTransactionalProductIds(List<Integer> productIds, List<String> storeNames, HashMap<String,CartSimulation> hashCartValues) {
        List<Integer> productIdsTransactional = new ArrayList<>();
        for(int id : productIds) {
            if(isTransactional(storeNames, id, hashCartValues)) {
                productIdsTransactional.add(id);
            }
        }
        return productIdsTransactional;
    }

    private List<CartSimulation> getFinalCartResults(HashMap<String,CartSimulation> hashCartValues, User user, List<String> storeNames) {
        List<CartSimulation> finalResults = new ArrayList<>();
        CartSimulation optimal = hashCartValues.get("Optimal Cart");
        CartSimulation preferred = hashCartValues.get(user.getPreferredStore().getFantasyName());
        CartSimulation cheapest = getCheapestSupermarket(hashCartValues,storeNames);
        if(preferred == cheapest) {
            cheapest = createAnotherCheapestCartSimulation(cheapest,user);
        }
        if(preferred == null) {
            preferred = new CartSimulation();
            preferred.setName("Super preferido: N/A");
        } else {
            preferred.setName("Super preferido: " + preferred.getName());
        }
        cheapest.setName("Super más barato: " + cheapest.getName());
        finalResults.add(optimal);
        finalResults.add(preferred);
        finalResults.add(cheapest);
        saveDetailsResults(finalResults);
        return finalResults;
    }

    private CartSimulation createAnotherCheapestCartSimulation(CartSimulation cheapest,User user) {
        double totalCartValue = cheapest.getTotalCartValue();
        double totalTransacionalCartValue = cheapest.getTotalTransactionalCartValue();
        HashMap<Integer, CheapestProductInfo> cheapestProducts = cheapest.getCheapestProducts();
        cheapest = new CartSimulation(cheapest.getName(),user,cheapest.getDate());
        cheapest.setTotalCartValue(totalCartValue);
        cheapest.setTotalTransactionalCartValue(totalTransacionalCartValue);
        cheapest.setCheapestProducts(cheapestProducts);
        return cheapest;
    }

    @Transactional(rollbackFor=Exception.class)
    public List<CartSimulation> getCheapestResults(String username) throws DataException {
        User user = getUserByUsername(username);
        List<CartItem> cartItems = user.getCart().getItems();
        if(cartItems == null || cartItems.isEmpty()) {
            throw new DataException("No hay productos cargados en el carrito.");
        }
        List<Integer> productIds = loadProductIdsFromCartItems(cartItems);
        HashMap<String,CartSimulation> hashCartValues = getHashCartValues(productIds,user);
        Set<String> allKeys = hashCartValues.keySet();
        List<String> storeNames = allKeys.stream().filter(key -> !key.equals("Optimal Cart")).toList();
        List<Integer> productIdsTransactional = loadTransactionalProductIds(productIds, storeNames, hashCartValues);
        setTotalValuesToCartSimulation(hashCartValues,storeNames,productIdsTransactional);
        return getFinalCartResults(hashCartValues,user,storeNames);
    }

    private void saveDetailsResults(List<CartSimulation> finalResults) {
        for(CartSimulation cartSimulation : finalResults) {
            for(CheapestProductInfo info :  cartSimulation.getCheapestProducts().values()) {
                CartSimulationDetail detail = new CartSimulationDetail();
                detail.setProduct(info.getProduct());
                detail.setPrice(info.getPrice());
                detail.setTransactional(info.isTransactional());
                String storeString = String.join(", ", info.getStores().stream().map(Store::getFantasyName).toList());
                detail.setStoreNames(storeString);
                cartSimulation.addDetail(detail);
            }
            cartSimulationRepository.save(cartSimulation);
        }
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

    public List<CartItem> getCartItemsByProductId(long productId) {
        if(!cartItemRepository.findByProductId(productId).isEmpty()) {
            return cartItemRepository.findByProductId(productId);
        }
        return null;
    }

    public CartItem getCartItemByPk(CartItemPk cartItemPk) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemPk);
        if(!cartItem.isPresent()) {
            return null;
        }
        return cartItem.get();
    }

    public void addCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    public List<CartSimulationDetail> getCartSimulationDetailsByProductId(int productId) {
        List<CartSimulationDetail> cartSimulationDetails = cartSimulationDetailsRepository.findByProductId(productId);
        if(cartSimulationDetails.isEmpty()) {
            return null;
        }
        return cartSimulationDetails;
    }

    public void addOrUpdateCartSimulationDetail(CartSimulationDetail cartSimulationDetail) {
        cartSimulationDetailsRepository.save(cartSimulationDetail);
    }
}
