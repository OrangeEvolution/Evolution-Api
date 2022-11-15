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

import br.com.fcamara.digital.orangeevolution.data.model.enums.StatusProgressEnum;
import br.com.fcamara.digital.orangeevolution.data.vo.ContentProgressVO;
import br.com.fcamara.digital.orangeevolution.data.vo.ContentVO;
import br.com.fcamara.digital.orangeevolution.services.ContentProgressServices;
import br.com.fcamara.digital.orangeevolution.services.ContentServices;
import br.com.fcamara.digital.orangeevolution.services.TrailServices;
import br.com.fcamara.digital.orangeevolution.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Content Endpoint")
@RestController
@RequestMapping("/api/content")
public class ContentController {
	@Autowired
	private ContentServices services;
	@Autowired
	private ContentProgressServices progressServices;
	@Autowired
	private UserServices userServices;
	@Autowired
	private TrailServices trailServices;

	@Operation(summary = "Create new content")
	@PostMapping
	public ContentVO create(@RequestBody ContentVO content) {
		ContentVO contentVO = services.create(content);
		var trails = trailServices.findTrailsByCategory(content.getCategory());
		if (contentVO != null) {
			for (var trail : trails) {
				var users = userServices.findUserByTrailId(trail.getKey());
				for (var user : users) {
					ContentProgressVO progress = ContentProgressVO.builder().content(contentVO.getKey())
							.status(StatusProgressEnum.NOT_COMPLETED).user(user.getKey()).build();
					progressServices.create(progress);
				}
			}
		}

		contentVO.add(linkTo(methodOn(ContentController.class).findById(contentVO.getKey())).withSelfRel());
		return contentVO;
	}

	@Operation(summary = "Find a specific by you ID")
	@GetMapping(value = "/{id}")
	public ContentVO findById(@PathVariable(value = "id") Long id) {
		ContentVO contentVO = services.findById(id);
		contentVO.add(linkTo(methodOn(ContentController.class).findById(contentVO.getKey())).withSelfRel());
		return contentVO;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Operation(summary = "Find all content")
	@GetMapping
	public ResponseEntity<PagedModel<ContentVO>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			PagedResourcesAssembler assembler) {

		var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "description"));
		Page<ContentVO> contentVO = services.findAll(pageable);
		contentVO.stream()
				.forEach(s -> s.add(linkTo(methodOn(ContentController.class).findById(s.getKey())).withSelfRel()));
		return new ResponseEntity<>(assembler.toModel(contentVO), HttpStatus.OK);
	}

	@Operation(summary = "Update specific content by your ID")
	@PutMapping(value = "/{id}")
	public ContentVO update(@RequestBody ContentVO content) {
		ContentVO contentVO = services.update(content);
		contentVO.add(linkTo(methodOn(ContentController.class).findById(contentVO.getKey())).withSelfRel());
		return contentVO;
	}

	@Operation(summary = "Delete specific Content by your ID")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		services.delete(id);
		return ResponseEntity.ok().build();
	}
}
