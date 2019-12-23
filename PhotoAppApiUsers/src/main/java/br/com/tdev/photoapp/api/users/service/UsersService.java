package br.com.tdev.photoapp.api.users.service;

import br.com.tdev.photoapp.api.users.shared.UserDto;

public interface UsersService {
	
	UserDto createUser(UserDto userDetails);

}
