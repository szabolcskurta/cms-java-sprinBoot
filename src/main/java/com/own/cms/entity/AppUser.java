package com.own.cms.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.own.cms.entity.AppUserGroup;

@Entity
@Table( name="app_user")
public class AppUser implements  UserDetails{
	

	
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	@NotBlank(groups= {AppUserGroup.class},message ="The field mandatory")
	private String username;
	
	@Column
    private String password;
	
	@Column
	@NotBlank(groups= {AppUserGroup.class},message ="The field mandatory")
	private String firstName;
	
	@Column
	@NotBlank(groups= {AppUserGroup.class},message ="The field mandatory")
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
    
    @Column(name = "reset_password_token")
    private String resetPasswordToken;
    
    @OneToMany(mappedBy = "createdBy",cascade = CascadeType.MERGE,targetEntity = AppArticle.class)
    private List<AppArticle> articles = new ArrayList<AppArticle>();
    
//    @Column(name="auth_provider")
//    @Enumerated(EnumType.STRING)
//    private AuthenticationProvider provider;
// 
//    public AuthenticationProvider getProvider() {
//        return provider;
//    }
// 
//    public void setProvider(AuthenticationProvider provider) {
//        this.provider = provider;
//    }
     

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

	public List<AppArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<AppArticle> articles) {
		this.articles = articles;
	}
	
	
	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	@Override
	public String toString() {
		return "AppUser [id=" + id + ", username=" + username + ", password=" + password + ", roles=" + roles + "]";
	}
	
	@PreRemove
	private void preRemove() {
	  this.articles.forEach(article-> article.setCreatedBy(null));
	}

}
