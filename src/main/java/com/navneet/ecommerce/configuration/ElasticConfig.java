 package com.navneet.ecommerce.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
public class ElasticConfig {
	
	@Bean
	RestClient getRestClient() {
		RestClient restClient = RestClient.builder(
				new HttpHost("localhost", 9200)).build();
		return restClient;
	}
	
	@Bean
	ElasticsearchTransport getElasticsearchTransport() {
		return new RestClientTransport(getRestClient(), new JacksonJsonpMapper());
	}

	@Bean
	ElasticsearchClient getElasticsearchClient() {
		ElasticsearchClient client = new ElasticsearchClient(getElasticsearchTransport());
		return client;
	}
	
	
}
