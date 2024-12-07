package com.company.bazar.serviceTest;

import com.company.bazar.dto.CreateSaleDTO;
import com.company.bazar.dto.MaxSaleDTO;
import com.company.bazar.dto.ProductSaleDTO;
import com.company.bazar.dto.SaleDTO;
import com.company.bazar.model.Client;
import com.company.bazar.model.Product;
import com.company.bazar.model.Sale;
import com.company.bazar.model.SaleProduct;
import com.company.bazar.repository.IClientRepository;
import com.company.bazar.repository.IProductRepository;
import com.company.bazar.repository.ISaleProductRepository;
import com.company.bazar.repository.ISaleRepository;
import com.company.bazar.service.SaleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SaleServiceTest {
    @Mock
    private ISaleRepository saleRepository;

    @Mock
    private ISaleProductRepository saleProductRepository;

    @Mock
    private IClientRepository clientRepository;
    @Mock
    private IProductRepository productRepository;
    @InjectMocks //Permite inyectar los mock al service
    private  SaleService saleService;

    @BeforeEach
    public void setUp (){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void notFoundSaleTest (){

        when(saleRepository.findBySaleDate(any())).thenReturn(Collections.emptyList());
        when(saleProductRepository.findBySale_CodSaleIn((any()))).thenReturn(Collections.emptyList());

        SaleDTO result =saleService.totalSale(LocalDate.now());

        assertEquals(0, result.getSumTotalSale());
        assertEquals(0, result.getSalesQuantity());
    }
    @Test
    public void returnTwoSales(){

        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());
        Product product = new Product(1L, "TV", 700.0, 7L, new ArrayList<>());
        Product product1 = new Product(2L, "Impresora", 70.0, 5L, new ArrayList<>());

        List<SaleProduct> saleProducts = List.of(
         new SaleProduct(1, null, product, 7),
        new SaleProduct(2, null, product1, 8));


        List<Sale> sales = List.of(
                new Sale(1L, LocalDate.now(),  client, saleProducts, 1000.0 ),
                new Sale(2L, LocalDate.now(), client, saleProducts, 2000.0));

        when(saleRepository.findBySaleDate(any())).thenReturn(sales);
        when(saleProductRepository.findBySale_CodSaleIn(any())).thenReturn(saleProducts);

        SaleDTO result = saleService.totalSale(LocalDate.now());

        assertEquals(3000.0, result.getSumTotalSale() );
        assertEquals(2, result.getSalesQuantity() );
    }

    @Test
    public void emptySaleTest(){
        when(saleRepository.findAll()).thenReturn(Collections.emptyList());

        List<Sale> sale = saleService.getSales();

        assertEquals(0,  sale.size());
    }
    @Test
    public void getSaleTest (){

        Client client = new Client(1L, "Aldair", "Martinez", "1234576", null);


        List<Sale>sales = List.of(
                new Sale(1L, LocalDate.now(),  client, new ArrayList<>(), 1000.0  ),
                new Sale(2L, LocalDate.now(), client, new ArrayList<>(), 2000.0));

        when(saleRepository.findAll()).thenReturn(sales);

        List<Sale> result = saleService.getSales();

        assertEquals(2, result.size());

    }
    @Test
    public void findSaleErrorTest (){

        when(saleRepository.findById(any())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> this.saleService.findSale(2L));
    }
    @Test
    public void findSaleTest(){
        Sale sale = new Sale(1L, LocalDate.now(),  new Client(), new ArrayList<>(), 1000.0  );

        when(saleRepository.findById(any())).thenReturn(Optional.of(sale));

        Sale result = saleService.findSale(1L);

        assertEquals(1, result.getCodSale());
        assertTrue(result.getTotalSale() ==1000.0);
    }

    @Test
    public void createSaleErrorTest (){
        CreateSaleDTO sale = new CreateSaleDTO();

        when (clientRepository.findById(any())).thenReturn(null);

        assertThrows(RuntimeException.class, ()->this.saleService.createSale(sale));
        verify(saleProductRepository, never()).save(any());
        verify(saleRepository, never()).save(any());
    }
    @Test
    public void crateSaleTest (){

        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());
        when (clientRepository.findById(any())).thenReturn(Optional.of(client));
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

        when (saleRepository.save(any())).thenReturn(newSale);
        when (productRepository.findById(1L)).thenReturn(Optional.of(product));
        when (productRepository.findById(2L)).thenReturn(Optional.of(product1));
        when (saleProductRepository.save(any())).thenReturn(null);
        when (productRepository.save(any())).thenReturn(null);

        saleService.createSale(saleDTO);

        verify(saleRepository, times(2)).save(any());
        verify(saleProductRepository, times(2)).save(any());
        verify(productRepository, times(2)).save(any());
    }

    @Test
    public void invaliableStockTest (){
        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());
        when (clientRepository.findById(any())).thenReturn(Optional.of(client));
        Product product = new Product(1L, "TV", 700.0, 2L, new ArrayList<>());
        Product product1 = new Product(2L, "Impresora", 70.0, 2L, new ArrayList<>());




        List<ProductSaleDTO> productSaleDTOS = List.of(
                new ProductSaleDTO(1L, 17),
                new ProductSaleDTO(2L, 17));

        CreateSaleDTO saleDTO = new CreateSaleDTO(client.getIdClient(), productSaleDTOS);

        // Configurar mocks
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product1));

        assertThrows( RuntimeException.class, () -> this.saleService.createSale(saleDTO));

        verify(saleRepository, times(1)).save(any());
        verify(saleProductRepository, never()).save(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    public void deleteErrorTest () {

        when(saleRepository.existsById(any())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> this.saleService.deleteSale(1L));
    }

    @Test
    public  void deleteSusccessTest (){
        Long codSale =1L;

        when(saleRepository.existsById(any())).thenReturn(true);

        saleService.deleteSale(codSale);
        verify(saleRepository, times(1)).existsById(codSale);
        verify(saleRepository, times(1)).deleteById(codSale);

    }

    @Test
    public void findProductSaleTest (){
        Product product = new Product(1L, "TV", 700.0, 7L, new ArrayList<>());
        Product product1 = new Product(2L, "Impresora", 70.0, 5L, new ArrayList<>());

        List<SaleProduct> saleProducts = List.of(
                new SaleProduct(1, new Sale(), product, 7),
                new SaleProduct(2, new Sale(), product1, 8));

        Sale sale = new Sale(1L, LocalDate.now(), new Client(), saleProducts, 10000.0);

        when(saleRepository.findById(any())).thenReturn(Optional.of(sale));
        when(saleRepository.existsById(any())).thenReturn(true);

        List<String> productNames = saleService.findProductSale(sale.getCodSale());

        assertEquals(2, productNames.size());
        assertTrue(productNames.contains("TV"));
        assertFalse(productNames.contains("Celular"));

    }

     @Test
    public void  findProductErrorTest (){
        when(saleRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows( RuntimeException.class, ()-> saleService.findProductSale(1L));
     }
    @Test
    public void saleMajorNotFoundTest() {

        when(saleRepository.findSaleWithMaxTotal()).thenReturn(null);


        assertThrows(RuntimeException.class, () -> {
            saleService.saleMajor();
        });
    }

    @Test
    public void SaleMajorTest(){
        Client client = new Client(1L, "Aldair", "Martinez", "123456", new ArrayList<>());
        Sale sale = new Sale(1L, LocalDate.now(), client, new ArrayList<>(), 5000.0);
        when(saleRepository.findSaleWithMaxTotal()).thenReturn(sale);

        MaxSaleDTO result = saleService.saleMajor();
        assertEquals(1L, result.getCodSale());
        assertEquals(5000.0, result.getTotal());
        assertEquals("Aldair", result.getClientName());
        assertEquals("Martinez", result.getClientLastname());
        assertTrue(result.getTotal() > 3000);
    }

    @Test
    public  void editSaleTest (){
        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());
        when (clientRepository.findById(any())).thenReturn(Optional.of(client));
        Product product = new Product(1L, "TV", 700.0, 7L, new ArrayList<>());
        Product product1 = new Product(2L, "Impresora", 70.0, 5L, new ArrayList<>());

        List<SaleProduct> saleProducts = List.of(
                new SaleProduct(1, new Sale(), product, 1),
                new SaleProduct(2, new Sale(), product1, 1));


        List<ProductSaleDTO> productSaleDTOS = List.of(
                new ProductSaleDTO(1L, 1),
                new ProductSaleDTO(2L, 1));

        CreateSaleDTO saleDTO = new CreateSaleDTO(client.getIdClient(), productSaleDTOS);
        Sale newSale = new Sale(1L,LocalDate.now(), client, saleProducts,10000.0);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(saleRepository.save(any(Sale.class))).thenReturn(newSale);

        saleService.editSale(saleDTO);

        verify(saleRepository, times(2)).save(any());

    }
    @Test
    public void editSaleErrorTest (){
        when(saleRepository.findById(any())).thenReturn(Optional.empty());
        CreateSaleDTO createSaleDTO = new CreateSaleDTO();

        assertThrows(RuntimeException.class, () -> {
            saleService.editSale(createSaleDTO);
        });
        verify(saleRepository,never()).save(any());
    }
}
