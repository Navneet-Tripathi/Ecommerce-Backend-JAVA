package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.ProductVariantDto;

public interface ProductVariantsServices {
	public List<ProductVariantDto> getAllVariants();
	public ProductVariantDto addAVariant(ProductVariantDto dto);
}
