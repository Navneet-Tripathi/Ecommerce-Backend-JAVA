package com.navneet.ecommerce.servicesimpl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.navneet.ecommerce.annotations.TrackResponceTime;
import com.navneet.ecommerce.dto.ParentDto;
import com.navneet.ecommerce.dto.ProductDto;
import com.navneet.ecommerce.dto.ProductUpdateDto;
import com.navneet.ecommerce.dto.Variant;
import com.navneet.ecommerce.entities.Category;
import com.navneet.ecommerce.entities.Color;
import com.navneet.ecommerce.entities.Products;
import com.navneet.ecommerce.entities.Target;
import com.navneet.ecommerce.esmodel.Product;
import com.navneet.ecommerce.esrepository.ProductRepo;
import com.navneet.ecommerce.repository.CategoryDao;
import com.navneet.ecommerce.repository.ColorDao;
import com.navneet.ecommerce.repository.ProductDao;
import com.navneet.ecommerce.repository.ProductVariantsDao;
import com.navneet.ecommerce.repository.TargetDao;
import com.navneet.ecommerce.services.ProductServices;




@Service
@TrackResponceTime
public class ProductServicesImpl implements ProductServices {
	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductVariantsDao variantsDao;

	@Autowired
	private TargetDao targetDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private ColorDao colorDao;
	
	@Autowired
	private RedisCacheManager cacheManager;
	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	private Logger logger = LoggerFactory.getLogger(ProductServicesImpl.class);
	
	/* ---------------------------------------------- ES OPERATIONS ---------------------------------------------- */
	//Method to fetch products from elastic search
	@Override
	public List<Product> getProducts(Integer pageNumber, Integer pageSize){
		Pageable page = PageRequest.of(pageNumber, pageSize);
		Page<Product> productPage = this.productRepo.findAll(page);
		List<Product> productList = productPage.getContent();
		return productList;
	}
	
	//Method to add a product 
	@Override
	@Transactional
	public String addProductES(ProductUpdateDto dto) {
		Products savedDBProduct = this.addProductDB(dto);
 		List<Object[]> colorsAndSizesList = this.productDao.findUniqueColorsAndSizesForAProduct(savedDBProduct);
		ProductDto productDto = this.getProductDtoWithColorAndSize(savedDBProduct, colorsAndSizesList);
		Product esProduct = this.convertToESProduct(productDto);
		
		System.out.println("---------- creation date : ------------"+ esProduct.getProductCreationDateTime());
		Product savedESProduct = this.productRepo.save(esProduct);
		
		if(savedESProduct == null) {
			//Throw and Handle Exception
		}
		return "Product Saved succesfully with id : "+ savedESProduct.getProductId();
	}
	
	// Method to convert product-entity list to product-dto list--> For ElasticSearch operation
	@Override
	public List<ProductDto> convertToDtoList(List<Product> productList) {
		List<ProductDto> dtoList = new ArrayList<>();
		for(Product product: productList) {
			ProductDto dto = this.mapper.map(product, ProductDto.class);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	//Method to convert product-dto to product-entity --> For ElasticSearch operation
	@Override
	public Product convertToESProduct(ProductDto dto) {
		Product esProduct = this.mapper.map(dto, Product.class);
		return esProduct;
	}
	
	
	/* ---------------------------------------------- MYSQL OPERATIONS ---------------------------------------------- */
	// Method to fetch all products in the database using pagination concept
	@Override
	@Transactional
	@Cacheable(value = "listing", key = "{#pageNumber, #pageSize}")
	public List<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		//Page<Products> productPage = this.productDao.findAll(page);
		List<Products> productsList = this.productDao.findAllProducts(pageNumber*pageSize, pageSize);
		List<Object[]> colorsAndSizesList = this.productDao.findUniqueColorsAndSizesForProducts(productsList);
		List<ProductDto> productDtoList = this.getProductColorAndSize(productsList, colorsAndSizesList);
		return productDtoList;
	}

	//Method to get a productDto list containing details of a product and its corresponding available color and sizes
	@Override
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
					product.getProductDescription(), product.getImageUrl(), product.getProductCategoryName(),
					product.getProductTargetName(), product.getProductCreationTime(),
					product.getProductUpdationTime(), new ArrayList<>(colors), new ArrayList<>(sizes));

			productDtoList.add(productDto);
		}

		return productDtoList;
	}

	// //Method to fetch a product list according to various filters
	@Override
	@Transactional
	@Cacheable(value = "listing", key = "{#pageNumber, #pageSize, #targetName, #categoryName, #colorName}")
	public List<ProductDto> getAllProductsWithFilter(Integer pageNumber, Integer pageSize, String targetName,
			String categoryName, String colorName) {
		if(targetName.isBlank()) {
			targetName = null;
		}
		else {
			targetName = targetName.toLowerCase();
		}
		
		
		if(categoryName.isBlank()) {
			categoryName = null;
		}else {
			categoryName = categoryName.toLowerCase();
		}
		
		
		Integer colorId = null;
		if(!colorName.isBlank()) {
			Color color = this.colorDao.findColorByColorNameIgnoreCase(colorName);
			if (color != null) colorId = color.getColorId();
		}
		
		Pageable page = PageRequest.of(pageNumber, pageSize);
		Page<Products> productPage = this.productDao.findProductsByFilters(categoryName, targetName, colorId, page);
		List<Products> productsList = productPage.getContent();
		List<Object[]> colorsAndSizesList = this.productDao.findUniqueColorsAndSizesForProducts(productsList);
		List<ProductDto> productDtoList = this.getProductColorAndSize(productsList, colorsAndSizesList);
		return productDtoList;
	}

	/*
	 * //Method to add a product in the database
	 * 
	 * @Override public ProductDto addAProduct(ProductDto dto) { Products product =
	 * this.convertToProducts(dto); Products savedProduct =
	 * this.productDao.save(product);
	 * 
	 * product = this.productDao.getReferenceById(savedProduct.getProductId()); dto
	 * = this.convertToDto(product); return dto; }
	 */

	// Method to fetch a product from database using product id
	@Override
	@Cacheable(value = "product", key = "#productId")
	@Transactional
	public ProductDto getAProduct(Long productId) {
		Products product = this.productDao.findById(productId).orElse(null);
		if(product == null) return null;
		List<Object[]> colorsAndSizesList = this.productDao.findUniqueColorsAndSizesForAProduct(product);
		ProductDto productDto = this.getProductDtoWithColorAndSize(product, colorsAndSizesList);
		return productDto;
	}

	//Method to get a product dto with all the necessary details of color and size corresponding to a product
	@Override
	public ProductDto getProductDtoWithColorAndSize(Products product, List<Object[]> colorsAndSizesList) {
		Map<Long, Set<String>> colorsMap = new HashMap<>();
		Map<Long, Set<String>> sizesMap = new HashMap<>();

		for (Object[] colorAndSize : colorsAndSizesList) {
			Long productId = (Long) colorAndSize[0];
			String colorName = (String) colorAndSize[1];
			String sizeName = (String) colorAndSize[2];

			colorsMap.computeIfAbsent(productId, k -> new HashSet<>()).add(colorName);
			sizesMap.computeIfAbsent(productId, k -> new HashSet<>()).add(sizeName);
		}

		Long productId = product.getProductId();
		Set<String> colors = colorsMap.getOrDefault(productId, Collections.emptySet());
		Set<String> sizes = sizesMap.getOrDefault(productId, Collections.emptySet());

		ProductDto productDto = new ProductDto(product.getProductId(), product.getProductName(),
					product.getProductDescription(), product.getImageUrl(), product.getProductCategoryName(),
					product.getProductTargetName(), product.getProductCreationTime(),
					product.getProductUpdationTime(), new ArrayList<>(colors), new ArrayList<>(sizes));


		return productDto;
	}


	// Method to fetch the product list according to the name and other constraints provided in the search
	// 
	@Override
	@Transactional
	@Cacheable(value = "listing", key = "{#pageNumber, #pageSize, #productName, #targetName, #categoryName, #colorName}")
	public List<ParentDto> getAllProductsWithName(Integer pageNumber, Integer pageSize, String productName, String targetName, String categoryName, String colorName) {
		
		if(targetName.isBlank()) {
			targetName = null;
		}else {
			targetName = targetName.toLowerCase();
		}
		
		if(categoryName.isBlank()) {
			categoryName = null;
		}else {
			categoryName = categoryName.toLowerCase();
		}
		
		Integer colorId;
		if(colorName.isBlank()) {
			colorId = null;
		}else {
			Color color = this.colorDao.findColorByColorNameIgnoreCase(colorName);
			if (color != null) colorId = color.getColorId();
			else colorId = 0;
		}
		
		Pageable page = PageRequest.of(pageNumber, pageSize);
		Page<Products> productPage = this.productDao.findProductsByName(page, productName.toLowerCase(), categoryName, targetName, colorId);
		List<Products> productsList = productPage.getContent();
		List<Object[]> colorsAndSizesList = this.productDao.findUniqueColorsAndSizesForProducts(productsList);
		List<ProductDto> productDtoList = this.getProductColorAndSize(productsList, colorsAndSizesList);
		List<ParentDto> resultList;
		if(colorName.isBlank()) {
			resultList = new ArrayList<>(productDtoList);
		}else {
			List<Variant> variantList = this.getVariantList(productDtoList, colorName);
			resultList = new ArrayList<>(variantList);
		}
		return resultList;
	}

	private List<Variant> getVariantList(List<ProductDto> productDtoList, String colorName) {
		List<Variant> resultList = new ArrayList<>();
		for(ProductDto dto : productDtoList) {
			Variant variant = new Variant(dto.getProductId(), dto.getProductName(), dto.getProductDescription(), 
					dto.getImageUrl(), dto.getProductCategoryName(), dto.getProductTargetName(),
					dto.getProductCreationDateTime(), dto.getProductUpdationTime(),
					colorName, dto.getSizeList());
			resultList.add(variant);
		}
		return resultList;
	}

	//Method to delete a product according to its id
	@Override
	@Transactional
	@CacheEvict(value = "product", key = "#productId")
	@Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
	public String deleteProduct(Long productId) {
		try {
			Products product = this.productDao.findById(productId).orElse(null);
			
			if(product == null) {
				return "Product doesn't exists!";
			}else {
				this.productDao.deleteByProductId(productId);
				this.variantsDao.deleteByProducts_ProductId(productId);
				
				cacheManager.getCache("listing").clear();
				return "Success!";
			}
		}catch (ObjectOptimisticLockingFailureException e) {
			System.out.println("----------------> Optimistic locking exception occurred...");
			System.out.println("----------------> Retrying....");
			throw e;
		}catch(Exception e) {
			System.out.print("------------------> Some new exception occured!");
			e.printStackTrace();
		}
		return null;
		
	}

	//Method to update a product according to its id
	@Override
	@Transactional
	@CachePut(value = "product", key = "#productId")
	@Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
	public ProductDto updateProduct(Long productId, ProductUpdateDto productDto) {
		try {
			Products products = this.productDao.findById(productId).orElse(null);
			if(products == null) {
				//Handle exception here
				return null;
			}else {
				String categoryName = "";
				String targetName = "";
				
				System.out.println("-----> Inside else in update product....");
				Category category = this.categoryDao.findById(productDto.getCategoryId()).orElse(null);
				if(category != null) {
					categoryName = category.getCategoryName();
				}
				
				Target target = this.targetDao.findById(productDto.getTargetId()).orElse(null);
				if(target != null) {
					targetName = target.getTargetName();
				}
				
				products.setProductName(productDto.getProductName());
				products.setProductDescription(productDto.getProductDescription());
				products.setImageUrl(productDto.getImageUrl());
				products.setCategory(category);
				products.setTarget(target);
				products.setProductCategoryName(categoryName);
				products.setProductTargetName(targetName);
				Products savedProducts = this.productDao.save(products);
				
				
				if(savedProducts == null) {
					//Handle Exception here
				}
				List<Object[]> colorsAndSizesList = this.productDao.findUniqueColorsAndSizesForAProduct(savedProducts);
				ProductDto resultDto = this.getProductDtoWithColorAndSize(savedProducts, colorsAndSizesList);
				try {
					Thread.sleep(300000);
				}catch (InterruptedException e) {
					// TODO: handle exception
				}
				cacheManager.getCache("listing").clear();
				cacheManager.getCache("readProduct").clear();
				return resultDto;
			}
		}catch(ObjectOptimisticLockingFailureException e) {
			System.out.println("----------------> Optimistic locking exception occurred...");
			System.out.println("----------------> Retrying...");
			throw e;
		}catch(Exception e) {
			System.out.print("------------------> Some new exception occured!");
			e.printStackTrace();
		}
		return null;
	}

	//Method to add a product to the mysql-db
	@Override
	public Products addProductDB(ProductUpdateDto dto) {
		Category productCategory = this.categoryDao.findById(dto.getCategoryId()).orElse(null);
		Target productTarget = this.targetDao.findById(dto.getTargetId()).orElse(null);
		
		String targetName = "";
		String categoryName = "";
		if(productCategory!=null && productTarget!=null) {
			targetName = productTarget.getTargetName();
			categoryName = productCategory.getCategoryName();
		}
 		Products product = new Products(dto.getProductName(), dto.getProductDescription(), dto.getImageUrl(),
 				productCategory, productTarget, categoryName, targetName);
 		
 		Products savedProduct = this.productDao.save(product);
 		if(savedProduct == null) {
 			//Throw and handle exception
 		}
 		return savedProduct;
	}

	/*
	 * //Method to convert product-dto to product-entity
	 * 
	 * @Override public Products convertToProducts(ProductDto dto) { Products
	 * product = new Products(); product.setProductName(dto.getProductName());
	 * product.setProductDescription(dto.getProductDescription());
	 * product.setImageUrl(dto.getImageUrl());
	 * 
	 * Category category =
	 * this.categoryDao.getReferenceById(dto.getProductCategoryId()); Target target
	 * = this.targetDao.getReferenceById(dto.getProductTargetId());
	 * 
	 * 
	 * product.setCategory(category); product.setTarget(target); return product; }
	 */
}
