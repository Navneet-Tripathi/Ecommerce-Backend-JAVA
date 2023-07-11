package com.navneet.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.navneet.ecommerce.dto.ProductVariantDto;
import com.navneet.ecommerce.services.ProductVariantsServices;

@RestController
public class ProductVariantsController {
	@Autowired
	private ProductVariantsServices variantsServices;
	
	//Fetching all product variants from database using pagination 
	@GetMapping("/variants")
	public List<ProductVariantDto> getAllVariants(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize){
		return this.variantsServices.getAllVariants(pageNumber, pageSize);
	}
	
	@PostMapping("/variants")
	public ProductVariantDto addAVariant(@RequestBody ProductVariantDto dto) {
		return this.variantsServices.addAVariant(dto);
	}
}
