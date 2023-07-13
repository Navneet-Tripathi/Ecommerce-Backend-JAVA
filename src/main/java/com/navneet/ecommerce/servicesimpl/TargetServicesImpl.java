package com.navneet.ecommerce.servicesimpl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.navneet.ecommerce.annotations.TrackResponceTime;
import com.navneet.ecommerce.dto.TargetDto;
import com.navneet.ecommerce.entities.Target;
import com.navneet.ecommerce.repository.TargetDao;
import com.navneet.ecommerce.services.TargetServices;

@Service
@TrackResponceTime
public class TargetServicesImpl implements TargetServices{
	@Autowired 
	private ModelMapper mapper;
	
	@Autowired
	private TargetDao targetDao;
	
	
	@Override
	public List<TargetDto> getTargets() {
		List<Target> targetList = this.targetDao.findAll();
		List<TargetDto> dtoList = new ArrayList<>();
		
		for(Target t: targetList) {
			dtoList.add(this.convertToDto(t));
		}
		return dtoList;
	}

	@Override
	public TargetDto addATarget(TargetDto targetDto) {
		Target target = this.convertToTarget(targetDto);
		this.targetDao.save(target);
		
		target = this.targetDao.getReferenceById(targetDto.getTargetId());
		targetDto = this.convertToDto(target);
		return targetDto;
	}
	
	public TargetDto convertToDto(Target target) {
		TargetDto dto = this.mapper.map(target, TargetDto.class);
		return dto;
	}
	
	public Target convertToTarget(TargetDto dto) {
		Target target = this.mapper.map(dto, Target.class);
		return target;
	}

}
