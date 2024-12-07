package com.company.bazar.serviceTest;

import com.company.bazar.model.Client;
import com.company.bazar.model.Product;
import com.company.bazar.repository.IProductRepository;
import com.company.bazar.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp (){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createProductTest (){
        Product product = new Product(1L, "TV", 700.0, 7L, new ArrayList<>());

        when(productRepository.save(any())).thenReturn((product));
        productService.createProduct(product);

        verify(productRepository, times(1)).save(product);
    }
    @Test
    public void getProductErrorTest (){
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        List<Product> products = productService.getProducts();
        assertEquals(0, products.size());
        assertTrue(products.isEmpty());
    }
    @Test
    public void getProductTest (){
        List<Product>products=List.of(
                new Product(1L, "TV", 700.0, 7L, new ArrayList<>()),
                new Product(2L, "Impresora", 70.0, 5L, new ArrayList<>()));
        when(productRepository.findAll()).thenReturn(products);
        List<Product> result = productService.getProducts();

        assertEquals(2, result.size());
        assertFalse(result.isEmpty());
    }
    @Test
    public void findProductErrorTest (){
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, ()-> productService.findProduct(1L));

    }
    @Test
    public  void findProductTest (){
        Product product= new Product(1L, "TV", 700.0, 7L, new ArrayList<>());

        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        Product result = productService.findProduct(1L);

        assertEquals(700, result.getCost());
        assertTrue(result.getSaleList().isEmpty());
        assertFalse(result.getStock()>9);

    }
    @Test
    public void deleteProductErrorTest (){
        when(productRepository.exists(any())).thenReturn(false);

        assertThrows(RuntimeException.class, ()->productService.deleteProduct(1L));

        verify(productRepository, never()).deleteById(1L);
    }
    @Test
    public void deleteProductTest (){
        when(productRepository.existsById(any())).thenReturn(true);
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void editProductTest (){
        Product product = new Product(1L, "TV", 700.0, 7L, new ArrayList<>());

        when(productRepository.save(any())).thenReturn((product));

        productService.editProduct(product);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    public  void avaliableStockMoreFiveTest(){
        List<Product>products=List.of(
                new Product(1L, "TV", 700.0, 7L, new ArrayList<>()),
                new Product(2L, "Impresora", 70.0, 8L, new ArrayList<>())
        );
        when(this.productService.getProducts()).thenReturn(Collections.emptyList());
        List<Product> productAviables = productService.availableStock();
        assertEquals(0, productAviables.size());
        assertTrue(productAviables.isEmpty());


    }
    @Test
    public  void avaliableStockLestFiveTest(){
        List<Product>products=List.of(
                new Product(1L, "TV", 700.0, 6L, new ArrayList<>()),
                new Product(2L, "Impresora", 70.0, 5L, new ArrayList<>())
        );
        when(this.productService.getProducts()).thenReturn(products);
        List<Product> productAviables = productService.availableStock();
        assertEquals(1, productAviables.size());
        assertFalse(productAviables.isEmpty());


    }

}
