package com.poly.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Products")
public class Product implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@NotBlank
	String id;
	
	@ManyToOne
	@JoinColumn(name = "CategoryID")
	Category category;
	
	@NotBlank
	String name;
	
	@NotNull
	@PositiveOrZero
	Double price;
	
	@NotNull
	@PositiveOrZero
	Integer quantity;
	
	String description;
	String image;
	
	@Column(name = "[LIKE]")
	Integer like=0;
	
	@NotBlank
	String author;
	
	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<Cart> carts;
	
	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<Favorite> favorites;
	
	@JsonIgnore
	@OneToMany(mappedBy = "product")
	List<OrderDetail> orderDetails;
}
