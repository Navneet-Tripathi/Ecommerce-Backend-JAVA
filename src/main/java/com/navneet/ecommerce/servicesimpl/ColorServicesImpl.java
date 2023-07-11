package com.navneet.ecommerce.servicesimpl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.navneet.ecommerce.dto.ColorDto;
import com.navneet.ecommerce.entities.Color;
import com.navneet.ecommerce.repository.ColorDao;
import com.navneet.ecommerce.services.ColorServices;

@Service
public class ColorServicesImpl implements ColorServices{
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private ColorDao colorDao;
	
	@Override
	public List<ColorDto> getAllColors() {
		List<Color> colorList = this.colorDao.findAll();
		List<ColorDto> dtoList = new ArrayList<>();
		
		for(Color c: colorList) {
			dtoList.add(this.convertToDto(c));
		}
		return dtoList;
	}

	@Override
	public ColorDto addAColor(ColorDto colorDto) {
		Color color = this.convertToColor(colorDto);
		this.colorDao.save(color);
		
		color = this.colorDao.getReferenceById(colorDto.getColorId());
		colorDto = this.convertToDto(color);
		return colorDto;
	}
	
	public ColorDto convertToDto(Color color) {
		ColorDto dto = this.mapper.map(color, ColorDto.class);
		return dto;
	}
	
	public Color convertToColor(ColorDto dto) {
		Color color = this.mapper.map(dto, Color.class);
		return color;
	}
}
