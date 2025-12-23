package com.optify.dto;

import com.optify.domain.Category;

public class CategoryDto {
    private int categoryId;
    private String categoryName;

    public CategoryDto() {
    }

    public CategoryDto(Category category) {
        this.categoryId =  category.getId();
        this.categoryName = category.getName();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
