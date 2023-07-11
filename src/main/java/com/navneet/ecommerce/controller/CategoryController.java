package com.navneet.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.navneet.ecommerce.dto.CategoryDto;
import com.navneet.ecommerce.services.CategoryServices;

@RestController
public class CategoryController {
	@Autowired
	private CategoryServices categoryServices;
	
	@GetMapping("/categories")
	public List<CategoryDto> getCategoryList(){
		return this.categoryServices.getAllCategory();
	}
	
	@PostMapping("/categories")
	public CategoryDto addCategory(@RequestBody CategoryDto dto) {
		return this.categoryServices.addACategory(dto);
	}
}
