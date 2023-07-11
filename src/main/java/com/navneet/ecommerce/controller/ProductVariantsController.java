package com.navneet.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.navneet.ecommerce.dto.ProductVariantDto;
import com.navneet.ecommerce.services.ProductVariantsServices;

@RestController
public class ProductVariantsController {
	@Autowired
	private ProductVariantsServices variantsServices;
	
	@GetMapping("/variants")
	public List<ProductVariantDto> getAllVariants(){
		return this.variantsServices.getAllVariants();
	}
	
	@PostMapping("/variants")
	public ProductVariantDto addAVariant(@RequestBody ProductVariantDto dto) {
		return this.variantsServices.addAVariant(dto);
	}
}
