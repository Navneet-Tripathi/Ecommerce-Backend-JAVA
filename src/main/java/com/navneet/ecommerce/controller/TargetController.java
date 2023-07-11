package com.navneet.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.navneet.ecommerce.dto.TargetDto;
import com.navneet.ecommerce.services.TargetServices;

@RestController
public class TargetController {
	@Autowired
	private TargetServices targetServices;
	
	@GetMapping("/targets")
	public List<TargetDto> getAllTargets(){
		return this.targetServices.getTargets();
	}
	
	@PostMapping("/targets")
	public TargetDto addTarget(@RequestBody TargetDto targetDto) {
		return this.targetServices.addATarget(targetDto);
	}
}
