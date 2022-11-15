package br.com.fcamara.digital.orangeevolution.data.vo;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.fcamara.digital.orangeevolution.data.model.enums.StatusProgressEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentAndProgressVO extends RepresentationModel<ContentAndProgressVO> implements Serializable {

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
	private StatusProgressEnum progressEnum;
	private Long progress;

}