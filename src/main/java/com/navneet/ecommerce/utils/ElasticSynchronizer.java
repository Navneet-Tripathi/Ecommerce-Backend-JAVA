package com.navneet.ecommerce.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.navneet.ecommerce.dto.ProductDto;
import com.navneet.ecommerce.entities.ProductVariants;
import com.navneet.ecommerce.entities.Products;
import com.navneet.ecommerce.esmodel.Product;
import com.navneet.ecommerce.esmodel.Variant;
import com.navneet.ecommerce.esrepository.ProductRepo;
import com.navneet.ecommerce.esrepository.VariantRepo;
import com.navneet.ecommerce.repository.ProductDao;
import com.navneet.ecommerce.repository.ProductVariantsDao;
import com.navneet.ecommerce.services.ProductServices;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ElasticSynchronizer {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductVariantsDao variantsDao;

	@Autowired
	private ProductRepo productEsRepo;

	@Autowired
	private VariantRepo variantEsRepo;

	@Autowired
	private ProductServices productServices;

	private static final Logger LOG = LoggerFactory.getLogger(ElasticSynchronizer.class);

	@Scheduled(cron = "0 */3 * * * *")
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

	private void processProductBatch(List<Products> productList) {
		for (Products product : productList) {
			LOG.info("Syncing Product - {}", product.getProductId());
			/* <---- Saving into products index ----> */
			List<Object[]> colorsAndSizesList = this.productDao.findUniqueColorsAndSizesForAProduct(product);
			ProductDto productDto = this.productServices.getProductDtoWithColorAndSize(product, colorsAndSizesList);
			Product esProduct = this.productServices.convertToESProduct(productDto);
			productEsRepo.save(esProduct);
			LOG.info("Indexing in products successful!");

			/* <---- Saving into variants index ----> */
			List<ProductVariants> variantsList = this.variantsDao.findByProducts_ProductId(product.getProductId());
			for (ProductVariants variant : variantsList) {
				LOG.info("Starting indexing in variants.Details : "+variant.getSkuId() +"\t"+variant.getProductCreationDateTime());
				Variant esVariant = new Variant(variant.getSkuId(), product.getProductId(), product.getProductName(),
						product.getProductDescription(), product.getImageUrl(), variant.getVariantPrice().floatValue(),
						variant.getSize().getSizeName(), variant.getColor().getColorName(),
						product.getProductTargetName(), product.getProductCategoryName(),
						variant.getProductCreationDateTime().toString(), variant.getProductUpdationDateTime().toString(),
						variant.getProductQuantity());
				variantEsRepo.save(esVariant);
				LOG.info("Indexing in variants successful!");
			}
		}
	}

	private static Predicate getProductsModificationDatePredicate(CriteriaBuilder cb, Root<Products> root) {
		Expression<LocalDateTime> currentTime = cb.currentTimestamp().as(LocalDateTime.class);
		Expression<LocalDateTime> currentTimeMinus = cb.literal(
			    LocalDateTime.now().minusNanos(Constants.INTERVAL_IN_MILLISECONDE * 1_000_000L)
			);
		return cb.between(root.<LocalDateTime>get(Constants.MODIFICATION_DATE), currentTimeMinus, currentTime);
	}

}
