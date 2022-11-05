package br.com.fcamara.digital.orangeevolution.security;

import java.io.Serializable;

import lombok.Data;

@Data
public class AccountCredentialsVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;

}