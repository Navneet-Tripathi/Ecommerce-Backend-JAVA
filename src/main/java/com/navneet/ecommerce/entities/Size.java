package com.navneet.ecommerce.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "size")
public class Size {
	@Id
	@Column(name = "size_id", nullable = false)
	private Integer sizeId;
	
	@Column(name = "size_name", nullable = false, unique = true)
	private String sizeName;

	//Getters and Setters 
	public Integer getSizeId() {
		return sizeId;
	}

	public void setSizeId(Integer sizeId) {
		this.sizeId = sizeId;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
}
