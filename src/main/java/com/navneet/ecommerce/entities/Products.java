package com.navneet.ecommerce.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "products")
@OptimisticLocking(type = OptimisticLockType.VERSION)
@DynamicUpdate
public class Products implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id", nullable = false)
	private Long productId;
	
	@Version
	@Column(name = "version", nullable = false)
	private Long version;
	
	@Column(name = "product_name", nullable = false)
	private String productName;
	
	@Column(name = "product_description")
	private String productDescription;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "creation_date", nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	@CreationTimestamp
	private LocalDateTime productCreationTime;
	
	@Column(name = "updated_datetime", nullable = false, updatable = true ,columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@UpdateTimestamp
	private LocalDateTime productUpdationTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_categoryid", referencedColumnName = "category_id")
    private Category category;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_targetid", referencedColumnName = "target_id")
	private Target target;
	
	@Column(name = "product_categoryname")
	private String productCategoryName;
	
	@Column(name = "product_targetname")
	private String productTargetName;
	
	
	//Default Constructor
	public Products() {
		// TODO Auto-generated constructor stub
	}

	//Parameterized Constructor
	public Products(String productName, String productDescription, String imageUrl, Category category, Target target,
			String productCategoryName, String productTargetName) {
		this.productName = productName;
		this.productDescription = productDescription;
		this.imageUrl = imageUrl;
		this.category = category;
		this.target = target;
		this.productCategoryName = productCategoryName;
		this.productTargetName = productTargetName;
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
	public LocalDateTime getProductCreationTime() {
		return productCreationTime;
	}
	public void setProductCreationTime(LocalDateTime productCreationTime) {
		this.productCreationTime = productCreationTime;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Target getTarget() {
		return target;
	}
	public void setTarget(Target target) {
		this.target = target;
	}
	public LocalDateTime getProductUpdationTime() {
		return productUpdationTime;
	}
	public void setProductUpdationTime(LocalDateTime productUpdationTime) {
		this.productUpdationTime = productUpdationTime;
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
