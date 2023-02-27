package com.karlkeith.paychex.timeclock.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="clockusers")
public class TimeClockUser extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;
	
	@Column(name = "username")
	private String username;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "usertype")
	private UserType userType;
	
	@Builder
	public TimeClockUser(Long id, String name, String username, UserType userType) {
		super(id);
		this.name = name;
		this.username = username;
		this.userType = userType;
	}

}
