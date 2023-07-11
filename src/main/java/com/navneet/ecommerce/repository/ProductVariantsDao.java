package com.navneet.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navneet.ecommerce.entities.ProductVariants;

public interface ProductVariantsDao extends JpaRepository<ProductVariants, Long>{

}
