package br.com.fcamara.digital.orangeevolution.data.vo;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.fcamara.digital.orangeevolution.data.model.enums.StatusProgressEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentProgressVO extends RepresentationModel<ContentProgressVO> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
	private Long content;
	private Long user;
	private StatusProgressEnum status;

}
