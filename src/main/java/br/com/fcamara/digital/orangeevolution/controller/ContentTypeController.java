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

import br.com.fcamara.digital.orangeevolution.data.vo.ContentTypeVO;
import br.com.fcamara.digital.orangeevolution.services.ContentTypeServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ContentType Endpoint")
@RestController
@RequestMapping("/api/content-type")
public class ContentTypeController {
	@Autowired
	private ContentTypeServices services;

	@Operation(summary = "Create new content-type")
	@PostMapping
	public ContentTypeVO create(@RequestBody ContentTypeVO type) {
		ContentTypeVO typeVO = services.create(type);
		typeVO.add(linkTo(methodOn(ContentTypeController.class).findById(typeVO.getKey())).withSelfRel());
		return typeVO;
	}

	@Operation(summary = "Find a specific by you ID")
	@GetMapping(value = "/{id}")
	public ContentTypeVO findById(@PathVariable(value = "id") Long id) {
		ContentTypeVO typeVO = services.findById(id);
		typeVO.add(linkTo(methodOn(ContentTypeController.class).findById(typeVO.getKey())).withSelfRel());
		return typeVO;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Operation(summary = "Find all types of content")
	@GetMapping
	public ResponseEntity<PagedModel<ContentTypeVO>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			PagedResourcesAssembler assembler) {

		var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));
		Page<ContentTypeVO> typesVO = services.findAll(pageable);
		typesVO.stream()
				.forEach(s -> s.add(linkTo(methodOn(ContentTypeController.class).findById(s.getKey())).withSelfRel()));
		return new ResponseEntity<>(assembler.toModel(typesVO), HttpStatus.OK);
	}

	@Operation(summary = "Update specific content-type by your ID")
	@PutMapping(value = "/{id}")
	public ContentTypeVO update(@RequestBody ContentTypeVO type) {
		ContentTypeVO typeVO = services.update(type);
		typeVO.add(linkTo(methodOn(ContentTypeController.class).findById(typeVO.getKey())).withSelfRel());
		return typeVO;
	}

	@Operation(summary = "Delete specific Content-type by your ID")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		services.delete(id);
		return ResponseEntity.ok().build();
	}
}
