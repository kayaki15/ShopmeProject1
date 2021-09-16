package com.shopme.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import java.util.List;

@Controller
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listAll(Model model) {
		List<User> TheListUsers = service.listAll();
		model.addAttribute("listuser", TheListUsers);
		return "users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		
	//	injecting list rolles from user service to this model which is user
		List<Role> thelistRoles = service.listRoles() ;
		
		User user =new User() ;
		user.setEnabled(true);
        model.addAttribute("user", user) ;
        model.addAttribute("listRoles", thelistRoles) ;
	
	return "users_form" ;
		
	}


	
	@PostMapping("/users/save")
	public String saveUser(User user ,RedirectAttributes redirectAttributes) {
		System.out.println(user);

		//here for attaching save to UserService for registering the new user in database and display to our screen!!!
		service.save(user);  
		///
		
		redirectAttributes.addFlashAttribute("message", "The User has been saved successfully." ) ;

		return "redirect:/users" ;

	}
	

		
	}
