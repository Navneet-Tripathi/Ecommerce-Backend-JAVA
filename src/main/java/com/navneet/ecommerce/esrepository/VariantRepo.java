package com.navneet.ecommerce.esrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.navneet.ecommerce.esmodel.Variant;

public interface VariantRepo extends ElasticsearchRepository<Variant, Long>{

}
