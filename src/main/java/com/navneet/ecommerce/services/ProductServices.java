package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.ProductDto;

public interface ProductServices {
	//Method to fetch all the products from database using pagination
	public List<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize);
	
	//Method to fetch a product according to its id
	public ProductDto getAProduct(Long productId);
	
	//Method to add a product in the database
	public ProductDto addAProduct(ProductDto dto);
}
