package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.bean.Category;
import com.poly.bean.Favorite;
import com.poly.bean.Product;
import com.poly.bean.User;
import com.poly.repository.FavoriteRepository;

@Service
public class FavoriteService {
	@Autowired
	private FavoriteRepository faRepo;

//    @Autowired
//    private UserRepository uRepo;

//	public boolean isPostLiked(Long postId, String userId) {
//		Favorite post = faRepo.findById(postId).orElse(null);
//		if (post == null) {
//			return false;
//		}
//		User user = uRepo.findById(userId).orElse(null);
//		return user != null && post.getLikedByUsers().contains(user);
//	}

//	public void likePost(Long postId, Long userId) {
//		Favorite post = faRepo.findById(postId).orElse(null);
//		User user = uRepo.findById(userId).orElse(null);
//		if (post != null && user != null) {
//			post.getLikedByUsers().add(user);
//			faRepo.save(post);
//		}
//	}

//	public void unlikePost(Long postId, Long userId) {
//		Favorite post = faRepo.findById(postId).orElse(null);
//		User user = uRepo.findById(userId).orElse(null);
//		if (post != null && user != null) {
//			post.getLikedByUsers().remove(user);
//			faRepo.save(post);
//		}
//	}

	public List<Favorite> findByUsername(String username) {
		return faRepo.findByUserId(username);
	}
	public Favorite likeProduct(Favorite entity) {
		return faRepo.save(entity);
	}

	public List<Favorite> findByProductname(String pname,String uname) {
		return faRepo.findByProduct(pname,uname);
	}
	public List<Favorite> findByCategory(String c,String uname) {
		return faRepo.findByCategory(c,uname);
	}
	
	public Optional<Favorite> findProductLike(User username,Product productName){
		return faRepo.findByUserAndProduct(username,productName);
	}
	
	public Favorite save(Favorite favorite) {
		return faRepo.save(favorite);
	}
	
	public Favorite update(Favorite favorite) {
		return faRepo.saveAndFlush(favorite);
	}
}
