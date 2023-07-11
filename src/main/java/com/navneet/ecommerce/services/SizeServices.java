package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.SizeDto;

public interface SizeServices {
	public List<SizeDto> getAllSizes();
	public SizeDto addASize(SizeDto sizeDto);
}
