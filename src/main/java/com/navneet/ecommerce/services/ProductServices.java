package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.ProductDto;

public interface ProductServices {
	//Method to fetch all the products from database using pagination
	public List<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize);
	public ProductDto addAProduct(ProductDto dto);
}
