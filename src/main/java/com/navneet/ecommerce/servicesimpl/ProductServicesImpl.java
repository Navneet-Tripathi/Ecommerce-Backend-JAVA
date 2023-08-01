package com.navneet.ecommerce.servicesimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.navneet.ecommerce.annotations.TrackResponceTime;
import com.navneet.ecommerce.dto.ParentDto;
import com.navneet.ecommerce.dto.ProductDto;
import com.navneet.ecommerce.dto.Variant;
import com.navneet.ecommerce.entities.Category;
import com.navneet.ecommerce.entities.Color;
import com.navneet.ecommerce.entities.ProductVariants;
import com.navneet.ecommerce.entities.Products;
import com.navneet.ecommerce.entities.Target;
import com.navneet.ecommerce.repository.CategoryDao;
import com.navneet.ecommerce.repository.ColorDao;
import com.navneet.ecommerce.repository.ProductDao;
import com.navneet.ecommerce.repository.ProductVariantsDao;
import com.navneet.ecommerce.repository.TargetDao;
import com.navneet.ecommerce.services.ProductServices;


@Service
@TrackResponceTime
public class ProductServicesImpl implements ProductServices {
	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductVariantsDao variantsDao;

	@Autowired
	private TargetDao targetDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private ColorDao colorDao;



	// Method to fetch all products in the database using pagination concept
	@Override
	@Cacheable(value = "products", key = "{#pageNumber, #pageSize}")
	public List<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize) {
		System.out.println("Get all products with called ------------------");
		Pageable page = PageRequest.of(pageNumber, pageSize);
		//Page<Products> productPage = this.productDao.findAll(page);
		List<Products> productsList = this.productDao.findAllProducts(pageNumber*pageSize, pageSize);
		List<Object[]> colorsAndSizesList = this.productDao.findUniqueColorsAndSizesForProducts(productsList);
		List<ProductDto> productDtoList = this.getProductColorAndSize(productsList, colorsAndSizesList);
		return productDtoList;
	}

	//Method to get a productDto list containing details of a product and its corresponding available color and sizes
	private List<ProductDto> getProductColorAndSize(List<Products> productsList, List<Object[]> colorsAndSizesList) {
		Map<Long, Set<String>> colorsMap = new HashMap<>();
		Map<Long, Set<String>> sizesMap = new HashMap<>();

		for (Object[] colorAndSize : colorsAndSizesList) {
			Long productId = (Long) colorAndSize[0];
			String colorName = (String) colorAndSize[1];
			String sizeName = (String) colorAndSize[2];

			colorsMap.computeIfAbsent(productId, k -> new HashSet<>()).add(colorName);
			sizesMap.computeIfAbsent(productId, k -> new HashSet<>()).add(sizeName);
		}

		List<ProductDto> productDtoList = new ArrayList<>();

		for (Products product : productsList) {
			Long productId = product.getProductId();
			Set<String> colors = colorsMap.getOrDefault(productId, Collections.emptySet());
			Set<String> sizes = sizesMap.getOrDefault(productId, Collections.emptySet());

			ProductDto productDto = new ProductDto(product.getProductId(), product.getProductName(),
					product.getProductDescription(), product.getImageUrl(), product.getProductCategoryName(),
					product.getProductTargetName(), product.getProductCreationTime(),
					product.getProductUpdationTime(), new ArrayList<>(colors), new ArrayList<>(sizes));

			productDtoList.add(productDto);
		}

		return productDtoList;
	}

	// //Method to fetch a product list according to various filters
	@Override
	@Cacheable(value = "products", key = "{#pageNumber, #pageSize, #targetName, #categoryName, #colorName}")
	public List<ProductDto> getAllProductsWithFilter(Integer pageNumber, Integer pageSize, String targetName,
			String categoryName, String colorName) {
		System.out.println("Get all products with filters called ------------------");
		if(targetName.isBlank()) {
			targetName = null;
		}
		else {
			targetName = targetName.toLowerCase();
		}
		
		
		if(categoryName.isBlank()) {
			categoryName = null;
		}else {
			categoryName = categoryName.toLowerCase();
		}
		
		
		Integer colorId = null;
		if(!colorName.isBlank()) {
			Color color = this.colorDao.findColorByColorNameIgnoreCase(colorName);
			if (color != null) colorId = color.getColorId();
		}
		
		Pageable page = PageRequest.of(pageNumber, pageSize);
		Page<Products> productPage = this.productDao.findProductsByFilters(categoryName, targetName, colorId, page);
		List<Products> productsList = productPage.getContent();
		List<Object[]> colorsAndSizesList = this.productDao.findUniqueColorsAndSizesForProducts(productsList);
		List<ProductDto> productDtoList = this.getProductColorAndSize(productsList, colorsAndSizesList);
		return productDtoList;
	}

	/*
	 * //Method to add a product in the database
	 * 
	 * @Override public ProductDto addAProduct(ProductDto dto) { Products product =
	 * this.convertToProducts(dto); Products savedProduct =
	 * this.productDao.save(product);
	 * 
	 * product = this.productDao.getReferenceById(savedProduct.getProductId()); dto
	 * = this.convertToDto(product); return dto; }
	 */

	// Method to fetch a product from database using product id
	@Override
	public ProductDto getAProduct(Long productId) {
		Products product = this.productDao.getReferenceById(productId);
		Set<String> colorSet = new HashSet<>();
		Set<String> sizeSet = new HashSet<>();
		List<ProductVariants> variantsList = this.variantsDao.getProductVariantsForProduct(product.getProductId());
		for (ProductVariants variants : variantsList) {
			String colorName = variants.getColor().getColorName();
			String sizeName = variants.getSize().getSizeName();

			colorSet.add(colorName);
			sizeSet.add(sizeName);
		}

		ProductDto dto = this.convertToDto(product, new ArrayList<>(colorSet), new ArrayList<>(sizeSet));
		return dto;
	}

	// Method to convert product-entity to product-dto
	@Override
	public ProductDto convertToDto(Products product, List<String> colorList, List<String> sizeList) {
		ProductDto dto = new ProductDto();
		dto.setProductId(product.getProductId());
		dto.setProductName(product.getProductName());
		dto.setProductDescription(product.getProductDescription());
		dto.setImageUrl(product.getImageUrl());
		dto.setProductCreationDateTime(product.getProductCreationTime());
		dto.setProductUpdationTime(product.getProductUpdationTime());
		dto.setProductCategoryName(product.getCategory().getCategoryName());
		dto.setProductTargetName(product.getTarget().getTargetName());

		dto.setColorList(new ArrayList<>(colorList));
		dto.setSizeList(new ArrayList<>(sizeList));
		return dto;
	}

	// Method to fetch the product list according to the name and other constraints provided in the search
	// 
	@Override
	@Cacheable(value = "products", key = "{#pageNumber, #pageSize, #productName, #targetName, #categoryName, #colorName}")
	public List<ParentDto> getAllProductsWithName(Integer pageNumber, Integer pageSize, String productName, String targetName, String categoryName, String colorName) {
		
		if(targetName.isBlank()) {
			targetName = null;
		}else {
			targetName = targetName.toLowerCase();
		}
		
		if(categoryName.isBlank()) {
			categoryName = null;
		}else {
			categoryName = categoryName.toLowerCase();
		}
		
		Integer colorId;
		if(colorName.isBlank()) {
			colorId = null;
		}else {
			Color color = this.colorDao.findColorByColorNameIgnoreCase(colorName);
			if (color != null) colorId = color.getColorId();
			else colorId = 0;
		}
		
		Pageable page = PageRequest.of(pageNumber, pageSize);
		Page<Products> productPage = this.productDao.findProductsByName(page, productName.toLowerCase(), categoryName, targetName, colorId);
		List<Products> productsList = productPage.getContent();
		List<Object[]> colorsAndSizesList = this.productDao.findUniqueColorsAndSizesForProducts(productsList);
		List<ProductDto> productDtoList = this.getProductColorAndSize(productsList, colorsAndSizesList);
		List<ParentDto> resultList;
		if(colorName.isBlank()) {
			resultList = new ArrayList<>(productDtoList);
		}else {
			List<Variant> variantList = this.getVariantList(productDtoList, colorName);
			resultList = new ArrayList<>(variantList);
		}
		return resultList;
	}

	private List<Variant> getVariantList(List<ProductDto> productDtoList, String colorName) {
		List<Variant> resultList = new ArrayList<>();
		for(ProductDto dto : productDtoList) {
			Variant variant = new Variant(dto.getProductId(), dto.getProductName(), dto.getProductDescription(), 
					dto.getImageUrl(), dto.getProductCategoryName(), dto.getProductTargetName(),
					dto.getProductCreationDateTime(), dto.getProductUpdationTime(),
					colorName, dto.getSizeList());
			resultList.add(variant);
		}
		return resultList;
	}

	/*
	 * //Method to convert product-dto to product-entity
	 * 
	 * @Override public Products convertToProducts(ProductDto dto) { Products
	 * product = new Products(); product.setProductName(dto.getProductName());
	 * product.setProductDescription(dto.getProductDescription());
	 * product.setImageUrl(dto.getImageUrl());
	 * 
	 * Category category =
	 * this.categoryDao.getReferenceById(dto.getProductCategoryId()); Target target
	 * = this.targetDao.getReferenceById(dto.getProductTargetId());
	 * 
	 * 
	 * product.setCategory(category); product.setTarget(target); return product; }
	 */
}
