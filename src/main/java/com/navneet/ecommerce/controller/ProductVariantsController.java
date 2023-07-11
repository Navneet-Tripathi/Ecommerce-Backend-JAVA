package com.navneet.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.navneet.ecommerce.dto.ColorDto;
import com.navneet.ecommerce.dto.ProductVariantDto;
import com.navneet.ecommerce.services.ProductVariantsServices;

@RestController
public class ProductVariantsController {
	@Autowired
	private ProductVariantsServices variantsServices;
	
	//API to fetch all the product variants from database using pagination 
	@GetMapping("/variants")
	public List<ProductVariantDto> getAllVariants(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize){
		return this.variantsServices.getAllVariants(pageNumber, pageSize);
	}
	
	//API to fetch all the available colors corresponding to a product
	@GetMapping("/products/{productId}/colors")
	public List<ColorDto> findAvailableColors(@PathVariable Long productId){
		return this.variantsServices.getColorOptions(productId);
	}
	
	//API to add a variant in the database
	@PostMapping("/variants")
	public ProductVariantDto addAVariant(@RequestBody ProductVariantDto dto) {
		return this.variantsServices.addAVariant(dto);
	}
}
