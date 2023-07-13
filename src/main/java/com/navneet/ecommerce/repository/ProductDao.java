package com.navneet.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.navneet.ecommerce.entities.Products;

public interface ProductDao extends JpaRepository<Products, Long>{
	@EntityGraph(attributePaths = {"category", "target"})
	public Page<Products> findAll(Pageable page);
}
