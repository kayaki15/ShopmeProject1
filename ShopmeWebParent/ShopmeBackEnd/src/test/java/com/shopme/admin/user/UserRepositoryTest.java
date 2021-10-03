package com.shopme.admin.user;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;


import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	private UserRepository repo;
	private TestEntityManager entityManager;
	
	@Autowired
	public UserRepositoryTest(UserRepository repo, TestEntityManager entityManager) {
		this.repo = repo;
		this.entityManager = entityManager;
	}
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 2);
		User userWithOneRole = new User("salima@gmail.com", "hah", "miral", "kaka");
		userWithOneRole.addRole(roleAdmin);
		
		User savedUser = repo.save(userWithOneRole);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userWithTwoRole = new User("r@g.com", "rg2020", "Remzi", "Güloğlu");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userWithTwoRole.addRole(roleEditor);
		userWithTwoRole.addRole(roleAssistant);
		
		User savedUser = repo.save(userWithTwoRole);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userById = repo.findById(1).get();
		System.out.println(userById);
		assertThat(userById).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userUpdateUserDetails = repo.findById(1).get();
		userUpdateUserDetails.setEnabled(true);
		userUpdateUserDetails.setEmail("Norcompany@com");
		
		repo.save(userUpdateUserDetails);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userUpdateUserRoles = repo.findById(1).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userUpdateUserRoles.getRoles().remove(roleEditor);
		userUpdateUserRoles.addRole(roleSalesperson);
		
		repo.save(userUpdateUserRoles);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userDeleteUser = 2;	
		repo.deleteById(userDeleteUser);
		
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "Norcompany@com";
		User userByEmail = repo.getUserByEmail(email);
		assertThat(userByEmail).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
//	
	@Test
	public void testEnableUser() {
		Integer id = 24;
		repo.updateEnabledStatus(id, true);
		
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 24;
		repo.updateEnabledStatus(id, false);
		
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 1;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isEqualTo(pageSize);
	}

	@Test
	public void testSearchUsers() {
		String keyword = "Bruce";

		int pageNumber = 0;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(user -> System.out.println(user));	

		assertThat(listUsers.size()).isGreaterThan(0);
	}
}