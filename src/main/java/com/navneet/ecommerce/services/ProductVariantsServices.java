package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.ProductVariantDto;

public interface ProductVariantsServices {
	//Method to fetch all the product variants using pagination
	public List<ProductVariantDto> getAllVariants(Integer pageNumber, Integer pageSize);
	public ProductVariantDto addAVariant(ProductVariantDto dto);
}
