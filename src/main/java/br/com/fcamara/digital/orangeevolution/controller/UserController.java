package br.com.fcamara.digital.orangeevolution.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fcamara.digital.orangeevolution.data.model.enums.StatusProgressEnum;
import br.com.fcamara.digital.orangeevolution.data.vo.CategoryVO;
import br.com.fcamara.digital.orangeevolution.data.vo.ContentProgressVO;
import br.com.fcamara.digital.orangeevolution.data.vo.ContentVO;
import br.com.fcamara.digital.orangeevolution.data.vo.UserVO;
import br.com.fcamara.digital.orangeevolution.services.ContentProgressServices;
import br.com.fcamara.digital.orangeevolution.services.ContentServices;
import br.com.fcamara.digital.orangeevolution.services.TrailServices;
import br.com.fcamara.digital.orangeevolution.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Endpoint")
@RestController
@RequestMapping("api/user")
public class UserController {
	@Autowired
	private UserServices services;
	@Autowired
	private TrailServices trailServices;
	@Autowired
	private ContentProgressServices progressServices;
	@Autowired
	private ContentServices contentServices;

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

	@SuppressWarnings("rawtypes")
	@Operation(summary = "Add Trail to User")
	@PatchMapping(value = "/addtrail/{idTrail}")
	public ResponseEntity addTrailToUser(@PathVariable(value = "idTrail") Long idTrail,
			@AuthenticationPrincipal UserDetails userDetails) {
		var user = services.findUserVO(userDetails.getUsername());
		var trail = trailServices.findById(idTrail);
		boolean primaryAcess = false;
		if (user.getTrails().isEmpty()) {
			primaryAcess = true;
		}

		Map<Object, Object> model = new HashMap<>();

		if (!user.getTrails().contains(trail) || !primaryAcess) {
			user.getTrails().add(trail);
			var userVO = services.updateUserTrails(user);
			System.out.println("Adicionou Trilha");
			for (CategoryVO categoryVO : trail.getCategories()) {
				var contents = contentServices.findAllByCategory(categoryVO);
				System.out.println("Buscou os contents da categoria");
				for (ContentVO contentVO : contents) {
					System.out.println("Content: "+contentVO.getDescription());
					if (primaryAcess) {
						System.out.println("entrou no if de primeiro acesso");
						ContentProgressVO contentProgressVO = ContentProgressVO.builder()
								.status(StatusProgressEnum.NOT_COMPLETED).user(userVO.getKey())
								.content(contentVO.getKey()).build();
						progressServices.create(contentProgressVO);
					} else {
						System.out.println("Não entrou no if de primeiro acesso");
						var erro = progressServices.findByContentId(contentVO.getKey(), userVO.getKey());
						System.out.println(erro);
						if (erro==null) {
							System.out.println("Usuário nao tem esse progress");
							ContentProgressVO contentProgressVO = ContentProgressVO.builder()
									.status(StatusProgressEnum.NOT_COMPLETED).user(userVO.getKey())
									.content(contentVO.getKey()).build();
							progressServices.create(contentProgressVO);
						}
					}
				}

			}
			model.put("User trails:", userVO.getTrails());

		} else {
			String message = "The user is already registered on this trail";
			model.put("message", message);
			return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);

		}

		return ok(model);

	}

	@SuppressWarnings("rawtypes")
	@Operation(summary = "Remove User Trail by Trail ID")
	@PatchMapping(value = "/removetrail/{idTrail}")
	public ResponseEntity removeTrailToUser(@PathVariable(value = "idTrail") Long idTrail,
			@AuthenticationPrincipal UserDetails userDetails) {
		var user = services.findUserVO(userDetails.getUsername());
		var trail = trailServices.findById(idTrail);

		Map<Object, Object> model = new HashMap<>();
		UserVO userVO = new UserVO();

		if (user.getTrails().contains(trail)) {
			user.getTrails().remove(trail);
			userVO = services.updateUserTrails(user);
			model.put("User trails:", userVO.getTrails());

		} else {
			String message = "The user is not registered on this trail";
			model.put("message", message);
			return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);

		}

		return ok(model);

	}
}
