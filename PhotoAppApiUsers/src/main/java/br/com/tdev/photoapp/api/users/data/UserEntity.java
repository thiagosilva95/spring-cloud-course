package br.com.tdev.photoapp.api.users.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 2533022127749054360L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String firstName;
	
	@Column(nullable = false, length = 50)
	private String lastName;
	
	@Column(nullable = false, length = 120, unique = true)
	private String email;
	
	@Column(nullable = false, unique = true)
	private String userId;
	
	@Column(nullable = false, unique = true)
	private String encryptedPassword;
	
}
