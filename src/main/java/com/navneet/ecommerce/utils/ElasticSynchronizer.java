package com.navneet.ecommerce.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.navneet.ecommerce.dto.ProductDto;
import com.navneet.ecommerce.entities.ProductVariants;
import com.navneet.ecommerce.entities.Products;
import com.navneet.ecommerce.esmodel.ESProduct;
import com.navneet.ecommerce.esmodel.Product;
import com.navneet.ecommerce.esmodel.Variant;
import com.navneet.ecommerce.esrepository.ESProductRepo;
import com.navneet.ecommerce.esrepository.ProductRepo;
import com.navneet.ecommerce.esrepository.VariantRepo;
import com.navneet.ecommerce.repository.ProductDao;
import com.navneet.ecommerce.repository.ProductVariantsDao;
import com.navneet.ecommerce.services.ProductServices;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class ElasticSynchronizer implements CommandLineRunner{

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductVariantsDao variantsDao;
	
	@Autowired
	private ESProductRepo esProductRepo;

	@Autowired
	private ProductRepo productEsRepo;

	@Autowired
	private VariantRepo variantEsRepo;

	@Autowired
	private ProductServices productServices;

	private static final Logger LOG = LoggerFactory.getLogger(ElasticSynchronizer.class);

	/*
	@Override
	public void run(String... args) throws Exception{
		Products product = this.productDao.findById(21934L).orElse(null);
		LOG.info("Syncing Product - {}", product.getProductId());
		//Fetching the list of all the variants for each product
		List<ProductVariants> productVariants = this.variantsDao.findByProducts_ProductId(product.getProductId());
		
		//Creating a list of the variants for a product
		List<ESProduct.Variants> esVariantList = new ArrayList<>() ;
		for(ProductVariants variants : productVariants) {
			//Formatting LocalDateTime to String 
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			String variantCreationDateTime = variants.getProductUpdationDateTime().format(dateTimeFormatter);
			String variantUpdationDateTime = variants.getProductCreationDateTime().format(dateTimeFormatter);
			
			ESProduct.Variants esVariant = new ESProduct.Variants(variants.getColor().getColorName(), variantCreationDateTime,
					variants.getVariantPrice().floatValue(), variants.getProductQuantity(), variants.getSize().getSizeName(), 
					variants.getSkuId(), variantUpdationDateTime);
			esVariantList.add(esVariant);
			
		}
		
		//Fetching all the fields that are to be written in ESProduct
		Long productId = product.getProductId();
		String productName = product.getProductName();
		String productDescription = product.getProductDescription();
		String productImageURL = product.getImageUrl();
		String productCategoryName = product.getProductCategoryName();
		String productTargetName = product.getProductTargetName();
		
		// Formatting LocalDateTime to String 
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		String productCreationDateTime = product.getProductCreationTime().format(dateTimeFormatter);
		String productUpdationDateTime = product.getProductUpdationTime().format(dateTimeFormatter);
		
		ESProduct esProduct = new ESProduct();
		esProduct.setProductId(productId);
		esProduct.setProductCategoryName(productCategoryName);
		esProduct.setProductName(productName);
		esProduct.setProductDescription(productDescription);
		esProduct.setProductImageURl(productImageURL);
		esProduct.setProductTargetName(productTargetName);
		esProduct.setProductCreationDateTime(productCreationDateTime);
		esProduct.setProductUpdationDateTime(productUpdationDateTime);
		esProduct.setVariants(esVariantList);
		
		esProductRepo.save(esProduct);
	}
	*/
	
	
	@Override
	public void run(String... args) throws Exception {
		int batchSize = 1000;
		int pageNumber = 328;
		boolean continueProcessing = true;
		LOG.info("Starting to import data for the first time!");
		do {
			Pageable page = PageRequest.of(pageNumber, batchSize);
			List<Products> productList;
			LOG.info("Starting to import data for pageNumber: "+ pageNumber +"\tand batchSize: "+ batchSize);
			Page<Products> productPage = productDao.findAll(page);
			productList = productPage.getContent();
			

			if (productList.isEmpty()) {
				LOG.info("No more products to sync.");
				continueProcessing = false;
			} else {
				LOG.info("Syncing batch of products - Page: {}", pageNumber);
				this.processProductBatch(productList);
				pageNumber++;
			}
		} while (continueProcessing);
		
	}

	
	private void processProductBatch(List<Products> productList) {
		for (Products product : productList) {
			LOG.info("Syncing Product - {}", product.getProductId());
			//Fetching the list of all the variants for each product
			List<ProductVariants> productVariants = this.variantsDao.findByProducts_ProductId(product.getProductId());
			
			//Creating a list of the variants for a product
			List<ESProduct.Variants> esVariantList = new ArrayList<>() ;
			if(productVariants.size() != 0) {
				for(ProductVariants variants : productVariants) {
					//Formatting LocalDateTime to String 
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
					String variantCreationDateTime = variants.getProductUpdationDateTime().format(dateTimeFormatter);
					String variantUpdationDateTime = variants.getProductCreationDateTime().format(dateTimeFormatter);
					
					ESProduct.Variants esVariant = new ESProduct.Variants(variants.getColor().getColorName(), variantCreationDateTime,
							variants.getVariantPrice().floatValue(), variants.getProductQuantity(), variants.getSize().getSizeName(), 
							variants.getSkuId(), variantUpdationDateTime);
					esVariantList.add(esVariant);
					
				}
			}
			
			
			//Fetching all the fields that are to be written in ESProduct
			Long productId = product.getProductId();
			String productName = product.getProductName();
			String productDescription = product.getProductDescription();
			String productImageURL = product.getImageUrl();
			String productCategoryName = product.getProductCategoryName();
			String productTargetName = product.getProductTargetName();
			// Formatting LocalDateTime to String 
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			String productCreationDateTime = product.getProductCreationTime().format(dateTimeFormatter);
			String productUpdationDateTime = product.getProductUpdationTime().format(dateTimeFormatter);
			
			ESProduct esProduct = new ESProduct();
			esProduct.setProductId(productId);
			esProduct.setProductCategoryName(productCategoryName);
			esProduct.setProductName(productName);
			esProduct.setProductDescription(productDescription);
			esProduct.setProductImageURl(productImageURL);
			esProduct.setProductTargetName(productTargetName);
			esProduct.setProductCreationDateTime(productCreationDateTime);
			esProduct.setProductUpdationDateTime(productUpdationDateTime);
			esProduct.setVariants(esVariantList);
			
			esProductRepo.save(esProduct);
		}
	}

	
	/*
	@Transactional
	public void sync() {
		LOG.info("Start Syncing - {}", LocalDateTime.now());
		this.syncProducts();
		LOG.info(" End Syncing - {}", LocalDateTime.now());
	}

	private void syncProducts() {
		int batchSize = 100;
		int pageNumber = 0;
		Specification<Products> productSpecification = (root, criteriaQuery,
				criteriaBuilder) -> getProductsModificationDatePredicate(criteriaBuilder, root);
		boolean continueProcessing = true;
		boolean firstTimeEntry = true;
		
		if(productEsRepo.count() != 0) {
			firstTimeEntry = false;
		}
		do {
			Pageable page = PageRequest.of(pageNumber, batchSize);
			List<Products> productList;
			if (firstTimeEntry) {
				LOG.info("No documents found in product index, thus we are importing for the first time!");
				Page<Products> productPage = productDao.findAll(page);
				productList = productPage.getContent();
			} else {
				LOG.info("Few documents found in product index, thus inside else!");
				Page<Products> productPage = productDao.findAll(productSpecification, page);
				productList = productPage.getContent();
			}

			if (productList.isEmpty()) {
				LOG.info("No more products to sync.");
				continueProcessing = false;
			} else {
				LOG.info("Syncing batch of products - Page: {}", pageNumber);
				this.processProductBatch(productList);
				pageNumber++;
			}
		} while (continueProcessing);
	}

	private static Predicate getProductsModificationDatePredicate(CriteriaBuilder cb, Root<Products> root) {
		Expression<LocalDateTime> currentTime = cb.currentTimestamp().as(LocalDateTime.class);
		Expression<LocalDateTime> currentTimeMinus = cb.literal(
			    LocalDateTime.now().minusNanos(Constants.INTERVAL_IN_MILLISECONDE * 1_000_000L)
			);
		return cb.between(root.<LocalDateTime>get(Constants.MODIFICATION_DATE), currentTimeMinus, currentTime);
	}
	*/
}
