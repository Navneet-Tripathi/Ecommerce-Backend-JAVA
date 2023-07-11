package com.navneet.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.navneet.ecommerce.dto.ColorDto;
import com.navneet.ecommerce.services.ColorServices;

@RestController
public class ColorController {
	@Autowired
	private ColorServices colorServices;
	
	@GetMapping("/colors")
	public List<ColorDto> getAllColors(){
		return this.colorServices.getAllColors();
	}
	
	@PostMapping("/colors")
	public ColorDto addAColor(@RequestBody ColorDto colorDto) {
		return this.colorServices.addAColor(colorDto);
	}
}
