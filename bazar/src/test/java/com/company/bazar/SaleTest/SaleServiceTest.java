package com.company.bazar.SaleTest;

import com.company.bazar.dto.CreateSaleDTO;
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
    public void init (){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void notFoundSaleTest (){

        when(saleRepository.findBySaleDate(any())).thenReturn(Collections.emptyList());
        when(saleProductRepository.findBySale_CodSaleIn((any()))).thenReturn(Collections.emptyList());
        SaleDTO result =saleService.totalSale(LocalDate.now());
        assertEquals(0, result.getSumTotalSale());
    }
    @Test
    public void returnTwoSales(){

        Client client = new Client(1L, "Aldair", "Martinez", "1234576", null);
        Product product = new Product(1L, "TV", 700.0, 7L, null);
        Product product1 = new Product(2L, "Impresora", 70.0, 5L, null);
        List<SaleProduct> saleProducts = new ArrayList<>();
        SaleProduct saleProduct = new SaleProduct(1, null, product, 7);
        SaleProduct saleProduct1 = new SaleProduct(2, null, product1, 8);
        saleProducts.add(saleProduct);
        saleProducts.add(saleProduct1);
        Sale sale = new Sale(1L, LocalDate.now(),  client, saleProducts, 1000.0  );
        Sale sale1 = new Sale(2L, LocalDate.now(), null, saleProducts, 2000.0);
        List<Sale> sales = new ArrayList<>();
        sales.add(sale);
        sales.add(sale1);
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
        Product product = new Product(1L, "TV", 700.0, 7L, null);
        Product product1 = new Product(2L, "Impresora", 70.0, 5L, null);
        Sale sale = new Sale(1L, LocalDate.now(),  client, null, 1000.0  );
        Sale sale1 = new Sale(2L, LocalDate.now(), client, null, 2000.0);
        List<Sale>sales = new ArrayList<>();
        sales.add(sale);
        sales.add(sale1);
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
        Sale sale = new Sale(1L, LocalDate.now(),  null, null, 1000.0  );
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
    }
    @Test
    public void crateSaleTest (){

        Client client = new Client(1L, "Aldair", "Martinez", "1234576", null);
        when (clientRepository.findById(any())).thenReturn(Optional.of(client));
        Product product = new Product(1L, "TV", 700.0, 7L, null);
        Product product1 = new Product(2L, "Impresora", 70.0, 5L, null);
        List<SaleProduct> saleProducts = new ArrayList<>();
        SaleProduct saleProduct = new SaleProduct(1, null, product, 7);
        SaleProduct saleProduct1 = new SaleProduct(2, null, product1, 8);
        saleProducts.add(saleProduct);
        saleProducts.add(saleProduct1);
        Sale newSale = new Sale(1L,LocalDate.now(), client, saleProducts,10000.0);
        List<ProductSaleDTO> productSaleDTOS = new ArrayList<>();
        ProductSaleDTO productSaleDTO = new ProductSaleDTO(1L, 5);
        ProductSaleDTO productSaleDTO1 = new ProductSaleDTO(2L, 3);
        productSaleDTOS.add(productSaleDTO);
        productSaleDTOS.add(productSaleDTO1);
        CreateSaleDTO saleDTO = new CreateSaleDTO(client.getIdClient(), productSaleDTOS);
        when (saleRepository.save(any())).thenReturn(newSale);
        when (productRepository.findById(1L)).thenReturn(Optional.of(product));
        when (productRepository.findById(2L)).thenReturn(Optional.of(product1));
        when (saleProductRepository.save(any())).thenReturn(null);
        when (productRepository.save(any())).thenReturn(null);

        saleService.createSale(saleDTO);

        verify(saleRepository, times(2)).save(any());

    }
}
