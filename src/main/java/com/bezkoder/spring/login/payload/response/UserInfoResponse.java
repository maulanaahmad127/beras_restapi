package com.bezkoder.spring.login.payload.response;

import java.util.List;

public class UserInfoResponse {
	private Long id;
	private String username;
	private String nama;
	private String no_handphone;
	private String jenis_kelamin;
	private String email;
	private List<String> roles;

	public UserInfoResponse(Long id, String username, String nama, String no_handphone, String jenis_kelamin, String email,  List<String> roles) {
		this.id = id;
		this.username = username;
		this.nama = nama;
		this.no_handphone = no_handphone;
		this.jenis_kelamin = jenis_kelamin;
		this.email = email;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getJenis_kelamin() {
		return jenis_kelamin;
	}

	public void setJenis_kelamin(String jenis_kelamin) {
		this.jenis_kelamin = jenis_kelamin;
	}

	public String getNo_handphone() {
		return no_handphone;
	}

	public void setNo_handphone(String no_handphone) {
		this.no_handphone = no_handphone;
	}


	
	
}
