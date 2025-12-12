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
            throw new DataException("[DataException] Ya existe una categoría con ese nombre.");
        }

        return categoryRepository.save(category);
    }

     public Category updateCategory(Category category) throws DataException {
        if(!categoryRepository.findByName(category.getName()).isPresent()) {
            throw new DataException("[DataException] No se encontró la categoría seleccionada.");
        }
        Category updatedCategory = categoryRepository.findByName(category.getName()).get();
        updatedCategory.setName(category.getName());
        updatedCategory.setDescription(category.getDescription());

        return categoryRepository.save(updatedCategory);
     }

     public List<Category> getAllCategories() {
        return categoryRepository.findAll();
     }

     public Category getCategoryByName(String name) throws DataException {
        if(!categoryRepository.findByName(name).isPresent()) {
            throw new DataException("[DataException] No existe la categoría.");
        }

        return categoryRepository.findByName(name).get();
     }

     public void deleteCategoryByName(String name) throws DataException {
        if(!categoryRepository.findByName(name).isPresent()) {
            throw new DataException("[DataException] No existe la categoría seleccionada.");
        }

        categoryRepository.delete(categoryRepository.findByName(name).get());
     }
}
