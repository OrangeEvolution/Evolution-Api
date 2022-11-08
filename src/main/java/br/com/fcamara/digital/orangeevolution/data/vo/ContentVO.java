package br.com.fcamara.digital.orangeevolution.data.vo;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentVO extends RepresentationModel<ContentVO> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
	private String description;
	private String link;
	private String partner;
	private int durationInMinutes;
	private Long category;
	private Long contentType;

}
