package com.company.bazar.controllerTest;

import com.company.bazar.controller.SaleController;
import com.company.bazar.dto.CreateSaleDTO;
import com.company.bazar.dto.MaxSaleDTO;
import com.company.bazar.dto.ProductSaleDTO;
import com.company.bazar.dto.SaleDTO;
import com.company.bazar.model.Client;
import com.company.bazar.model.Product;
import com.company.bazar.model.Sale;
import com.company.bazar.model.SaleProduct;
import com.company.bazar.repository.ISaleRepository;
import com.company.bazar.service.SaleService;
import com.jayway.jsonpath.internal.function.numeric.Max;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

public class SaleControllerTest {

    @Mock
    SaleService saleService;


    @Mock
    ISaleRepository saleRepository;
    @InjectMocks
    SaleController saleController;

    @BeforeEach
    private void setUp (){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public  void totalSaleTest (){

        LocalDate dateSale = LocalDate.now();

       SaleDTO saleDTO = new SaleDTO(1000.0, 5);

       when(saleService.totalSale(dateSale)).thenReturn(saleDTO);

       SaleDTO result = saleController.findTotalSale(dateSale);

        assertNotNull(result);
        assertEquals(saleDTO.getSumTotalSale(), result.getSumTotalSale());
        assertEquals(saleDTO.getSalesQuantity(), result.getSalesQuantity());
        verify(saleService, times(1)).totalSale(dateSale);


    }

   @Test
    public  void  findMaxSaleTest (){
       MaxSaleDTO maxSaleDTO = new MaxSaleDTO(1L, 100.0, 5,"Aldair", "Martinez");
       when(saleService.saleMajor()).thenReturn(maxSaleDTO);

       MaxSaleDTO result = saleController.findMaxSale();

       assertNotNull(result);
       assertEquals(maxSaleDTO.getCodSale(), result.getCodSale());
       assertEquals(maxSaleDTO.getTotal(), result.getTotal());
       assertEquals(maxSaleDTO.getProductQuantity(), result.getProductQuantity());
       assertEquals(maxSaleDTO.getClientName(), result.getClientName());
       assertEquals(maxSaleDTO.getClientLastname(), result.getClientLastname());
       verify(saleService, times(1)).saleMajor();
    }

    @Test
    public void findProductSaleTest (){
        Long codSale = 1l;
        Product product = new Product(1L, "TV", 700.0, 7L, new ArrayList<>());
        Product product1 = new Product(2L, "Impresora", 70.0, 5L, new ArrayList<>());
        List<SaleProduct> saleProducts = List.of(
                new SaleProduct(1, new Sale(),product, 7),
                new SaleProduct(2, new Sale(), product1, 8)
        );
        List<String> productName = saleProducts.stream()
                .map(saleProduct -> saleProduct.getProduct().getNameProduct())
                .collect(Collectors.toList());
        when(saleService.findProductSale(codSale)).thenReturn(productName);

        List<String> result = saleController.findProductSale(codSale);

        assertNotNull(result);
        assertTrue(!result.isEmpty());
        assertEquals(2, result.size());
        assertInstanceOf(ArrayList.class, result);


    }

    @Test
    public void getSaleControllerTest (){
        Client client = new Client(1L, "Aldair", "Martinez", "1234576", null);


        List<Sale>sales = List.of(
                new Sale(1L, LocalDate.now(),  client, new ArrayList<>(), 1000.0  ),
                new Sale(2L, LocalDate.now(), client, new ArrayList<>(), 2000.0)
        );

        when(saleService.getSales()).thenReturn(sales);

        List<Sale> saleResult = saleController.getSales();

        assertNotNull(saleResult);
        assertTrue(!saleResult.isEmpty());
        assertEquals(2, saleResult.size());
        assertInstanceOf(List.class, saleResult);
        verify(saleService, times(1)).getSales();
    }


    @Test
    public void createControllerTest (){
        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());


        List<ProductSaleDTO> productSaleDTOS = List.of(
                new ProductSaleDTO(1L, 5),
                new ProductSaleDTO(2L, 3));

        CreateSaleDTO saleDTO = new CreateSaleDTO(client.getIdClient(), productSaleDTOS);
        doNothing().when(saleService).createSale(saleDTO);

        ResponseEntity<?> saleResult = saleController.createSale(saleDTO);
        assertNotNull(saleResult);
        assertEquals(OK, saleResult.getStatusCode());
        assertEquals("Sale Create Susccessfully", saleResult.getBody());
        verify(saleService, times(1)).createSale(saleDTO);

    }

    @Test
    public  void  createSaleNotFoundTest(){
        doThrow( new EntityNotFoundException("Sale not found")).when(saleService).createSale(any());

        ResponseEntity<?> responseEntity = saleController.createSale(any());

        assertNotNull(responseEntity);
        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Sale not found", responseEntity.getBody());
        verify(saleService, times(1)).createSale(any());
    }

    @Test
    public  void  createSaleInternalErrorTest(){
        doThrow( new RuntimeException("Internal Server Error")).when(saleService).createSale(any());

        ResponseEntity<?> responseEntity = saleController.createSale(any());

        assertNotNull(responseEntity);
        assertEquals(INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Internal Server Error", responseEntity.getBody());
        verify(saleService, times(1)).createSale(any());
    }
    @Test
    public void deleteSaleControllerTest (){
        doNothing().when(saleService).deleteSale(1L);

        ResponseEntity<?> deleteResult = saleController.deleteSale(1L);

        assertNotNull(deleteResult);
        assertEquals(OK, deleteResult.getStatusCode());
        assertEquals("Delete sale  susccessfully", deleteResult.getBody());
        verify(saleService, times(1)).deleteSale(1L);
    }
    @Test
    public  void  deleteSaleControllerNotFoundTest(){
        doThrow( new EntityNotFoundException("Sale not found")).when(saleService).deleteSale(1L);

        ResponseEntity<?> deleteResult = saleController.deleteSale(1L);

        assertNotNull(deleteResult);
        assertEquals(NOT_FOUND, deleteResult.getStatusCode());
        assertEquals("Sale not found", deleteResult.getBody());
        verify(saleService, times(1)).deleteSale(1L);
    }
    @Test
    public  void  deleteSaleControllerInternalErrorTest(){
        doThrow( new RuntimeException("Server internal Error")).when(saleService).deleteSale(1L);

        ResponseEntity<?> deleteResult = saleController.deleteSale(1L);

        assertNotNull(deleteResult);
        assertEquals(INTERNAL_SERVER_ERROR, deleteResult.getStatusCode());
        assertEquals("Server internal Error", deleteResult.getBody());
        verify(saleService, times(1)).deleteSale(1L);
    }


    @Test
    void editSaleSuccessTest() {

        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());

        Product product = new Product(1L, "TV", 700.0, 7L, new ArrayList<>());
        Product product1 = new Product(2L, "Impresora", 70.0, 5L, new ArrayList<>());

        List<SaleProduct> saleProducts = List.of(
                new SaleProduct(1, new Sale(), product, 7),
                new SaleProduct(2, new Sale(), product1, 8));


        List<ProductSaleDTO> productSaleDTOS = List.of(
                new ProductSaleDTO(1L, 5),
                new ProductSaleDTO(2L, 3));

        CreateSaleDTO saleDTO = new CreateSaleDTO(client.getIdClient(), productSaleDTOS);
        Sale newSale = new Sale(1L,LocalDate.now(), client, saleProducts,10000.0);


        when(saleService.findSale(client.getIdClient())).thenReturn(newSale);
        doNothing().when(saleService).editSale(saleDTO);


        ResponseEntity<?> response = saleController.editSale(saleDTO);


        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(saleDTO, response.getBody());
        verify(saleService, times(1)).editSale(saleDTO);
    }

    @Test
    void editSaleNotFoundTest() {
        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());

        Product product = new Product(1L, "TV", 700.0, 7L, new ArrayList<>());
        Product product1 = new Product(2L, "Impresora", 70.0, 5L, new ArrayList<>());

        List<SaleProduct> saleProducts = List.of(
                new SaleProduct(1, new Sale(), product, 7),
                new SaleProduct(2, new Sale(), product1, 8));


        List<ProductSaleDTO> productSaleDTOS = List.of(
                new ProductSaleDTO(1L, 5),
                new ProductSaleDTO(2L, 3));

        CreateSaleDTO saleDTO = new CreateSaleDTO(client.getIdClient(), productSaleDTOS);



        doThrow(new EntityNotFoundException("Sale not found")).when(saleService).editSale(saleDTO);


        ResponseEntity<?> response = saleController.editSale(saleDTO);


        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Sale not found", response.getBody());
        verify(saleService, times(1)).editSale(saleDTO);
    }

    @Test
    void editSaleInternalServerErrorTest() {
        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());

        Product product = new Product(1L, "TV", 700.0, 7L, new ArrayList<>());
        Product product1 = new Product(2L, "Impresora", 70.0, 5L, new ArrayList<>());

        List<SaleProduct> saleProducts = List.of(
                new SaleProduct(1, new Sale(), product, 7),
                new SaleProduct(2, new Sale(), product1, 8));


        List<ProductSaleDTO> productSaleDTOS = List.of(
                new ProductSaleDTO(1L, 5),
                new ProductSaleDTO(2L, 3));

        CreateSaleDTO saleDTO = new CreateSaleDTO(client.getIdClient(), productSaleDTOS);
        doThrow(new RuntimeException("Unexpected server error")).when(saleService).editSale(saleDTO);


        ResponseEntity<?> response = saleController.editSale(saleDTO);


        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(saleService, times(1)).editSale(saleDTO);
    }
    @Test
    public void findSaleControllerTest (){
        Long idTest = 1L ;
        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());
        Product product = new Product(1L, "TV", 700.0, 7L, new ArrayList<>());
        Product product1 = new Product(2L, "Impresora", 70.0, 5L, new ArrayList<>());

        List<SaleProduct> saleProducts = List.of(
                new SaleProduct(1, new Sale(), product, 7),
                new SaleProduct(2, new Sale(), product1, 8));
        Sale sale = new Sale(1L,LocalDate.now(), client, saleProducts,10000.0);

        when(saleService.findSale(idTest)).thenReturn(sale);

        ResponseEntity<?> saleResult = saleController.findSale(idTest);

        assertNotNull(saleResult);
        assertEquals(OK, saleResult.getStatusCode());
        assertEquals(sale, saleResult.getBody());
        verify(saleService, times(1)).findSale(idTest);
    }

    @Test
    public void findSaleControllerNotfoundTest (){
        Long idTest = 1L ;

        when(saleService.findSale(idTest)).thenThrow(new EntityNotFoundException("Sale not found"));

        ResponseEntity<?> saleResult = saleController.findSale(idTest);
        assertNotNull(saleResult);
        assertEquals(NOT_FOUND, saleResult.getStatusCode());
        assertEquals("Sale not found", saleResult.getBody());
        verify(saleService, times(1)).findSale(idTest);
    }

    @Test
    public void findSaleControllerInternalErrorTest (){
        Long idTest = 1L ;

        when(saleService.findSale(idTest)).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> saleResult = saleController.findSale(idTest);
        assertNotNull(saleResult);
        assertEquals(INTERNAL_SERVER_ERROR, saleResult.getStatusCode());
        assertEquals("Server internal Error", saleResult.getBody());
        verify(saleService, times(1)).findSale(idTest);
    }
}
