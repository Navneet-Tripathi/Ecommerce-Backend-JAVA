package com.navneet.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ProductDto {
	private Long productId;
	private String productName;
	private String productDescription;
	private String imageUrl;
	private Integer productCategoryId;
	private String productCategoryName;
	private Integer productTargetId;
	private String productTargetName;
	private LocalDateTime productCreationDateTime;
	private LocalDateTime productUpdationTime;
	private List<ColorDto> colorList;
	private List<SizeDto> sizeList;

	
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
	public LocalDateTime getProductUpdationTime() {
		return productUpdationTime;
	}
	public void setProductUpdationTime(LocalDateTime prouductUpdationTime) {
		this.productUpdationTime = prouductUpdationTime;
	}
	public List<ColorDto> getColorList() {
		return colorList;
	}
	public void setColorList(List<ColorDto> colorList) {
		this.colorList = colorList;
	}
	public List<SizeDto> getSizeList() {
		return sizeList;
	}
	public void setSizeList(List<SizeDto> sizeList) {
		this.sizeList = sizeList;
	}
}
