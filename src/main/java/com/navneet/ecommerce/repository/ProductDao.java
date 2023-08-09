package com.navneet.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.navneet.ecommerce.annotations.TrackResponceTime;
import com.navneet.ecommerce.entities.Products;

import jakarta.persistence.LockModeType;

@TrackResponceTime
public interface ProductDao extends JpaRepository<Products, Long> {
	// Method to fetch a product entity along with its related entities
    @Query(value = "SELECT * FROM products p LEFT JOIN category c ON p.product_categoryid = c.category_id LEFT JOIN target t ON p.product_targetid = t.target_id LIMIT :pageNumber, :pageSize", nativeQuery = true)
	public List<Products> findAllProducts(Integer pageNumber, Integer pageSize);

	// Method to fetch productId, colorName, and sizeName corresponding to a product list
	@Query("SELECT p.productId, c.colorName, s.sizeName FROM Products p "
			+ "LEFT JOIN ProductVariants v ON v.products = p " + "LEFT JOIN Color c ON v.color = c "
			+ "LEFT JOIN Size s ON v.size = s " + "WHERE p IN :products "
			+ "GROUP BY p.productId, c.colorName, s.sizeName")
	public List<Object[]> findUniqueColorsAndSizesForProducts(List<Products> products);
	
	//Method to fetch productId, colorName and sizeName corresponding to a single product
	@Query("SELECT p.productId, c.colorName, s.sizeName FROM Products p "
			+ "LEFT JOIN ProductVariants v ON v.products = p " + "LEFT JOIN Color c ON v.color = c "
			+ "LEFT JOIN Size s ON v.size = s " + "WHERE p = :products "
			+ "GROUP BY p.productId, c.colorName, s.sizeName")
	public List<Object[]> findUniqueColorsAndSizesForAProduct(Products products);


	// Method to fetch product based on some applied filters
	@Query("SELECT p FROM Products p "
			+ "WHERE (:categoryName IS NULL OR LOWER(p.productCategoryName) = :categoryName) "
			+ "AND (:targetName IS NULL OR LOWER(p.productTargetName) = :targetName) "
			+ "AND p IN (SELECT DISTINCT pv.products FROM ProductVariants pv " + "JOIN pv.color clr "
			+ "WHERE (:colorId IS NULL OR clr.colorId = :colorId) )")
	Page<Products> findProductsByFilters(String categoryName, String targetName, Integer colorId, Pageable pageable);

	@Query("SELECT p FROM Products p "
			+ "WHERE (LOWER(p.productName) LIKE %:searchedString% )"
			+ "AND (:categoryName IS NULL OR LOWER(p.productCategoryName) = :categoryName) "
			+ "AND (:targetName IS NULL OR LOWER(p.productTargetName) = :targetName) "
			+ "AND p IN (SELECT DISTINCT pv.products FROM ProductVariants pv " + "JOIN pv.color clr "
			+ "WHERE (:colorId IS NULL OR clr.colorId = :colorId) )")
	public Page<Products> findProductsByName(Pageable page, String searchedString, String categoryName, String targetName,
			Integer colorId);
	
	@Query("Select DISTINCT pv.products from ProductVariants pv "+ "WHERE (LOWER(pv.products.productName) LIKE %:searchedString%)")
	public Page<Products> findProducts(Pageable page, String searchedString);
	
	//Method to delete a product based on the productId
	@Modifying
	@Query("DELETE FROM Products p WHERE p.productId = :productId")
	public void deleteByProductId(Long productId);
	
	//Method to find a product using its id
	@Cacheable(value = "readProduct", key = "#productId")
	Optional<Products> findById(Long productId);
	

}
