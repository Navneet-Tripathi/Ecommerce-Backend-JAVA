package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.ColorDto;
import com.navneet.ecommerce.dto.ProductVariantDto;

public interface ProductVariantsServices {
	//Method to fetch all the product variants using pagination
	public List<ProductVariantDto> getAllVariants(Integer pageNumber, Integer pageSize);
	
	//Method to add a variant 
	public ProductVariantDto addAVariant(ProductVariantDto dto);
	
	//Method to fetch available colors in a product
	public List<ColorDto> getColorOptions(Long productId);
}
