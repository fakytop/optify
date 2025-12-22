package com.optify.services;

import com.optify.domain.Category;
import com.optify.exceptions.DataException;
import com.optify.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category addCategory(Category category) throws DataException {
        if(categoryRepository.findByName(category.getName()).isPresent()) {
            throw new DataException("[DataException] Ya existe la categoría: {" + category.getName() + "}");
        }

        return categoryRepository.save(category);
    }

     public Category updateCategory(Category category) throws DataException {
        if(!categoryRepository.findByName(category.getName()).isPresent()) {
            throw new DataException("[DataException] No existe la categoría con nombre: {" + category.getName() + "}");
        }
        Category updatedCategory = categoryRepository.findByName(category.getName()).get();
        updatedCategory.setName(category.getName());
        updatedCategory.setDescription(category.getDescription());

        return categoryRepository.save(updatedCategory);
     }

     public List<Category> getAllCategories() {
        return categoryRepository.findAll();
     }

     public Category getCategoryByName(String name) {
        if(!categoryRepository.findByName(name).isPresent()) {
            return null;
        }

        return categoryRepository.findByName(name).get();
     }

     public void deleteCategoryByName(String name) throws DataException {
        if(!categoryRepository.findByName(name).isPresent()) {
            throw new DataException("[DataException] No existe la categoría con nombre: {" + name + "}");
        }

        categoryRepository.delete(categoryRepository.findByName(name).get());
     }

     public Category getCategoryById(int id) throws DataException {
        if(!categoryRepository.findById(id).isPresent()) {
            throw new DataException("[DataException] No existe la categoría con id: {" + id + "}");
        }
        return categoryRepository.findById(id).get();
     }
}
