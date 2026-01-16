package com.optify.services;


import com.optify.domain.Cart;
import com.optify.domain.CartItem;
import com.optify.domain.Product;
import com.optify.domain.User;
import com.optify.exceptions.DataException;
import com.optify.repository.ProductRepository;
import com.optify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;

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
}
