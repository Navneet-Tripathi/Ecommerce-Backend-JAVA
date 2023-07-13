package com.navneet.ecommerce.servicesimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.navneet.ecommerce.annotations.TrackResponceTime;
import com.navneet.ecommerce.dto.ProductDto;
import com.navneet.ecommerce.entities.Category;
import com.navneet.ecommerce.entities.Products;
import com.navneet.ecommerce.entities.Target;
import com.navneet.ecommerce.repository.CategoryDao;
import com.navneet.ecommerce.repository.ProductDao;
import com.navneet.ecommerce.repository.TargetDao;
import com.navneet.ecommerce.services.ProductServices;

@Service
@TrackResponceTime
public class ProductServicesImpl implements ProductServices{
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private TargetDao targetDao;
	
	//Method to fetch all products in the database using pagination concept
	@Override
	public List<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		Page<Products> pageList = this.productDao.findAll(page);
		List<Products> productList = pageList.getContent();
		List<ProductDto> dtoList = new ArrayList<>();
		
		for(Products p: productList) {
			dtoList.add(this.convertToDto(p));
		}
		return dtoList;
	}

	//Method to add a product in the database
	@Override
	public ProductDto addAProduct(ProductDto dto) {
		Products product = this.convertToProducts(dto);
		Products savedProduct = this.productDao.save(product);
		
		product = this.productDao.getReferenceById(savedProduct.getProductId());
		dto = this.convertToDto(product);
		return dto;
	}
	
	//Method to fetch a product from database using product id
	@Override
	public ProductDto getAProduct(Long productId) {
		Products products = this.productDao.getReferenceById(productId);
		ProductDto dto = this.convertToDto(products);
		return dto;
	}

	
	//Method to convert product-entity to product-dto
	@Override
	public ProductDto convertToDto(Products product) {
		ProductDto dto = new ProductDto();
		dto.setProductId(product.getProductId());
		dto.setProductName(product.getProductName());
		dto.setProductDescription(product.getProductDescription());
		dto.setImageUrl(product.getImageUrl());
		dto.setProductCategoryId(product.getCategory().getCategoryId());
		dto.setProductTargetId(product.getTarget().getTargetId());
		dto.setProductCreationDateTime(product.getProductCreationTime());
		dto.setProductCategoryName(product.getCategory().getCategoryName());
		dto.setProductTargetName(product.getTarget().getTargetName());
		dto.setProductUpdationTime(product.getProductUpdationTime());
		return dto;
	}
	
	
	//Method to convert product-dto to product-entity
	@Override
	public Products convertToProducts(ProductDto dto) {
		Products product = new Products();
		product.setProductName(dto.getProductName());
		product.setProductDescription(dto.getProductDescription());
		product.setImageUrl(dto.getImageUrl());
		
		Category category = this.categoryDao.getReferenceById(dto.getProductCategoryId());
		Target target = this.targetDao.getReferenceById(dto.getProductTargetId());
				
				
		product.setCategory(category);
		product.setTarget(target);
		return product;
	}
}
