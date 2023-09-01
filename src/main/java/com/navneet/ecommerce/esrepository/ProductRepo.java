package com.navneet.ecommerce.esrepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.navneet.ecommerce.esmodel.Product;

public interface ProductRepo extends ElasticsearchRepository<Product, Long>{
	@Query("{\"wildcard\": {\"productName\": {\"value\": \"*?0*\", \"case_insensitive\": true}}}")
	Page<Product> findByProductName(String productName, Pageable page);
	
	 @Query("{\"bool\":{" +
	           "\"must\":[" +
	           "?#{#category != null ? {\"term\":{\"productCategoryName.keyword\":{\"value\":\"?0\",\"case_insensitive\":true}}}} : null," +
	           "?#{#target != null ? {\"term\":{\"productTargetName.keyword\":{\"value\":\"?1\",\"case_insensitive\":true}}}} : null," +
	           "?#{#name != null ? {\"wildcard\":{\"productName\":{\"value\":\"?2\",\"case_insensitive\":true}}}} : null" +
	           "]" +
	           "}}")
	 Page<Product> findProductsByConditions(String category, String target, String name, Pageable page);

}
