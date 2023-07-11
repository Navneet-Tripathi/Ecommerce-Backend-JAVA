package com.navneet.ecommerce.dto;

import java.time.LocalDateTime;

public class ProductDto {
	private Long productId;
	private String productName;
	private String productDescription;
	private String imageUrl;
	private Integer productCategoryId;
	private Integer productTargetId;
	private LocalDateTime productCreationDateTime;
	private String productCategoryName;
	private String productTargetName;
	
	//Getters and Setters
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	public Integer getProductTargetId() {
		return productTargetId;
	}
	public void setProductTargetId(Integer productTargetId) {
		this.productTargetId = productTargetId;
	}
	public LocalDateTime getProductCreationDateTime() {
		return productCreationDateTime;
	}
	public void setProductCreationDateTime(LocalDateTime productCreationDateTime) {
		this.productCreationDateTime = productCreationDateTime;
	}
	public String getProductCategoryName() {
		return productCategoryName;
	}
	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}
	public String getProductTargetName() {
		return productTargetName;
	}
	public void setProductTargetName(String productTargetName) {
		this.productTargetName = productTargetName;
	}
}
