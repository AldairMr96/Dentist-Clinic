package com.company.bazar.controllerTest;
import com.company.bazar.controller.ProductController;
import com.company.bazar.model.Product;
import com.company.bazar.model.SaleProduct;
import com.company.bazar.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
public class ProductControllerTest {
    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    @BeforeEach
    private void setUp (){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createProductControllerTest (){
        List<SaleProduct> saleProducts = new ArrayList<>();
        Product product = new Product(1L, "TV", 120.0, 8L, saleProducts);

        doNothing().when(productService).createProduct(product);

        Product productResult = productController.createProduct(product);

        assertNotNull(productResult);
        assertEquals(product.getCodProduct(), productResult.getCodProduct());
        assertEquals(product.getNameProduct(), productResult.getNameProduct());
        assertEquals(product.getCost(), productResult.getCost());
        assertEquals(product.getStock(), productResult.getStock());
        assertTrue(saleProducts.isEmpty());
    }

    @Test
    public  void getProductEmptyTest (){
        when(productService.getProducts()).thenReturn(Collections.emptyList());

        List<Product> products = productController.getProduct();

        assertTrue(products.isEmpty());
        verify(productService, times(1)).getProducts();


    }
    @Test
    public void getProductTest(){
        List<Product> products = List.of(
                new Product(1L, "TV", 100.0, 5L, new ArrayList<>() ),
                new Product(2L, "Mouse", 2.0, 10L, new ArrayList<>())
        );
        when(productService.getProducts()).thenReturn(products);

        List<Product> results = productController.getProduct();

        assertFalse(results.isEmpty());
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    public void deleteProductTest (){
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<?> responseEntity = productController.deleteProduct(1L);

        assertNotNull(responseEntity);
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals("Delete Product susccessfully", responseEntity.getBody());
        verify(productService, times(1)).deleteProduct(1L);
    }
    @Test
    public  void  deleteProductNotFoundTest(){
        doThrow( new EntityNotFoundException("Product not found")).when(productService).deleteProduct(1L);

        ResponseEntity<?> responseEntity = productController.deleteProduct(1L);

        assertNotNull(responseEntity);
        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Product not found", responseEntity.getBody());
        verify(productService, times(1)).deleteProduct(1L);
    }
    @Test
    public  void  deleteProductServerErrorTest(){
        doThrow( new RuntimeException("Server internal Error")).when(productService).deleteProduct(1L);

        ResponseEntity<?> responseEntity = productController.deleteProduct(1L);

        assertNotNull(responseEntity);
        assertEquals(INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Server internal Error", responseEntity.getBody());
        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void editProductSuccessTest() {
        List<SaleProduct> saleProducts = new ArrayList<>();
        Product product = new Product(1L, "TV", 120.0, 8L, saleProducts);


        doNothing().when(productService).editProduct(product);


        ResponseEntity<?> response = productController.editProduct(product);


        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(product, response.getBody());


        verify(productService, times(1)).editProduct(product);
    }

    @Test
    void editProductNotFoundTest() {
        List<SaleProduct> saleProducts = new ArrayList<>();
        Product product = new Product(1L, "TV", 120.0, 8L, saleProducts);


        doThrow(new EntityNotFoundException("Product not found")).when(productService).editProduct(product);


        ResponseEntity<?> response = productController.editProduct(product);


        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found", response.getBody());


        verify(productService, times(1)).editProduct(product);
    }

    @Test
    void editProductServerErrorTest() {
        List<SaleProduct> saleProducts = new ArrayList<>();
        Product product = new Product(1L, "TV", 120.0, 8L, saleProducts);


        doThrow(new RuntimeException("Server internal Error")).when(productService).editProduct(product);


        ResponseEntity<?> response = productController.editProduct(product);

        // Verificar el resultado
        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());


        verify(productService, times(1)).editProduct(product);
    }

    @Test
    public void findProductTest (){
        Long idTest =1L;
        Product product =new Product(1L, "TV", 100.0, 5L, new ArrayList<>());

        when(productService.findProduct(idTest)).thenReturn(product);

        ResponseEntity<?> responseEntity = productController.findClient(idTest);

        assertNotNull(responseEntity);
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(product, responseEntity.getBody());
        verify(productService, times(1)).findProduct(idTest);
    }
    @Test
    public void findProductNotFoundTest (){
        Long idTest =1L;
        when(productService.findProduct(idTest)).thenThrow( new EntityNotFoundException("Product not found"));

        ResponseEntity<?> responseEntity = productController.findClient(idTest);

        assertNotNull(responseEntity);
        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Product not found", responseEntity.getBody());
        verify(productService, times(1)).findProduct(idTest);
    }
    @Test
    public void findProductInternalErrorTest (){
        Long idTest =1L;
        when(productService.findProduct(idTest)).thenThrow( new RuntimeException("Server internal Error"));

        ResponseEntity<?> responseEntity = productController.findClient(idTest);

        assertNotNull(responseEntity);
        assertEquals(INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Server internal Error", responseEntity.getBody());
        verify(productService, times(1)).findProduct(idTest);
    }

    @Test
    public  void avaliableStockMoreFiveTest (){


        when(productService.availableStock()).thenReturn(Collections.emptyList());

        List<Product> result = productController.getAvaliableStock();

        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(productService, times(1)).availableStock();


    }

    @Test
    public void avaliableStockLessFive (){
        List<Product> products = List.of(
                new Product(1L, "TV", 100.0, 4L, new ArrayList<>() ),
                new Product(2L, "Mouse", 2.0, 1L, new ArrayList<>())
        );

        when(productService.availableStock()).thenReturn(products);

        List<Product> result = productController.getAvaliableStock();

        assertEquals(2, result.size());
        assertTrue(!result.isEmpty());
        verify(productService, times(1)).availableStock();
    }
}
