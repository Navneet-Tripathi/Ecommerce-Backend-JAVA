package com.navneet.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navneet.ecommerce.entities.Products;

public interface ProductDao extends JpaRepository<Products, Long>{

}
