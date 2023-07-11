package com.navneet.ecommerce.dto;

import java.math.BigDecimal;

public class ProductVariantDto {
	private Long skuId;
	private Long productId;
	private String productName;
	private Integer variantColorId;
	private String variantColorName;
	private Integer variantSizeId;
	private String variantSizeName;
	private BigDecimal variantPrice;
	private Integer quantity;
	
	//Getters and Setters
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
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
	public Integer getVariantColorId() {
		return variantColorId;
	}
	public void setVariantColorId(Integer variantColorId) {
		this.variantColorId = variantColorId;
	}
	public String getVariantColorName() {
		return variantColorName;
	}
	public void setVariantColorName(String variantColorName) {
		this.variantColorName = variantColorName;
	}
	public Integer getVariantSizeId() {
		return variantSizeId;
	}
	public void setVariantSizeId(Integer variantSizeId) {
		this.variantSizeId = variantSizeId;
	}
	public String getVariantSizeName() {
		return variantSizeName;
	}
	public void setVariantSizeName(String variantSizeName) {
		this.variantSizeName = variantSizeName;
	}
	public BigDecimal getVariantPrice() {
		return variantPrice;
	}
	public void setVariantPrice(BigDecimal variantPrice) {
		this.variantPrice = variantPrice;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
