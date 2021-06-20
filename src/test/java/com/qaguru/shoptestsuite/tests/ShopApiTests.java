package com.qaguru.shoptestsuite.tests;

import com.qaguru.shoptestsuite.lib.TestBase;
import com.qaguru.shoptestsuite.models.Product;
import com.qaguru.shoptestsuite.service.ProductService;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class ShopApiTests extends TestBase {

    @Test
    public void addANewProduct(){

        Product product = new Product();
        product.setName("One A Day");
        product.setDescription("Advanced multi-vitamin tablets");
        product.setPrice(12.7f);
        ProductService service = new ProductService(requestSpecification);
        String id = service.createANewRecord(product, HttpStatus.SC_CREATED);

        Product savedRecord = service.getRecord(id, HttpStatus.SC_OK);

        Assert.assertEquals(savedRecord.getName(), product.getName());
        Assert.assertEquals(savedRecord.getDescription(), product.getDescription());

    }
    @Test
    public void updateRecord(){
        Product product = new Product();
        product.setName("One A Day");
        product.setDescription("Advanced multi-vitamin tablets");
        product.setPrice(12.7f);
        ProductService service = new ProductService(requestSpecification);
        String id = service.createANewRecord(product,HttpStatus.SC_CREATED);

        Product productUpdate = new Product();
        productUpdate.setId(UUID.fromString(id));
        productUpdate.setName("Updated Name");
        productUpdate.setDescription("Updated Description");
        productUpdate.setPrice(10.7f);

        service.updateRecord(productUpdate);
        Product updatedRecord = service.getRecord(id, HttpStatus.SC_OK);
        Assert.assertEquals(updatedRecord.getName(), productUpdate.getName());
        Assert.assertEquals(updatedRecord.getDescription(), productUpdate.getDescription());

    }

    @Test
    public void deleteAProduct(){

        Product product = new Product();
        product.setName("One A Day");
        product.setDescription("Advanced multi-vitamin tablets");
        product.setPrice(12.7f);
        ProductService service = new ProductService(requestSpecification);
        String id = service.createANewRecord(product, HttpStatus.SC_CREATED);

        service.deleteRecord(id);
        service.getRecord(id,HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
    @Test
    public void findAllProducts(){
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setName("Adventure Force Hydro Burst Pressurized One Pump Super Shot Water Blaster");
        product1.setDescription("Mega Pressurized Water Blaster");
        product1.setPrice(19.98f);

        Product product2 = new Product();
        product2.setName("Play Day Inflatable Boom Box Float");
        product2.setDescription("Inflatable Boom Box");
        product2.setPrice(15.98f);

        Product product3 = new Product();
        product3.setName("Adventure Force Steady Stream Water Blaster");
        product3.setDescription("V");
        product3.setPrice(9.98f);

        products.add(product1);
        products.add(product2);
        products.add(product3);

        ProductService service = new ProductService(requestSpecification);

        for(Product product:products){
            String id = service.createANewRecord(product, HttpStatus.SC_CREATED);
            product.setId(UUID.fromString(id));
        }
        service.findAllRecords(products);

    }

}
