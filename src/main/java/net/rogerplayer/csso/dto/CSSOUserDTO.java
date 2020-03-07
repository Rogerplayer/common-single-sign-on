package net.rogerplayer.csso.dto;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class CSSOUserDTO {

	private Long id;
	private String user;
	private String password;
	private String email;
	private String nome;
}
