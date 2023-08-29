package com.navneet.ecommerce.esmodel;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

@Document(indexName = "variants")
public class Variant {
	@Id
	@Field(type = FieldType.Long, name = "skuId")
	private Long sku_id;
	
	@Field(type = FieldType.Long, name = "productId")
	private Long productId;
	
	@Field(type = FieldType.Text, name = "productName")
	private String productName;
	
	@Field(type = FieldType.Text, name = "productDescription")
	private String productDescription;
	
	@Field(type = FieldType.Keyword, name = "productImageUrl")
	private String productImageUrl;
	
	@Field(type = FieldType.Float, name = "productPrice")
	private Float productPrice;
	
	@MultiField(mainField = @Field(type = FieldType.Text), otherFields = {
			@InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 100)
	})
	private String productSizeName;
	
	@MultiField(mainField = @Field(type = FieldType.Text), otherFields = {
			@InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 100)
	})
	private String productColorName;
	
	
	@MultiField(mainField = @Field(type = FieldType.Text), otherFields = {
			@InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 256)
	})
	private String productTargetName;
	
	@MultiField(mainField = @Field(type = FieldType.Text), otherFields = {
			@InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 256)
	})
	private String productCategoryName;
	
	@Field(type = FieldType.Date, name = "productCreationTime")
	private String productCreationDateTime;
	
	@Field(type = FieldType.Date, name = "productUpdationTime")
	private String productUpdationDateTime;
	
	@Field(type = FieldType.Integer, name = "quantity")
	private Integer productQuantity;
	
	
	//Default Constructor
	public Variant() {
		
	}
	
	//Parameterized Constructor
	public Variant(Long sku_id, Long productId, String productName, String productDescription, String productImageUrl,
			Float productPrice, String productSizeName, String productColorName, String productTargetName,
			String productCategoryName, String productCreationDateTime, String productUpdationDateTime,
			Integer productQuantity) {
		this.sku_id = sku_id;
		this.productId = productId;
		this.productName = productName;
		this.productDescription = productDescription;
		this.productImageUrl = productImageUrl;
		this.productPrice = productPrice;
		this.productSizeName = productSizeName;
		this.productColorName = productColorName;
		this.productTargetName = productTargetName;
		this.productCategoryName = productCategoryName;
		this.productCreationDateTime = productCreationDateTime;
		this.productUpdationDateTime = productUpdationDateTime;
		this.productQuantity = productQuantity;
	}
	

	//Getters
	public Long getSku_id() {
		return sku_id;
	}

	public Long getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public Float getProductPrice() {
		return productPrice;
	}

	public String getProductSizeName() {
		return productSizeName;
	}

	public String getProductColorName() {
		return productColorName;
	}

	public String getProductTargetName() {
		return productTargetName;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public String getProductCreationDateTime() {
		return productCreationDateTime;
	}

	public String getProductUpdationDateTime() {
		return productUpdationDateTime;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}

	
	//Setters
	public void setSku_id(Long sku_id) {
		this.sku_id = sku_id;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public void setProductPrice(Float productPrice) {
		this.productPrice = productPrice;
	}

	public void setProductSizeName(String productSizeName) {
		this.productSizeName = productSizeName;
	}

	public void setProductColorName(String productColorName) {
		this.productColorName = productColorName;
	}

	public void setProductTargetName(String productTargetName) {
		this.productTargetName = productTargetName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public void setProductCreationDateTime(String productCreationDateTime) {
		this.productCreationDateTime = productCreationDateTime;
	}

	public void setProductUpdationDateTime(String productUpdationDateTime) {
		this.productUpdationDateTime = productUpdationDateTime;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}
}
