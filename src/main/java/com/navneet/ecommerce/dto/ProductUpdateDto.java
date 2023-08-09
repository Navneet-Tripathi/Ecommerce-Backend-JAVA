package com.navneet.ecommerce.dto;

public class ProductUpdateDto {
	private String productName;
	private String productDescription;
	private String imageUrl;
	private Integer categoryId;
	private Integer targetId;
	
	//Default Constructor
	public ProductUpdateDto() {
		
	}
	
	//Parameterized Constructor
	public ProductUpdateDto(String productName, String productDescription, String imageUrl, Integer categoryId,
			Integer targetId) {
		this.productName = productName;
		this.productDescription = productDescription;
		this.imageUrl = imageUrl;
		this.categoryId = categoryId;
		this.targetId = targetId;
	}
	
	//Getters and Setters
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
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getTargetId() {
		return targetId;
	}
	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}
}
