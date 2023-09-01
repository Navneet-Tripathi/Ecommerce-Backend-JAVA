package com.navneet.ecommerce.esrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.navneet.ecommerce.esmodel.ESProduct;

public interface ESProductRepo extends ElasticsearchRepository<ESProduct, Long>{

}
