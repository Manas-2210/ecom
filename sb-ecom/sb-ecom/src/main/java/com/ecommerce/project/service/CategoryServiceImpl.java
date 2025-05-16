package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> checkCategory = categoryRepository.findAll();
        if(checkCategory.isEmpty()){
            throw new APIException("No Category present as of now");
        }
        return checkCategory;
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null){
            throw new APIException("Category with the name "+ category.getCategoryName() +" is already exists !!");
        }
        categoryRepository.save(category);
    }

    @Override
    public String removeCategory(Long id) {

        Category category = categoryRepository
                .findById(id).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", id));

        categoryRepository.delete(category);
        return "Category with categoryId "+id+" is deleted successfully";
     }

    @Override
    public Category updateCategory(Long categoryId, Category updatedCategory) {

        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));
        existingCategory.setCategoryName(updatedCategory.getCategoryName());
        Category savedCategory = categoryRepository.save(existingCategory);
        return savedCategory;

    }
}
