package xyz.chandlerph.webminning.spider.domain;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "cookies")
public class Cookies {

	@Id
    private Long id;
 
    private String email;

    private String password;

    private String name;

    private String hash;

    private String cookie;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHash() {
		return hash;
	}

	
	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

}
