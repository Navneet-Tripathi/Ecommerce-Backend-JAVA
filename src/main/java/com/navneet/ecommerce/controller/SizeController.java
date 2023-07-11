package com.navneet.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.navneet.ecommerce.dto.SizeDto;
import com.navneet.ecommerce.services.SizeServices;

@RestController
public class SizeController {
	@Autowired
	private SizeServices sizeServices;
	
	@GetMapping("/sizes")
	public List<SizeDto> getAllSizes(){
		return this.sizeServices.getAllSizes();
	}
	
	@PostMapping("/sizes")
	public SizeDto addASize(@RequestBody SizeDto dto) {
		return this.sizeServices.addASize(dto);
	}
}
