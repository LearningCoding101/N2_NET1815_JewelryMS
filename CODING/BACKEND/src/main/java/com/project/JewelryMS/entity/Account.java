package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.JewelryMS.enumClass.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Account")
@Getter
@Setter
@ToString
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_userID")
    private int PK_userID;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "aUsername", unique = true)
    private String aUsername;

    @Column(name = "aPassword")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String aPassword;

    @Column(name = "accountName")
    private String accountName;

    @Column(name = "status")
    private Integer status;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private RoleEnum role;

    @Column(name = "aImage")
    private String aImage;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("account")
    private StaffAccount staffAccount;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.aPassword;
    }

    @Override
    public String getUsername() {
        return this.aUsername;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
