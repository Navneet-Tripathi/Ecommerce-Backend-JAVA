package com.navneet.ecommerce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.navneet.ecommerce.annotations.TrackResponceTime;
import com.navneet.ecommerce.dto.ProductDto;
import com.navneet.ecommerce.entities.Products;

@Component
@TrackResponceTime
public class External {
	public List<ProductDto> getProductColorAndSize(List<Products> productsList, List<Object[]> colorsAndSizesList) {
		Map<Long, Set<String>> colorsMap = new HashMap<>();
		Map<Long, Set<String>> sizesMap = new HashMap<>();

		for (Object[] colorAndSize : colorsAndSizesList) {
			Long productId = (Long) colorAndSize[0];
			String colorName = (String) colorAndSize[1];
			String sizeName = (String) colorAndSize[2];

			colorsMap.computeIfAbsent(productId, k -> new HashSet<>()).add(colorName);
			sizesMap.computeIfAbsent(productId, k -> new HashSet<>()).add(sizeName);
		}

		List<ProductDto> productDtoList = new ArrayList<>();

		for (Products product : productsList) {
			Long productId = product.getProductId();
			Set<String> colors = colorsMap.getOrDefault(productId, Collections.emptySet());
			Set<String> sizes = sizesMap.getOrDefault(productId, Collections.emptySet());

			ProductDto productDto = new ProductDto(product.getProductId(), product.getProductName(),
					product.getProductDescription(), product.getImageUrl(), product.getCategory().getCategoryName(),
					product.getTarget().getTargetName(), product.getProductCreationTime(),
					product.getProductUpdationTime(), new ArrayList<>(colors), new ArrayList<>(sizes));

			productDtoList.add(productDto);
		}

		return productDtoList;
	}
}
