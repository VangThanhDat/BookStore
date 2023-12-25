package com.poly.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

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
@Table(name = "Users")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotBlank(message = "Không được để trống")
	String id;
	String password;
	@NotBlank(message = "Không được để trống")
	String fullname;
	@NotBlank(message = "Không được để trống")
	@Email(message = "Không đúng định dạng email")
	String email;
	@NotNull(message = "Vui lòng chọn giới tính")
	Boolean gender;
	@DateTimeFormat(iso = ISO.DATE)
	@Temporal(TemporalType.DATE)
	@Column(name = "Birthday")
	@NotNull(message = "Vui lòng chọn ngày sinh")
	Date birthday;
	@NotBlank(message = "Không được để trống")
	String phone;
	@NotBlank(message = "Không được để trống")
	String address;
	@NotNull(message = "Vui lòng chọn trạng thái cho user")
	Boolean active;

	@JsonIgnore
	@Transient
	String confirmPassword;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Cart> carts;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Favorite> favorites;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Order> orders;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Authority> authorities;

	public String getRoleUser(List<Authority> authorities) {
		String role = null;
		List<String> roles = new ArrayList<>();
		if (!authorities.isEmpty()) {
			for (Authority authority : authorities) {
				roles.add(authority.getRole().getId());
			}
		}
		if (roles != null) {
			if (roles.contains("ADMIN_API")) {
				role = "ADMIN_API";
			} else if (roles.contains("ADMIN")) {
				role = "ADMIN";
			} else if (roles.contains("USER")) {
				role = "USER";
			}
		}
		return role;
	}
}
