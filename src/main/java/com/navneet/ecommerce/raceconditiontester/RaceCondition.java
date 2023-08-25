package com.navneet.ecommerce.raceconditiontester;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.navneet.ecommerce.dto.ProductDto;
import com.navneet.ecommerce.dto.ProductUpdateDto;
import com.navneet.ecommerce.services.ProductServices;


@Component
public class RaceCondition {
	
	@Autowired
	private ProductServices productServices;
	
	public void run() {
		Thread updateThread1 = new Thread(() -> {
			System.out.println("------------- Update Product method called! UPDATE THREAD 1 -----------------");
			ProductUpdateDto inputDto = new ProductUpdateDto("Nike VaporFly1", "Description for Nike VaporFly1",
					"Image URL for Nike VaporFly1", 1, 1);
			
			ProductDto dto = this.productServices.updateProduct(1L, inputDto);
			if(dto == null) {
            	System.out.println("Returned object by "+ Thread.currentThread().getId() +"'s updateProductMethod() THREAD 1 is null! ");
            }else {
            	System.out.println("Object returned by "+ Thread.currentThread().getId() +"'s updateProductMethod() THRAD 1 isn't null!\nThe value is : "+ dto.toString());
            }
        });
		
		Thread updateThread2 = new Thread(() -> {
			System.out.println("------------- Update Product method called! UPDATE THREAD 2-----------------");
			ProductUpdateDto inputDto = new ProductUpdateDto("Nike HyperVenom3", "Description for Nike HyperVenom3",
					"Image URL for Nike HyperVenom3", 1, 1);
			
			ProductDto dto = this.productServices.updateProduct(1L, inputDto);
			if(dto == null) {
            	System.out.println("Returned object by "+ Thread.currentThread().getId() +"'s updateProductMethod() THREAD 2 is null! ");
            }else {
            	System.out.println("Object returned by "+ Thread.currentThread().getId() +"'s updateProductMethod() THREAD 2 isn't null!\nThe value is : "+ dto.toString());
            }
        });
		
		Thread updateThread3 = new Thread(() -> {
			System.out.println("------------- Update Product method called! UPDATE THREAD 3 -----------------");
			ProductUpdateDto inputDto = new ProductUpdateDto("Nike VaporFly2", "Description for Nike VaporFly2",
					"Image URL for Nike VaporFly2", 1, 1);
			
			ProductDto dto = this.productServices.updateProduct(1L, inputDto);
			if(dto == null) {
            	System.out.println("Returned object by "+ Thread.currentThread().getId() +"'s updateProductMethod() THREAD 1 is null! ");
            }else {
            	System.out.println("Object returned by "+ Thread.currentThread().getId() +"'s updateProductMethod() THRAD 1 isn't null!\nThe value is : "+ dto.toString());
            }
        });
		
		Thread updateThread4 = new Thread(() -> {
			System.out.println("------------- Update Product method called! UPDATE THREAD 4 -----------------");
			ProductUpdateDto inputDto = new ProductUpdateDto("Nike VaporFly3", "Description for Nike VaporFly3",
					"Image URL for Nike VaporFly3", 1, 1);
			
			ProductDto dto = this.productServices.updateProduct(1L, inputDto);
			if(dto == null) {
            	System.out.println("Returned object by "+ Thread.currentThread().getId() +"'s updateProductMethod() THREAD 1 is null! ");
            }else {
            	System.out.println("Object returned by "+ Thread.currentThread().getId() +"'s updateProductMethod() THRAD 1 isn't null!\nThe value is : "+ dto.toString());
            }
        });
		
		Thread deleteThread1 = new Thread(() -> {
			System.out.println("------------- Update Product method called! UPDATE THREAD 2-----------------");
			
			String status = this.productServices.deleteProduct(100014L);
			if(status == "Success!") {
				System.out.println("Product Deleted! Thread : "+ Thread.currentThread().getId() +" DELETE THREAD!");
			}else {
				System.out.println(status);
				System.out.println("DELETE THREAD");
			}
        });
		
		
		Thread deleteThread2 = new Thread(() -> {
			System.out.println("------------- Update Product method called! UPDATE THREAD 2-----------------");
			
			String status = this.productServices.deleteProduct(100014L);
			if(status == "Success!") {
				System.out.println("Product Deleted! Thread : "+ Thread.currentThread().getId() +" DELETE THREAD!");
			}else {
				System.out.println(status);
				System.out.println("DELETE THREAD");
			}
        });
		

        // Create thread for getAProduct
        Thread getThread = new Thread(() -> {
        	System.out.println("------------- Get Product method called!-----------------");
        	System.out.println("Current Thread  : "+ Thread.currentThread().getId());
            ProductDto dto = this.productServices.getAProduct(1L);
            if(dto == null) {
            	System.out.println("Returned object by "+ Thread.currentThread().getId() +"'s getProductMethod() is null!");
            }else {
            	System.out.println("Object returned by "+ Thread.currentThread().getId() +"'s getProductMethod() isn't null!\nThe value is : "+ dto.toString());
            }
        });

        // Start the threads
        updateThread1.start();
        getThread.start();
        updateThread2.start();
        updateThread3.start();
        updateThread4.start();
        
     
	}
}
