package com.poly.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OrderDetail")
public class OrderDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	String id;
	
	@ManyToOne
	@JoinColumn(name = "OrderID")
	Order order;
	
	@ManyToOne
	@JoinColumn(name = "ProductID")
	Product product;
	
	@Column(name = "Quantity_purchased")
	Integer quantityPurchased;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DateCreate")
	Date date = new Date();
}
