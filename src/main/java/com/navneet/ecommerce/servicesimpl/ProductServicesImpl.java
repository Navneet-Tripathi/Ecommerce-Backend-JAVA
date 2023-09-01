package com.navneet.ecommerce.servicesimpl;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.springframework.data.domain.Sort;
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
import com.navneet.ecommerce.esmodel.ESProduct;
import com.navneet.ecommerce.esmodel.Product;
import com.navneet.ecommerce.esrepository.ESProductRepo;
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
	private ESProductRepo esProductRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	private Logger logger = LoggerFactory.getLogger(ProductServicesImpl.class);
	
	/* ---------------------------------------------- ES OPERATIONS ---------------------------------------------- */
	//Method to fetch all the products from elastic search
	@Override
	public List<ProductDto> getProducts(Integer pageNumber, Integer pageSize){
		Sort sort = Sort.by(Sort.Direction.ASC, "productId"); //Sort the result set in ascending order w.r.t id
		Pageable page = PageRequest.of(pageNumber, pageSize, sort);
		Page<ESProduct> productPage = this.esProductRepo.findAll(page);
		List<ESProduct> productList = productPage.getContent();
		List<ProductDto> dtoList = this.convertToDtoList(productList);
		return dtoList;
	}
	
	//Method to get a list of all the product with name
	@Override
	public List<ParentDto> getESProductsWithFilters(Integer pageNumber, Integer pageSize, String productName, String productTargetName, String productCategoryName) {
		/*
		if(productName.length() == 0) productName = null;
		if(productCategoryName.length() == 0) productCategoryName = null;
		if(productTargetName.length() == 0) productTargetName = null;
		
		
		
		List<ParentDto> resultList;
		Sort sort = Sort.by(Sort.Direction.ASC, "productId"); //Sort the result set in ascending order w.r.t id
		Pageable page = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> productPage = this.productRepo.findProductsByConditions(productCategoryName, productTargetName, productName, page);
		List<Product> productList = productPage.getContent();
		List<ProductDto> productDtoList = this.convertToDtoList(productList);
		resultList = new ArrayList<>(productDtoList);
		return resultList;
		*/
		return null;
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
	public List<ProductDto> convertToDtoList(List<ESProduct> productList) {
		List<ProductDto> dtoList = new ArrayList<>();
		for(ESProduct product: productList) {
			ProductDto dto = this.convertESProductToDto(product);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	//Method to delete a product entity from elastic search
	@Override
	@Transactional
	public String deleteESProduct(Long productId) {
		String dbStatus = this.deleteProduct(productId);
		
		if(!dbStatus.equalsIgnoreCase("success!")) {
			//Throw and handle exception
		}
		Product esProduct = this.productRepo.findById(productId).orElse(null);
		if(esProduct == null) {
			//Product isn't in the es so no need to delete
			return "Successfully Deleted!";
		}
		this.productRepo.delete(esProduct);
		return "Successfully Deleted!";
	}
	
	//Method to fetch a product from ES using productId
	@Override
	public ProductDto getAESProduct(Long productId) {
		/*
		Product esProduct = this.productRepo.findById(productId).orElse(null);
		if(esProduct == null) {
			return null;
		}else {
			ProductDto resultDto = this.convertESProductToDto(esProduct);
			return resultDto;
		}
		*/
		return null;
	}
	
	//Method to convert ES document to DTO
	@Override
	public ProductDto convertESProductToDto(ESProduct esProduct) {
		//Converting String date to LocalDateTime instance
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime creationDateTime =  LocalDateTime.parse(esProduct.getProductCreationDateTime(), formatter);
        LocalDateTime updationDateTime = LocalDateTime.parse(esProduct.getProductUpdationDateTime(), formatter);
        
        //Fetching list of available color and size for a product
        Set<String> colorSet = new HashSet<>();
        Set<String> sizeSet = new HashSet<>();
        
        List<ESProduct.Variants> esVariantList = esProduct.getVariants();
        for(ESProduct.Variants variant : esVariantList) {
        	colorSet.add(variant.getColorName());
        	sizeSet.add(variant.getSizeName());
        }
		
		ProductDto dto = new ProductDto(esProduct.getProductId(), esProduct.getProductName(), 
				esProduct.getProductDescription(), esProduct.getProductImageURl(), esProduct.getProductCategoryName(),
				esProduct.getProductTargetName(), creationDateTime, updationDateTime, 
				new ArrayList<>(colorSet), new ArrayList<>(sizeSet));
		return dto;
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
