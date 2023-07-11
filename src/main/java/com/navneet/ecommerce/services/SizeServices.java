package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.SizeDto;
import com.navneet.ecommerce.entities.Size;

public interface SizeServices {
	public List<SizeDto> getAllSizes();
	public SizeDto addASize(SizeDto sizeDto);
	
	//Method to convert size-entity to size-dto
	public SizeDto convertToDto(Size size);
	//Method to convert size-dto to size-entity
	public Size convertToSize(SizeDto dto);
}
