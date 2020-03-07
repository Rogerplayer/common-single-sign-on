package net.rogerplayer.csso.dto;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class CSSOUserDTO {

	private Long id;
	private String username;
	private String password;
	private String email;
	private String nome;
}
