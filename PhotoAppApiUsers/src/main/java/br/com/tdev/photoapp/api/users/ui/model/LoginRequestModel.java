package br.com.tdev.photoapp.api.users.ui.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestModel {
	
	private String email;
	private String password;

}
