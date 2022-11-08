package br.com.fcamara.digital.orangeevolution.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import br.com.fcamara.digital.orangeevolution.data.vo.ContentProgressVO;
import br.com.fcamara.digital.orangeevolution.services.ContentProgressServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Content Progress Endpoint")
@RestController
@RequestMapping("/api/progress")
public class ContentProgressController {

	@Autowired
	private ContentProgressServices services;

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Operation(summary = "Find all Progress")
	@GetMapping
	public ResponseEntity<PagedModel<ContentProgressVO>> findAll(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			PagedResourcesAssembler assembler) {

		var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "status"));
		Page<ContentProgressVO> progressVO = services.findAll(pageable);
		progressVO.stream().forEach(
				s -> s.add(linkTo(methodOn(ContentProgressController.class).findById(s.getKey())).withSelfRel()));
		return new ResponseEntity<>(assembler.toModel(progressVO), HttpStatus.OK);
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
