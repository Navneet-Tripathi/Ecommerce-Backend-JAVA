package com.navneet.ecommerce.esrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.navneet.ecommerce.esmodel.Product;

public interface ProductRepo extends ElasticsearchRepository<Product, Long>{

}
