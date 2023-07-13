package com.navneet.ecommerce.servicesimpl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.navneet.ecommerce.annotations.TrackResponceTime;
import com.navneet.ecommerce.dto.CategoryDto;
import com.navneet.ecommerce.entities.Category;
import com.navneet.ecommerce.repository.CategoryDao;
import com.navneet.ecommerce.services.CategoryServices;

@Service
@TrackResponceTime
public class CategoryServicesImpl implements CategoryServices{
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CategoryDao categoryDao;
	
	//Method to fetch all the categories
	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categoryList = this.categoryDao.findAll();
		List<CategoryDto> dtoList = new ArrayList<>();
		
		for(Category c: categoryList) {
			dtoList.add(this.convertToDto(c));
		}
		return dtoList;
	}
	
	//Method to fetch a category by id
	@Override
	public CategoryDto getCategoryById(Integer categoryId) {
		Category category = this.categoryDao.getReferenceById(categoryId);
		return this.convertToDto(category);
	}

	//Method to add a category entity
	@Override
	public CategoryDto addACategory(CategoryDto dto) {
		Category category = this.convertToCategory(dto);
		this.categoryDao.save(category);
		
		category = this.categoryDao.getReferenceById(dto.getCategoryId());
		dto = this.convertToDto(category);
		return dto;
	}
	
	//Method to update a category entity
	@Override
	public CategoryDto updateACategory(CategoryDto dto, Integer categoryId) {
		Category category = this.categoryDao.getReferenceById(categoryId);
		category.setCategoryName(dto.getCategoryName());
		Category updatedCategory =  this.categoryDao.save(category);
		return this.convertToDto(updatedCategory);
	}
	
	//Method to delete a category entity
	@Override
	public String deleteACategory(Integer categoryId) {
		Category categoryToDelete = this.categoryDao.getReferenceById(categoryId);
		this.categoryDao.delete(categoryToDelete);
		return "Success";
	}

	//Method to convert category-entity to category-dto
	public CategoryDto convertToDto(Category category) {
		CategoryDto dto = this.mapper.map(category, CategoryDto.class);
		return dto;
	}
	
	//Method to convert category-dto to category-entity
	public Category convertToCategory(CategoryDto dto) {
		Category category = this.mapper.map(dto, Category.class);
		return category;
	}
}
