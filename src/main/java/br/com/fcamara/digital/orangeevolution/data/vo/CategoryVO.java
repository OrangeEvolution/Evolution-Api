package br.com.fcamara.digital.orangeevolution.data.vo;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryVO extends RepresentationModel<CategoryVO> implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonProperty("id")
	private Long key;
	private String name;

}
