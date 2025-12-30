package com.optify.services;


import com.optify.domain.CartItem;
import com.optify.domain.Product;
import com.optify.domain.User;
import com.optify.exceptions.DataException;
import com.optify.repository.ProductRepository;
import com.optify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;

    @Transactional(rollbackFor=Exception.class)
    public void addProductToCart(String username, String ean, double quant) throws DataException {
        if(!userRepository.findByUsername(username).isPresent()) {
            throw new DataException("[DataException] No existe el nombre de usuario: " + username);
        }
        if(!productRepository.findByEan(ean).isPresent()) {
            throw new DataException("[DataException] No se encuentra el producto con código: " + ean);
        }
        User user = userRepository.findByUsername(username).get();
        Product product = productRepository.findByEan(ean).get();

        CartItem cartItem = new CartItem(user.getCart(), product, quant);
        user.addItemToCart(cartItem);

        userRepository.save(user);
    }
}
