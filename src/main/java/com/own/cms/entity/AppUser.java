package com.own.cms.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table( name="app_user")
public class AppUser implements UserDetails{


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	@NotBlank(message ="The field mandatory")
	private String username;
	
	@Column
    private String password;
	
	@Column
	@NotBlank(message ="The field mandatory")
	private String firstName;
	
	@Column
	@NotBlank(message ="The field mandatory")
	private String lastName;

    @Transient
    private String passwordConfirm;
    
    @Transient
    private String plainPassword;
    
    @Column
    @Email
    private String email;

    @ManyToMany(fetch = FetchType.EAGER) 
    private List<AppRole> roles;
    
    
    @Column(nullable = true)
    private String photo;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public List<AppRole> getRoles() {
        return roles;
    }

    public void setRoles(List<AppRole> roles) {
        this.roles = roles;
    }

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "AppUser [id=" + id + ", username=" + username + ", password=" + password + ", roles=" + roles + "]";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (AppRole role : this.getRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return grantedAuthorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}


    
}
