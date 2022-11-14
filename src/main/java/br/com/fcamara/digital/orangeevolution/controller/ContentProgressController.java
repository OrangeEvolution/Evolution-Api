package br.com.fcamara.digital.orangeevolution.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fcamara.digital.orangeevolution.data.vo.ContentProgressVO;
import br.com.fcamara.digital.orangeevolution.services.ContentProgressServices;
import br.com.fcamara.digital.orangeevolution.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Content Progress Endpoint")
@RestController
@RequestMapping("/api/progress")
public class ContentProgressController {

	@Autowired
	private ContentProgressServices services;
	@Autowired
	private UserServices userServices;

	@Operation(summary = "Create new progress")
	@PostMapping
	public ContentProgressVO create(@RequestBody ContentProgressVO contentProgress) {
		ContentProgressVO contentProgressVO = services.create(contentProgress);
		contentProgressVO.add(
				linkTo(methodOn(ContentProgressController.class).findById(contentProgressVO.getKey())).withSelfRel());
		return contentProgressVO;
	}

	@Operation(summary = "Find a specific by you ID")
	@GetMapping(value = "/{id}")
	public ContentProgressVO findById(@PathVariable(value = "id") Long id) {
		ContentProgressVO progressVO = services.findById(id);
		progressVO.add(linkTo(methodOn(ContentProgressController.class).findById(progressVO.getKey())).withSelfRel());
		return progressVO;
	}

	@Operation(summary = "Find a specific Progress by content ID")
	@GetMapping(value = "/content/{id}")
	public ContentProgressVO findByContentId(@PathVariable(value = "id") Long id,
			@AuthenticationPrincipal UserDetails userDetails) {
		ContentProgressVO progressVO = services.findByContentId(id,
				userServices.findUser(userDetails.getUsername()).getId());
		progressVO.add(linkTo(methodOn(ContentProgressController.class).findById(progressVO.getKey())).withSelfRel());
		return progressVO;
	}

	@Operation(summary = "Find all Progress User")
	@GetMapping
	public List<ContentProgressVO> findAll(@AuthenticationPrincipal UserDetails userDetails) {

		return services.findAll(userServices.findUser(userDetails.getUsername()).getId());
	}

	@Operation(summary = "Update specific content progress by your ID")
	@PutMapping(value = "/{id}")
	public ContentProgressVO update(@RequestBody ContentProgressVO type) {
		ContentProgressVO progressVO = services.update(type);
		progressVO.add(linkTo(methodOn(ContentProgressController.class).findById(progressVO.getKey())).withSelfRel());
		return progressVO;
	}

	@Operation(summary = "Delete specific content progress by your ID")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		services.delete(id);
		return ResponseEntity.ok().build();
	}

}
