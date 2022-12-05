package com.sena.mindsoul;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.sena.mindsoul.entity.Role;
import com.sena.mindsoul.entity.User;

import com.sena.mindsoul.repository.RoleRepository;
import com.sena.mindsoul.repository.UserRepository;

//Esta clase arranca la aplicación
@SpringBootApplication
public class MindsoulApplication {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private  RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(MindsoulApplication.class, args);
	}

	//Event listener quiere decir que apenas la aplicacion haya cargado se van a ejecutar los metodos
	@EventListener(ApplicationReadyEvent.class)
	public void saveAdmin(){
		//buscamos el ROLE_ADMIN si no existe lo creamos
		Role role = roleRepository.findByName("ROLE_ADMIN");
		if(role == null){
			role = createRoleAdmin();
		}

		// construimos un usuario si se quiere pueden cambiar
		User admin = new User(
				null,
				"admin",
				"admin@localhost.com",
				// la contraseña es 123456789, debe estar encriptada
				"$2a$10$iSwJ2skACecSsWRY.YQ78ezGhEdhtrvHTNU9XXUFWxaCQYKlbze0m",
				Arrays.asList(role));

		userRepository.save(admin); 


	}

	//creamos el role si no existe se hace por defecto no borrarlo porque no se puede crear desde la app
	public Role createRoleAdmin(){
		Role admin = new Role();
		admin.setName("ROLE_ADMIN");
		return roleRepository.save(admin);
	}

}
