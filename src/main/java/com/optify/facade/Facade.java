package com.optify.facade;

import com.optify.domain.*;
import com.optify.dto.ProductImportDto;
import com.optify.dto.UserRegisterDto;
import com.optify.dto.UserLoginDto;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
import com.optify.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Facade {

    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private StoreProductService storeProductService;
    @Autowired
    private DataImportService dataImportService;
    @Autowired
    private CartService cartService;

    private Facade() {}

    // Métodos referidos al usuario.


    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public void register(UserRegisterDto userRegisterDto) throws AuthenticationException, DataException {
        userService.register(userRegisterDto);
    }

    public String logIn(UserLoginDto userDto) throws AuthenticationException {
        return userService.logIn(userDto);
    }

    //Métodos referidos al supermercado
    public Store addStore(Store store) throws DataException {
        return storeService.addStore(store);
    }

    public Store updateStore(Store store) throws DataException {
        return storeService.updateStore(store);
    }

    public List<Store> getAllStores() {
        return storeService.getAllStores();
    }

    public Store getStoreByRut(long rut) throws DataException {
        return storeService.getStoreByRut(rut);
    }

    public void addUrlCategoryByRut(long rut, String category) throws DataException {
        storeService.addUrlCategoryToStore(rut,category);
    }

    public void deleteStore(long rut) throws DataException {
        storeService.deleteStore(rut);
    }

    public Category addCategory(Category category) throws DataException {
        return categoryService.addCategory(category);
    }

    public Category updateCategory(Category category) throws DataException {
        return categoryService.updateCategory(category);
    }

    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    public void deleteCategoryByName(String name) throws DataException {
        categoryService.deleteCategoryByName(name);
    }

    public Category getCategoryByName(String name) throws DataException {
        return categoryService.getCategoryByName(name);
    }

    public Category getCategoryById(int id) throws DataException {
        return categoryService.getCategoryById(id);
    }

    public Product getProductByName(String name) throws DataException {
        return productService.getProductByName(name);
    }

    public StoreProduct saveStoreProduct(StoreProduct storeProduct) throws DataException {
        return storeProductService.addOrUpdateStoreProduct(storeProduct);
    }

    public Product addProduct(Product product) throws DataException {
        return productService.addProduct(product);
    }

    public void importProductFromStoreData(ProductImportDto dto) throws DataException {
        dataImportService.importProductFromStoreData(dto);
    }

    public void importProductsBatch(List<ProductImportDto> dtos) throws DataException {
        dataImportService.importProductsBatch(dtos);
    }

    public Page<Product> getProductsByCategoryId(int categoryId, Pageable pageable) throws DataException {
        return productService.getProductsByCategoryId(categoryId,pageable);
    }

    public Page<Product> searchProductsByName(String term, Pageable pageable) throws DataException {
        return productService.searchProductsByName(term,pageable);
    }

    public void addProductToCart(String username, int id, double quant) throws DataException {
        cartService.addProductToCart(username, id, quant);
    }

    public void removeProductFromCart(String username, int id) throws DataException {
        cartService.removeProductFromCart(username, id);
    }

    public List<CartItem> getProductsCart(String username) throws DataException {
        return cartService.getProductsCart(username);
    }

    public void addUnitProductCart(String username, int id) throws DataException {
        cartService.addUnitProductCart(username,id);
    }

    public void subtractUnitProductCart(String username, int id) throws DataException {
        cartService.subtractUnitProductCart(username,id);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }
}
