package com.navneet.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navneet.ecommerce.entities.Category;

public interface CategoryDao extends JpaRepository<Category, Integer>{

}
