package com.navneet.ecommerce.servicesimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.navneet.ecommerce.dto.ColorDto;
import com.navneet.ecommerce.dto.ProductVariantDto;
import com.navneet.ecommerce.entities.Color;
import com.navneet.ecommerce.entities.ProductVariants;
import com.navneet.ecommerce.entities.Products;
import com.navneet.ecommerce.entities.Size;
import com.navneet.ecommerce.repository.ColorDao;
import com.navneet.ecommerce.repository.ProductDao;
import com.navneet.ecommerce.repository.ProductVariantsDao;
import com.navneet.ecommerce.repository.SizeDao;
import com.navneet.ecommerce.services.ColorServices;
import com.navneet.ecommerce.services.ProductVariantsServices;

@Service
public class ProductVariantsServicesImpl implements ProductVariantsServices{
	@Autowired
	private ProductVariantsDao variantsDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ColorDao colorDao;
	
	@Autowired
	private SizeDao sizeDao;
	
	@Autowired
	private ColorServices colorServices;
	
	//Method to fetch all the product variants using pagination
	@Override
	public List<ProductVariantDto> getAllVariants(Integer pageNumber, Integer pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		Page<ProductVariants> pageList = this.variantsDao.findAll(page);
		List<ProductVariants> variantList = pageList.getContent();
		List<ProductVariantDto> dtoList = new ArrayList<>();
		
		for(ProductVariants v: variantList) {
			dtoList.add(this.convertToDto(v));
		}
		return dtoList;
	}

	//Method to add a variant 
	@Override
	public ProductVariantDto addAVariant(ProductVariantDto dto) {
		ProductVariants variants = this.convertToVariants(dto);
		ProductVariants savedVariant = this.variantsDao.save(variants);
		
		variants = this.variantsDao.getReferenceById(savedVariant.getSkuId());
		dto = this.convertToDto(variants);
		return dto;
	}
	
	//Method to fetch available color options for a product
	@Override
	public List<ColorDto> getColorOptions(Long productId) {
		Products product = productDao.getReferenceById(productId);
		List<Color> colorList = variantsDao.findDistinctColors(product);
		List<ColorDto> dtoList = new ArrayList<>();
		for(Color c : colorList) {
			ColorDto dto = this.colorServices.convertToDto(c);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	public ProductVariantDto convertToDto(ProductVariants variants) {
		ProductVariantDto dto = new ProductVariantDto();
		dto.setSkuId(variants.getSkuId());
		dto.setProductId(variants.getProducts().getProductId());
		dto.setProductName(variants.getProducts().getProductName());
		dto.setVariantColorId(variants.getColor().getColorId());
		dto.setVariantColorName(variants.getColor().getColorName());
		dto.setVariantSizeId(variants.getSize().getSizeId());
		dto.setVariantSizeName(variants.getSize().getSizeName());
		dto.setVariantPrice(variants.getVariantPrice());
		dto.setQuantity(variants.getProductQuantity());
		return dto;
	}
	
	public ProductVariants convertToVariants(ProductVariantDto dto) {
		ProductVariants variants = new ProductVariants();
		
		Products products = this.productDao.getReferenceById(dto.getProductId());
		variants.setProducts(products);
		
		Color color = this.colorDao.getReferenceById(dto.getVariantColorId());
		variants.setColor(color);
		
		Size size = this.sizeDao.getReferenceById(dto.getVariantSizeId());
		variants.setSize(size);
		
		variants.setVariantPrice(dto.getVariantPrice());
		variants.setProductQuantity(dto.getQuantity());
		return variants;
	}
}
