package com.navneet.ecommerce.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "color")
public class Color {
	@Id
	@Column(name = "color_id", nullable = false)
	private Integer colorId;
	
	@Column(name = "color_name", nullable = false, unique = true)
	private String colorName;
	
	//Getters and Setters
	public Integer getColorId() {
		return colorId;
	}
	public void setColorId(Integer colorId) {
		this.colorId = colorId;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	
}
