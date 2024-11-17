package com.company.bazar.repository;

import com.company.bazar.model.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISaleProductRepository extends JpaRepository<SaleProduct, Long> {
    public List<SaleProduct> findBySale_CodSaleIn(List<Long> saleIds);
}
