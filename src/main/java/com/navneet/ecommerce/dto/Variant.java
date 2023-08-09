package com.navneet.ecommerce.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Variant implements ParentDto, Serializable{
	private static final long serialVersionUID = 123456789L;
	private Long productId;
	private String productName;
	private String productDescription;
	private String imageUrl;
	private String productCategoryName;
	private String productTargetName;
	private LocalDateTime productCreationDateTime;
	private LocalDateTime productUpdationTime;
	private String colorName;
	private List<String> sizeList;
	
	//Default Constructor
	public Variant() {
		
	}

	//Parameterized Constructor
	public Variant(Long productId, String productName, String productDescription, String imageUrl,
			String productCategoryName, String productTargetName, LocalDateTime productCreationDateTime,
			LocalDateTime productUpdationTime, String colorName, List<String> sizeList) {
		this.productId = productId;
		this.productName = productName;
		this.productDescription = productDescription;
		this.imageUrl = imageUrl;
		this.productCategoryName = productCategoryName;
		this.productTargetName = productTargetName;
		this.productCreationDateTime = productCreationDateTime;
		this.productUpdationTime = productUpdationTime;
		this.colorName = colorName;
		this.sizeList = sizeList;
	}
	
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
	public LocalDateTime getProductCreationDateTime() {
		return productCreationDateTime;
	}
	public void setProductCreationDateTime(LocalDateTime productCreationDateTime) {
		this.productCreationDateTime = productCreationDateTime;
	}
	public LocalDateTime getProductUpdationTime() {
		return productUpdationTime;
	}
	public void setProductUpdationTime(LocalDateTime productUpdationTime) {
		this.productUpdationTime = productUpdationTime;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public List<String> getSizeList() {
		return sizeList;
	}
	public void setSizeList(List<String> sizeList) {
		this.sizeList = sizeList;
	}
}
