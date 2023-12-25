package com.poly.controller.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poly.bean.Category;
import com.poly.bean.Product;
import com.poly.service.CategorieService;
import com.poly.service.ProductService;
import com.poly.service.SessionService;
import com.poly.util.FileUploadUntil;

@Controller
public class ProductController {
	@Autowired
	ProductService pdao;

	@Autowired
	CategorieService cdao;

	@Autowired
	SessionService session;

	@GetMapping("/admin/manage/products")
	public String products(Model model, @RequestParam("p") Optional<Integer> p) {
		Pageable pageable;
		try {
			pageable = PageRequest.of(p.orElse(0), 5);
		} catch (Exception e) {
			pageable = PageRequest.of(0, 5);
		}
		Page<Product> list = pdao.findAll(pageable);
		List<Category> categories = cdao.findAll();
		model.addAttribute("categories", categories);
		Product product = new Product();
		model.addAttribute("list", list);
		model.addAttribute("product", product);
		return "admin/manage/products";
	}

	@GetMapping("/admin/manage/products/{id}")
	public String getForm(Model model, @ModelAttribute("product") Product entity, @PathVariable("id") String id,
			@RequestParam("p") Optional<Integer> p, Pageable pageable) {
		try {
			pageable = PageRequest.of(p.orElse(0), 5);
		} catch (Exception e) {
			pageable = PageRequest.of(0, 5);
		}
//		List<Category> categories = cdao.findAll();
//		model.addAttribute("categories", categories);
		entity = pdao.findById2(id);
		model.addAttribute("product", entity);
		Page<Product> list = pdao.findAll(pageable);
		model.addAttribute("list", list);
		return "admin/manage/products";
	}

	@PostMapping("/admin/manage/products/update/{id}")
	public String update(Model model, @RequestParam("p") Optional<Integer> p,
			@Validated @ModelAttribute("product") Product entity, Errors errors, @PathVariable("id") String id,
			@RequestParam("photo") MultipartFile multipartFile) throws IOException {
		Pageable pageable;
		try {
			pageable = PageRequest.of(p.orElse(0), 5);
		} catch (Exception e) {
			pageable = PageRequest.of(0, 5);
		}
		Page<Product> list = pdao.findAll(pageable);

		model.addAttribute("list", list);
		if (errors.hasErrors()) {
			return "admin/manage/products";
		} else {
			System.out.println(multipartFile.getOriginalFilename());
			entity.setImage(multipartFile.getOriginalFilename());
			Product saveImg = pdao.create(entity);
			if (saveImg != null) {
				try {
					File saveFile = new ClassPathResource("\\static\\image").getFile();
					Path path = Paths
							.get(saveFile.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
					Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(path);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			pdao.update(entity);
			return "redirect:/admin/manage/products/" + id;
		}

		
	}

	@PostMapping("/admin/manage/products/delete/{id}")
	public String delete(Model model, @ModelAttribute("product") Product entity, @PathVariable("id") String id) {
		pdao.findById(id);
		entity.setQuantity(0);
		pdao.update(entity);
		return "redirect:/admin/manage/products";
	}

	@PostMapping("/admin/manage/products/save")
	public String saveNew(Model model, @RequestParam("p") Optional<Integer> p,
			@RequestParam("photo") MultipartFile multipartFile, @Validated @ModelAttribute("product") Product entity,
			Errors errors) throws IOException {
		Pageable pageable;
		try {
			pageable = PageRequest.of(p.orElse(0), 5);
		} catch (Exception e) {
			pageable = PageRequest.of(0, 5);
		}
		Page<Product> list = pdao.findAll(pageable);
		model.addAttribute("list", list);
		Boolean check = pdao.checkID(entity.getId());

		if (errors.hasErrors()) {
			return "admin/manage/products";
		} else if (!errors.hasErrors()) {
			if (check == true) {
				model.addAttribute("errors", "Sản phẩm đã tồn tại trong kho");
				return "admin/manage/products";
			} else {
				System.out.println(multipartFile.getOriginalFilename());
				entity.setImage(multipartFile.getOriginalFilename());
				Product saveImg = pdao.create(entity);
				if (saveImg != null) {
					try {
						File saveFile = new ClassPathResource("\\static\\image").getFile();
						Path path = Paths
								.get(saveFile.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
						Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
						System.out.println(path);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			pdao.create(entity);
		}
		return "redirect:/admin/manage/products";

	}

	@PostMapping("/admin/manage/products/search")
	public String seachProduct(Model model, @RequestParam("keyword") Optional<String> name,
			@RequestParam("p") Optional<Integer> p, @ModelAttribute("product") Product entity) {
		String find;
		if (session.getAttribute("keyword") == null) {
			find = name.orElse("");
		} else {
			find = name.orElse(session.getAttribute("keyword"));
		}
		session.setAttribute("keyword", find);
		Pageable pageable = PageRequest.of(p.orElse(0), 5);
		Page<Product> list = pdao.findByNamePage("%" + find + "%", pageable);
		model.addAttribute("list", list);
		return "admin/manage/products";
	}

}
