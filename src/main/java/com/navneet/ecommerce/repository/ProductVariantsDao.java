package com.navneet.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.navneet.ecommerce.entities.ProductVariants;
import com.navneet.ecommerce.entities.Products;
import com.navneet.ecommerce.entities.Size;
import com.navneet.ecommerce.entities.Color;

public interface ProductVariantsDao extends JpaRepository<ProductVariants, Long>{
	@Query("SELECT DISTINCT pv.color FROM ProductVariants pv WHERE pv.products = ?1")
    public List<Color> findDistinctColors(Products products);
	
	@Query("SELECT DISTINCT pv.size FROM ProductVariants pv WHERE pv.products = ?1 AND pv.color = ?2")
	public List<Size> findDistinctSizes(Products products, Color color);
}
