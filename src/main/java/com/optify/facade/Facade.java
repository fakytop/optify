package com.optify.facade;

import com.optify.domain.*;
import com.optify.dto.ProductDto;
import com.optify.dto.UserDto;
import com.optify.exceptions.AuthenticationException;
import com.optify.exceptions.DataException;
import com.optify.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    public void register(UserDto userDto) throws AuthenticationException, DataException {
        userService.register(userDto);
    }

    public String logIn(UserDto userDto) throws AuthenticationException {
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

    public Product getProductByEan(String ean) throws DataException {
        return productService.getProductByEan(ean);
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

    public void importProductFromStoreData(ProductDto dto) throws DataException {
        dataImportService.importProductFromStoreData(dto);
    }

    public void importProductsBatch(List<ProductDto> dtos) throws DataException {
        dataImportService.importProductsBatch(dtos);
    }

    public List<Product> getProductsByCategoryId(int categoryId) throws DataException {
        return productService.getProductsByCategoryId(categoryId);
    }

    public List<Product> searchProductsByName(String term) {
        return productService.searchProductsByName(term);
    }

    public void addProductToCart(String username, String ean, double quant) throws DataException {
        cartService.addProductToCart(username, ean, quant);
    }

    public void removeProductFromCart(String username, String ean) throws DataException {
        cartService.removeProductFromCart(username, ean);
    }
}
