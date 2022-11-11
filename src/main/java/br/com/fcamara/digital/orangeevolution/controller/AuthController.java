package br.com.fcamara.digital.orangeevolution.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fcamara.digital.orangeevolution.data.vo.UserVO;
import br.com.fcamara.digital.orangeevolution.exception.InvalidInputException;
import br.com.fcamara.digital.orangeevolution.security.AccountCredentialsVO;
import br.com.fcamara.digital.orangeevolution.security.jwt.JwtTokenProvider;
import br.com.fcamara.digital.orangeevolution.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	private UserServices userServices;

	@SuppressWarnings("rawtypes")
	@Operation(summary = "Authentication a user and return a token")
	@PostMapping(value = "/signin")
	public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {
		try {
			var username = data.getUsername();
			var password = data.getPassword();

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			var user = userServices.findUser(username);
			var token = "";
			if (user != null) {
				token = tokenProvider.createToken(username, user.getRoles(), user.getId(),user.getFullName());
			} else {
				throw new UsernameNotFoundException("UserName" + username + "not found!");
			}
			Map<Object, Object> model = new HashMap<>();
			model.put("token", token);
			return ok(model);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalidad username/password supplied!");
		}

	}

	@SuppressWarnings("rawtypes")
	@Operation(summary = "Create new user")
	@PostMapping(value = "/signup")
	public ResponseEntity create(@RequestBody UserVO user) {
		try {
			UserVO userVO = userServices.create(user);
			userVO.add(linkTo(methodOn(UserController.class).findById(userVO.getKey())).withSelfRel());
			Map<Object, Object> model = new HashMap<>();
			model.put("user", userVO);
			return ok(model);

		} catch (Exception e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

}
