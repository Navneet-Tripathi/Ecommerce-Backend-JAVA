package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.ColorDto;
import com.navneet.ecommerce.entities.Color;

public interface ColorServices {
	public List<ColorDto> getAllColors();
	public ColorDto addAColor(ColorDto colorDto);
	
	//Method to convert color entity to color dto
	public ColorDto convertToDto(Color color);
	//Method to convert color dto to color entity
	public Color convertToColor(ColorDto dto);
}
