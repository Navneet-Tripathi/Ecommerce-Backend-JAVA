package com.navneet.ecommerce.esmodel;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "products")
public class Product {
	@Id
	private Long id;
	
	@Field(type = FieldType.Text, name = "productName")
	private String productName;
	
	@Field(type = FieldType.Text, name = "productDescription")
	private String productDescription;
	
	//Default Constructor
	public Product() {
		
	}
	
	//Parameterized Constructor
	public Product(Long id, String productName, String productDescription) {
		this.id = id;
		this.productName = productName;
		this.productDescription = productDescription;
	}

	//Getters
	public Long getId() {
		return id;
	}
	public String getProductName() {
		return productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	
	//Setters
	public void setId(Long id) {
		this.id = id;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	
	
}
