package br.com.fcamara.digital.orangeevolution.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fcamara.digital.orangeevolution.data.vo.UserVO;
import br.com.fcamara.digital.orangeevolution.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Endpoint")
@RestController
@RequestMapping("api/user")
public class UserController {
	@Autowired
	private UserServices services;

	@Operation(summary = "Create new user")
	@PostMapping
	public UserVO create(@RequestBody UserVO user) {
		UserVO userVO = services.create(user);
		userVO.add(linkTo(methodOn(UserController.class).findById(userVO.getKey())).withSelfRel());
		return userVO;

	}

	@Operation(summary = "Find a specific by you ID")
	@GetMapping(value = "/{id}")
	public UserVO findById(@PathVariable(value = "id") Long id) {
		UserVO userVO = services.findById(id);
		userVO.add(linkTo(methodOn(UserController.class).findById(userVO.getKey())).withSelfRel());
		return userVO;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Operation(summary = "Find all users")
	@GetMapping
	public ResponseEntity<PagedModel<UserVO>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			PagedResourcesAssembler assembler) {

		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "fullName"));
		Page<UserVO> users = services.findAll(pageable);
		users.stream().forEach(s -> s.add(linkTo(methodOn(UserController.class).findById(s.getKey())).withSelfRel()));
		return new ResponseEntity<>(assembler.toModel(users), HttpStatus.OK);
	}

	@Operation(summary = "Update specific user by your ID")
	@PutMapping
	public UserVO update(@RequestBody UserVO user) {
		UserVO userVO = services.update(user);
		userVO.add(linkTo(methodOn(UserController.class).findById(userVO.getKey())).withSelfRel());
		return userVO;
	}

	@Operation(summary = "Delete specific user by your ID")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		services.delete(id);
		return ResponseEntity.ok().build();
	}

}
