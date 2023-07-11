package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.ColorDto;

public interface ColorServices {
	public List<ColorDto> getAllColors();
	public ColorDto addAColor(ColorDto colorDto);
}
