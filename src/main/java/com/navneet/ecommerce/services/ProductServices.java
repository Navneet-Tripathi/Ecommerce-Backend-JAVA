package com.navneet.ecommerce.services;


import java.util.List;

import com.navneet.ecommerce.dto.ParentDto;
import com.navneet.ecommerce.dto.ProductDto;
import com.navneet.ecommerce.dto.ProductUpdateDto;
import com.navneet.ecommerce.entities.Products;
import com.navneet.ecommerce.esmodel.Product;

public interface ProductServices {
	//Method to fetch all the products from database using pagination
	public List<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize);
	
	
	//Method to fetch a product according to its id
	public ProductDto getAProduct(Long productId);
	
	//Method to delete a product according to its id
	public String deleteProduct(Long productId);
	
	//Method to update a product based on the provided product id and the dto
	public ProductDto updateProduct(Long productId, ProductUpdateDto productDto);
	
	/*
	//Method to add a product in the database
	public ProductDto addAProduct(ProductDto dto);
	*/
	
	//Method to convert product-entity to product-dto
	public ProductDto convertToDto(Products product, List<String> colorList, List<String> sizeList);

	//Method to fetch a product list according to various filters
	public List<ProductDto> getAllProductsWithFilter(Integer pageNumber, Integer pageSize, String targetName, String categoryName, String colorName);

	//Method to fetch a product list according to the name given in search bar
	public List<ParentDto> getAllProductsWithName(Integer pageNumber, Integer pageSize, String productName, String targetName, String categoryName, String colorName);


	
	/* ------------------------------------------- ES OPERATIONS ------------------------------------------- */
	public Iterable<Product> getProducts();


	
	
	/*
	//Method to convert product-dto to product-entity
	public Products convertToProducts(ProductDto dto);
	*/
}
