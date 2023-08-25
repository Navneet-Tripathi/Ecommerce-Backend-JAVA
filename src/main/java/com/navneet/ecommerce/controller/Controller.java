package com.navneet.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.navneet.ecommerce.dto.CategoryDto;
import com.navneet.ecommerce.dto.ColorDto;
import com.navneet.ecommerce.dto.ParentDto;
import com.navneet.ecommerce.dto.ProductDto;
import com.navneet.ecommerce.dto.ProductUpdateDto;
import com.navneet.ecommerce.dto.ProductVariantDto;
import com.navneet.ecommerce.dto.SizeDto;
import com.navneet.ecommerce.dto.TargetDto;
import com.navneet.ecommerce.esmodel.Product;
import com.navneet.ecommerce.services.CategoryServices;
import com.navneet.ecommerce.services.ColorServices;
import com.navneet.ecommerce.services.ProductServices;
import com.navneet.ecommerce.services.ProductVariantsServices;
import com.navneet.ecommerce.services.SizeServices;
import com.navneet.ecommerce.services.TargetServices;


@RestController
public class Controller {
	@Autowired
	private CategoryServices categoryServices;
	
	@Autowired
	private ColorServices colorServices;
	
	@Autowired
	private ProductServices productServices;
	
	@Autowired
	private ProductVariantsServices variantsServices;
	
	@Autowired
	private SizeServices sizeServices;
	
	@Autowired
	private TargetServices targetServices;
	
	                             /* --- APIs for Category entities --- */
	
	//Fetching all the available categories
	@GetMapping("/categories")
	public List<CategoryDto> getCategoryList(){
		return this.categoryServices.getAllCategory();
	}
	
	//Fetch a category entity by id
	@GetMapping("/categories/{categoryId}")
	public CategoryDto getById(@PathVariable Integer categoryId) {
		return this.categoryServices.getCategoryById(categoryId);
	}
	
	//API to post a category entity
	@PostMapping("/categories")
	public CategoryDto addCategory(@RequestBody CategoryDto dto) {
		return this.categoryServices.addACategory(dto);
	}
	
	//API to update a category entity
	@PutMapping("/categories/{categoryId}")
	public CategoryDto updateCategory(@RequestBody CategoryDto dto, @PathVariable Integer categoryId) {
		return this.categoryServices.updateACategory(dto, categoryId);
	}
	
	//API to delete a category entity
	@DeleteMapping("/categories/{categoryId}")
	public String deleteCategory(@PathVariable Integer categoryId) {
		return this.categoryServices.deleteACategory(categoryId);
	}
	                            /* --- APIs for Color entities --- */
	
	@GetMapping("/colors")
	public List<ColorDto> getAllColors(){
		return this.colorServices.getAllColors();
	}
	
	@PostMapping("/colors")
	public ColorDto addAColor(@RequestBody ColorDto colorDto) {
		return this.colorServices.addAColor(colorDto);
	}
	
	                           /* --- APIs for Size entities --- */
	
	@GetMapping("/sizes")
	public List<SizeDto> getAllSizes(){
		return this.sizeServices.getAllSizes();
	}
	
	@PostMapping("/sizes")
	public SizeDto addASize(@RequestBody SizeDto dto) {
		return this.sizeServices.addASize(dto);
	}
	
								/* --- APIs for Target entities --- */
	
	@GetMapping("/targets")
	public List<TargetDto> getAllTargets(){
		return this.targetServices.getTargets();
	}
	
	@PostMapping("/targets")
	public TargetDto addTarget(@RequestBody TargetDto targetDto) {
		return this.targetServices.addATarget(targetDto);
	}
	
	
	                          /* --- APIs for Products entities --- */
	
	//Fetching all the products from the database using pagination
	@GetMapping("/products")
	public List<ParentDto> getProducts(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "100", required = false) Integer pageSize,
			@RequestParam(value = "targetName", defaultValue = "", required = false) String targetName,
			@RequestParam(value = "categoryName", defaultValue = "", required = false) String categoryName,
			@RequestParam(value = "productName", defaultValue = "", required = false) String productName,
			@RequestParam(value = "colorName", defaultValue = "", required = false) String colorName){
		List<ParentDto> dtoList;
		if(targetName.isBlank() && categoryName.isBlank() & productName.isBlank() & colorName.isBlank()) {
			List<ProductDto> productDtos = this.productServices.getAllProducts(pageNumber, pageSize);
			dtoList = new ArrayList<>(productDtos);
			
		}else if(productName.isBlank()){
			List<ProductDto> productDtos = this.productServices.getAllProductsWithFilter(pageNumber, pageSize, targetName, categoryName, colorName);
			dtoList = new ArrayList<>(productDtos);
		}else { 
			dtoList = this.productServices.getAllProductsWithName(pageNumber, pageSize, productName, targetName, categoryName, colorName);
		}
		return dtoList;
	}
	
	

	//Fetch a product from the database according to its id
	@GetMapping(path = "/products/{productId}")
	public ProductDto getAProduct(@PathVariable Long productId) {
		ProductDto dto = this.productServices.getAProduct(productId);
		if(dto == null) {
			System.out.println("No such product exists!");
		}
		return dto;
	}
	
	//Delete a product from the database according to its id
	@DeleteMapping(path = "/products/{productId}")
	public String deleteAProduct(@PathVariable Long productId) {
		return this.productServices.deleteProduct(productId);
	}
	
	//Update a product based on its id
	@PutMapping(path = "/products/{productId}")
	public String updateAProduct(@PathVariable Long productId, @RequestBody ProductUpdateDto productDto) {
		ProductDto dto = this.productServices.updateProduct(productId, productDto);
        return "Success!";
	}
	
	/*
	@PostMapping("/products")
	public ProductDto addAProduct(@RequestBody ProductDto dto) {
		return this.productServices.addAProduct(dto);
	}
	*/
	
	@GetMapping(path = "/es/products")
	public Iterable<Product> getEsProducts(){
		return this.productServices.getProducts();
	}
	
	                         /* --- APIs for ProductVariants entities --- */

	
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
	
	//API to fetch all the available sizes corresponding to a particular color option in a product
	@GetMapping("/products/{productId}/{colorId}/sizes")
	public List<SizeDto> findAvailableSizes(@PathVariable Long productId, @PathVariable Integer colorId){
		return this.variantsServices.getSizeOptions(productId, colorId);
	}
	
	//API to add a variant in the database
	@PostMapping("/variants")
	public ProductVariantDto addAVariant(@RequestBody ProductVariantDto dto) {
		return this.variantsServices.addAVariant(dto);
	}

}
