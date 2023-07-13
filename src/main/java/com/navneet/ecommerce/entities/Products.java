package com.navneet.ecommerce.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Products {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id", nullable = false)
	private Long productId;
	
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
	private LocalDateTime productUpdationTime;
	
	@ManyToOne
    @JoinColumn(name = "product_categoryid", referencedColumnName = "category_id")
    private Category category;
	
	@ManyToOne
	@JoinColumn(name = "product_targetid", referencedColumnName = "target_id")
	private Target target;
	
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
	
	
}
