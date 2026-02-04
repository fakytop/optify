package com.optify.services;

import com.optify.domain.Category;
import com.optify.exceptions.DataException;
import com.optify.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
         Optional<Category> optionalCategory = categoryRepository.findByName(category.getName());
        if(!optionalCategory.isPresent()) {
            throw new DataException("[DataException] No existe la categoría con nombre: {" + category.getName() + "}");
        }
        Category updatedCategory = optionalCategory.get();
        updatedCategory.setName(category.getName());
        updatedCategory.setDescription(category.getDescription());
        return categoryRepository.save(updatedCategory);
     }

     public List<Category> getAllCategories() {
        return categoryRepository.findAll();
     }

     public Category getCategoryByName(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);
        if(!optionalCategory.isPresent()) {
            return null;
        }
        return optionalCategory.get();
     }

     public void deleteCategoryByName(String name) throws DataException {
        if(!categoryRepository.findByName(name).isPresent()) {
            throw new DataException("[DataException] No existe la categoría con nombre: {" + name + "}");
        }
        categoryRepository.delete(categoryRepository.findByName(name).get());
     }

     public Category getCategoryById(int id) throws DataException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(!optionalCategory.isPresent()) {
            throw new DataException("[DataException] No existe la categoría con id: {" + id + "}");
        }
        return optionalCategory.get();
     }
}
