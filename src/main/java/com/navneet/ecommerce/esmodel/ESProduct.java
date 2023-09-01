package com.navneet.ecommerce.esmodel;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.InnerField;

@Document(indexName = "ecommerce_products")
public class ESProduct {

    @Id
    @Field(type = FieldType.Long, name = "product_id")
    private Long productId;

    @MultiField(mainField = @Field(type = FieldType.Text, name = "product_categoryName"), otherFields = {
            @InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 100)
    })
    private String productCategoryName;

    @Field(type = FieldType.Date, name = "product_creationDateTime")
    private String productCreationDateTime;

    @Field(type = FieldType.Text, name = "product_description")
    private String productDescription;

    @Field(type = FieldType.Keyword, name = "product_imageURL")
    private String productImageURl;

    @Field(type = FieldType.Text, name = "product_name")
    private String productName;

    @MultiField(mainField = @Field(type = FieldType.Text, name = "product_targetName"), otherFields = {
            @InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 100)
    })
    private String productTargetName;

    @Field(type = FieldType.Date, name = "product_updationDateTime")
    private String productUpdationDateTime;

    @Field(type = FieldType.Nested)
    private List<Variants> variants;
    
    
    public static class Variants {

        @MultiField(mainField = @Field(type = FieldType.Text, name = "colorName"), otherFields = {
                @InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 100)
        })
        private String colorName;

        @Field(type = FieldType.Date, name = "creation_dateTime")
        private String creationDateTime;

        @Field(type = FieldType.Float, name = "price")
        private Float price;

        @Field(type = FieldType.Integer, name = "quantity")
        private Integer quantity;

        @MultiField(mainField = @Field(type = FieldType.Text, name = "sizeName"), otherFields = {
                @InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 100)
        })
        private String sizeName;

        @Field(type = FieldType.Long, name = "skuId")
        private Long skuId;

        @Field(type = FieldType.Date, name = "updation_dateTime")
        private String updation_dateTime;
        
        //Default Constructor
        public Variants() {
			// TODO Auto-generated constructor stub
		}
        
        //Parameterized Constructor
		public Variants(String colorName, String creationDateTime, Float price, Integer quantity, String sizeName,
				Long skuId, String updation_dateTime) {
			this.colorName = colorName;
			this.creationDateTime = creationDateTime;
			this.price = price;
			this.quantity = quantity;
			this.sizeName = sizeName;
			this.skuId = skuId;
			this.updation_dateTime = updation_dateTime;
		}

		// Getters and setters
		public String getColorName() {
			return colorName;
		}

		public void setColorName(String colorName) {
			this.colorName = colorName;
		}

		public String getCreationDateTime() {
			return creationDateTime;
		}

		public void setCreationDateTime(String creationDateTime) {
			this.creationDateTime = creationDateTime;
		}

		public Float getPrice() {
			return price;
		}

		public void setPrice(Float price) {
			this.price = price;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}

		public String getSizeName() {
			return sizeName;
		}

		public void setSizeName(String sizeName) {
			this.sizeName = sizeName;
		}

		public Long getSkuId() {
			return skuId;
		}

		public void setSkuId(Long skuId) {
			this.skuId = skuId;
		}

		public String getUpdation_dateTime() {
			return updation_dateTime;
		}

		public void setUpdation_dateTime(String updation_dateTime) {
			this.updation_dateTime = updation_dateTime;
		}
        
    }

    //Getters and Setters for ESProduct
	public Long getProductId() {
		return productId;
	}


	public void setProductId(Long productId) {
		this.productId = productId;
	}


	public String getProductCategoryName() {
		return productCategoryName;
	}


	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}


	public String getProductCreationDateTime() {
		return productCreationDateTime;
	}


	public void setProductCreationDateTime(String productCreationDateTime) {
		this.productCreationDateTime = productCreationDateTime;
	}


	public String getProductDescription() {
		return productDescription;
	}


	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}


	public String getProductImageURl() {
		return productImageURl;
	}


	public void setProductImageURl(String productImageURl) {
		this.productImageURl = productImageURl;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getProductTargetName() {
		return productTargetName;
	}


	public void setProductTargetName(String productTargetName) {
		this.productTargetName = productTargetName;
	}


	public String getProductUpdationDateTime() {
		return productUpdationDateTime;
	}


	public void setProductUpdationDateTime(String productUpdationDateTime) {
		this.productUpdationDateTime = productUpdationDateTime;
	}


	public List<Variants> getVariants() {
		return variants;
	}


	public void setVariants(List<Variants> variants) {
		this.variants = variants;
	}
    
    
}
