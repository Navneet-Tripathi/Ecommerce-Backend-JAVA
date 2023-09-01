package com.navneet.ecommerce.services;


import java.util.List;


import com.navneet.ecommerce.dto.ParentDto;
import com.navneet.ecommerce.dto.ProductDto;
import com.navneet.ecommerce.dto.ProductUpdateDto;
import com.navneet.ecommerce.entities.Products;
import com.navneet.ecommerce.esmodel.ESProduct;
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
	
	//Method to add a product in the database
	public Products addProductDB(ProductUpdateDto dto);


	//Method to fetch a product list according to various filters
	public List<ProductDto> getAllProductsWithFilter(Integer pageNumber, Integer pageSize, String targetName, String categoryName, String colorName);

	//Method to fetch a product list according to the name given in search bar
	public List<ParentDto> getAllProductsWithName(Integer pageNumber, Integer pageSize, String productName, String targetName, String categoryName, String colorName);

	//Method that will return a list dto, each containing info about color & size for a product
	List<ProductDto> getProductColorAndSize(List<Products> productsList, List<Object[]> colorsAndSizesList);
	
	//Method that will return a dto containing info about color & size for a product
	ProductDto getProductDtoWithColorAndSize(Products product, List<Object[]> colorsAndSizesList);
	
	/* ------------------------------------------- ES OPERATIONS ------------------------------------------- */
	//Method to get a list of all the products from ES
	public List<ProductDto> getProducts(Integer pageNumber, Integer pageSize);
	
	//Method to get a list of all the product with name
	public List<ParentDto> getESProductsWithFilters(Integer pageNumber, Integer pageSize, String productName, String productTargetName, String productCategoryName);
	
	//Method to get a product from ES using productId
	public ProductDto getAESProduct(Long productId);
	
	//Method to add a product to ES
	public String addProductES(ProductUpdateDto dto);

	//Method to convert product-entity to product-dto
	public List<ProductDto> convertToDtoList(List<ESProduct> productList);
	
	//Method to convert ESProduct entity to dto
	public ProductDto convertESProductToDto(ESProduct esProduct);
	
	//Method to delete a product from ES
	public String deleteESProduct(Long productId);
	
	//Method to convert dto to ESProduct entity
	public Product convertToESProduct(ProductDto dto);

	

	

	
	
	/*
	//Method to convert product-dto to product-entity
	public Products convertToProducts(ProductDto dto);
	*/
}
