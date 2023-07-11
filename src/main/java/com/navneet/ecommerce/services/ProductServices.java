package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.ProductDto;

public interface ProductServices {
	public List<ProductDto> getAllProducts();
	public ProductDto addAProduct(ProductDto dto);
}
