package com.navneet.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navneet.ecommerce.entities.Color;

public interface ColorDao extends JpaRepository<Color, Integer>{
	public Color findColorByColorNameIgnoreCase(String colorName);
}
