package com.navneet.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navneet.ecommerce.entities.Target;

public interface TargetDao extends JpaRepository<Target, Integer>{
	public Target findTargetByTargetNameIgnoreCase(String targetName);
}
