package com.navneet.ecommerce.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "productvariant", uniqueConstraints = { @UniqueConstraint(name = "uniqueColumns", columnNames = {"product_id", "product_colorid", "product_sizeid"})})
public class ProductVariants {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sku_id", nullable = false)
	private Long skuId;
	
	@Column(name = "product_price", nullable = false)
	private BigDecimal variantPrice;
	
	@Column(name = "quantity", nullable = false)
	private Integer productQuantity;
	
	@Column(name = "creation_datetime", nullable = false)
	private LocalDateTime productCreationDateTime;
	
	@Column(name = "updation_datetime", nullable = false)
	private LocalDateTime productUpdationDateTime;
	
	@ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
	private Products products;
	
	@ManyToOne
	@JoinColumn(name = "product_colorid", referencedColumnName = "color_id")
	private Color color;
	
	@ManyToOne
	@JoinColumn(name = "product_sizeid", referencedColumnName = "size_id")
	private Size size;

	
	//Getters and Setters
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getVariantPrice() {
		return variantPrice;
	}

	public void setVariantPrice(BigDecimal variantPrice) {
		this.variantPrice = variantPrice;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public LocalDateTime getProductCreationDateTime() {
		return productCreationDateTime;
	}

	public void setProductCreationDateTime(LocalDateTime productCreationDateTime) {
		this.productCreationDateTime = productCreationDateTime;
	}

	public LocalDateTime getProductUpdationDateTime() {
		return productUpdationDateTime;
	}

	public void setProductUpdationDateTime(LocalDateTime productUpdationDateTime) {
		this.productUpdationDateTime = productUpdationDateTime;
	}
}
