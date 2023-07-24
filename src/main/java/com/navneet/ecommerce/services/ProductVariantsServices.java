package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.ColorDto;
import com.navneet.ecommerce.dto.ProductVariantDto;
import com.navneet.ecommerce.dto.SizeDto;

public interface ProductVariantsServices {
	//Method to fetch all the product variants using pagination
	public List<ProductVariantDto> getAllVariants(Integer pageNumber, Integer pageSize);
	
	//Method to add a variant 
	public ProductVariantDto addAVariant(ProductVariantDto dto);
	
	//Method to fetch available colors in a product
	public List<ColorDto> getColorOptions(Long productId);
	
	//Method to fetch available sizes corresponding to a color option of a product
	public List<SizeDto> getSizeOptions(Long productId, Integer colorId);
	
	
	public List<ProductVariantDto> getAllVariantsWithColor(Integer pageNumber, Integer pageSize, String productName, String colorName);
}
