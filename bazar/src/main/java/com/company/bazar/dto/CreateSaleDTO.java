package com.company.bazar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateSaleDTO {


    private long idClient;
    private List<ProductSaleDTO> productList;

}
