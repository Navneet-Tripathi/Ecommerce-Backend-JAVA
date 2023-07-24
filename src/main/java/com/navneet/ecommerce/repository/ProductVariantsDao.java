package com.navneet.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.navneet.ecommerce.entities.ProductVariants;
import com.navneet.ecommerce.entities.Products;
import com.navneet.ecommerce.entities.Size;
import com.navneet.ecommerce.entities.Color;

public interface ProductVariantsDao extends JpaRepository<ProductVariants, Long>{
	@EntityGraph(attributePaths = {"color", "size"})
	public Page<ProductVariants> findAll(Pageable page);
	
	@EntityGraph(attributePaths = {"color", "size"})
	public List<ProductVariants> findAll();
	
	@Query("SELECT DISTINCT pv.color FROM ProductVariants pv WHERE pv.products = ?1")
    public List<Color> findDistinctColors(Products products);
	
	@Query("SELECT DISTINCT pv.size FROM ProductVariants pv WHERE pv.products = ?1 AND pv.color = ?2 AND pv.productQuantity>0")
	public List<Size> findDistinctSizes(Products products, Color color);
	
	 @Query("SELECT pv FROM ProductVariants pv "
	           + "LEFT JOIN FETCH pv.color "
	           + "LEFT JOIN FETCH pv.size "
	           + "WHERE pv.products.productId = :productId")
	 List<ProductVariants> getProductVariantsForProduct(Long productId);
	 
	 @Query("SELECT pv FROM ProductVariants pv JOIN FETCH pv.products p JOIN FETCH pv.color c JOIN FETCH pv.size s WHERE p.productName = :productName AND c.colorName = :colorName")
	 public Page<ProductVariants> getVariantsWithFilter(String productName, String colorName, Pageable page);
}
