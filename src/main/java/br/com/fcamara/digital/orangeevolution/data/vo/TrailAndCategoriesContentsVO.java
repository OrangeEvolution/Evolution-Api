package br.com.fcamara.digital.orangeevolution.data.vo;

import java.io.Serializable;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrailAndCategoriesContentsVO extends RepresentationModel<TrailAndCategoriesContentsVO>
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private String mounted_by;
	private List<CategoryContentVO> categories;

}
