package com.navneet.ecommerce.servicesimpl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.navneet.ecommerce.dto.SizeDto;
import com.navneet.ecommerce.entities.Size;
import com.navneet.ecommerce.repository.SizeDao;
import com.navneet.ecommerce.services.SizeServices;

@Service
public class SizeServicesImpl implements SizeServices{
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private SizeDao sizeDao;
	
	@Override
	public List<SizeDto> getAllSizes() {
		List<Size> sizeList = this.sizeDao.findAll();
		List<SizeDto> dtoList = new ArrayList<>();
		
		for(Size s: sizeList) {
			dtoList.add(this.convertToDto(s));
		}
		return dtoList;
	}

	@Override
	public SizeDto addASize(SizeDto sizeDto) {
		Size size = this.convertToSize(sizeDto);
		this.sizeDao.save(size);
		
		size = this.sizeDao.getReferenceById(sizeDto.getSizeId());
		sizeDto = this.convertToDto(size);
		return sizeDto;
	}

	public SizeDto convertToDto(Size size) {
		SizeDto dto = this.mapper.map(size, SizeDto.class);
		return dto;
	}
	
	public Size convertToSize(SizeDto dto) {
		Size size = this.mapper.map(dto, Size.class);
		return size;
	}
}
