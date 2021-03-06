package br.com.davimonteiro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/users")
public class UserController extends RestControllerTemplete<User, Long> {

	@Autowired
	public UserController(UserRepository repository) {
		super(repository);
		repository.save(User.builder().id(1L).name("Davi").password("1234").build());
	}

}
