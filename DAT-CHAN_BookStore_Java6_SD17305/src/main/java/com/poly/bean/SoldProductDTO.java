package com.poly.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class SoldProductDTO {
	@Id
	private String productId;
    private String productName;
    private double productPrice;
    private Integer sold;
    private long soldQuantity;
    private String productCategory;
}
