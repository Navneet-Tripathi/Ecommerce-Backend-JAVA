package com.navneet.ecommerce.services;

import java.util.List;

import com.navneet.ecommerce.dto.TargetDto;

public interface TargetServices {
	public List<TargetDto> getTargets();
	public TargetDto addATarget(TargetDto targetDto);
}
