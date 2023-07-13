package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.CategoryDto;

public interface CategoryServices {
	//Method to fetch all the categories 
	public List<CategoryDto> getAllCategory();
	
	//Method to fetch a category by id
	public CategoryDto getCategoryById(Integer categoryId);
	
	//Method to add a category entity
	public CategoryDto addACategory(CategoryDto dto);
	
	//Method to update a category entity
	public CategoryDto updateACategory(CategoryDto dto, Integer categoryId);
	
	//Method to delete a category entity
	public String deleteACategory(Integer categoryId);
}
