package com.navneet.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navneet.ecommerce.entities.Size;

public interface SizeDao extends JpaRepository<Size, Integer>{

}
