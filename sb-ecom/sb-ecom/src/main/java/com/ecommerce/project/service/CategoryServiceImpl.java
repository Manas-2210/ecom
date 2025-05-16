package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty())
            throw new APIException("No Category present as of now");

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(Category -> modelMapper.map(Category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);

        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category checkCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if(checkCategory != null){
            throw new APIException("Category with the name "+ categoryDTO.getCategoryName() +" is already exists !!");
        }

        //convert DTO to Entity
        Category newCategory = modelMapper.map(categoryDTO, Category.class);

        //Saving Entity
        Category savedCategory = categoryRepository.save(newCategory);

        return modelMapper.map(savedCategory, CategoryDTO.class);

    }

    @Override
    public CategoryDTO removeCategory(Long id) {

        Category category = categoryRepository
                .findById(id).orElseThrow(()-> new  ResourceNotFoundException("Category", "categoryId", id));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
     }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO updatedCategoryDTO) {

        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));

        existingCategory.setCategoryName(updatedCategoryDTO.getCategoryName());
        categoryRepository.save(existingCategory);

        return modelMapper.map(existingCategory, CategoryDTO.class);

    }
}
