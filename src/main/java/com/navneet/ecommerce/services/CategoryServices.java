package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.CategoryDto;

public interface CategoryServices {
	public List<CategoryDto> getAllCategory();
	public CategoryDto addACategory(CategoryDto dto);
}
