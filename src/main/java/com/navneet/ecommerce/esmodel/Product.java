package com.navneet.ecommerce.esmodel;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

@Document(indexName = "products")
public class Product {	
	@Id
	@Field(type = FieldType.Long, name = "productId")
	private Long productId;
	
	@Field(type = FieldType.Text, name = "productName")
	private String productName;
	
	@Field(type = FieldType.Text, name = "productDescription")
	private String productDescription;
	
	@Field(type = FieldType.Keyword, name = "imageUrl")
	private String productImageUrl;
	
	
	@MultiField(mainField = @Field(type = FieldType.Text), otherFields = {
			@InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 100)
	})
	private String productTargetName;
	
	@MultiField(mainField = @Field(type = FieldType.Text), otherFields = {
			@InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 100)
	})
	private String productCategoryName;
	
	@Field(type = FieldType.Date, name = "productCreationDateTime")
	private String productCreationDateTime;
	
	@Field(type = FieldType.Date, name = "productUpdationTime")
	private String productUpdationDateTime;
	
	@MultiField(mainField = @Field(type = FieldType.Text), otherFields = {
			@InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 1000)
	})
	private List<String> colorList;
	
	@MultiField(mainField = @Field(type = FieldType.Text), otherFields = {
			@InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 1000)
	})
	private List<String> sizeList;

	//Default Constructor
	public Product() {
		// TODO Auto-generated constructor stub
	}

	//Parameterized Constructor
	public Product(Long productId, String productName, String productDescription, String productImageUrl,
			String productTargetName, String productCategoryName, String productCreationDateTime,
			String productUpdationDateTime, List<String> colorList, List<String> sizeList) {
		this.productId = productId;
		this.productName = productName;
		this.productDescription = productDescription;
		this.productImageUrl = productImageUrl;
		this.productTargetName = productTargetName;
		this.productCategoryName = productCategoryName;
		this.productCreationDateTime = productCreationDateTime;
		this.productUpdationDateTime = productUpdationDateTime;
		this.colorList = colorList;
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

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public String getProductTargetName() {
		return productTargetName;
	}

	public void setProductTargetName(String productTargetName) {
		this.productTargetName = productTargetName;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public String getProductCreationDateTime() {
		return productCreationDateTime;
	}

	public void setProductCreationDateTime(String productCreationDateTime) {
		this.productCreationDateTime = productCreationDateTime;
	}

	public String getProductUpdationDateTime() {
		return productUpdationDateTime;
	}

	public void setProductUpdationDateTime(String productUpdationDateTime) {
		this.productUpdationDateTime = productUpdationDateTime;
	}

	public List<String> getColorList() {
		return colorList;
	}

	public void setColorList(List<String> colorList) {
		this.colorList = colorList;
	}

	public List<String> getSizeList() {
		return sizeList;
	}

	public void setSizeList(List<String> sizeList) {
		this.sizeList = sizeList;
	}
}
