package com.navneet.ecommerce.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "target")
public class Target {
	@Id
	@Column(name = "target_id", nullable = false)
	private Integer  targetId;
	
	@Column(name = "target_name", nullable = false, unique = true)
	private String targetName;

	//Getters and Setters
	public Integer getTargetId() {
		return targetId;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	
	
}
