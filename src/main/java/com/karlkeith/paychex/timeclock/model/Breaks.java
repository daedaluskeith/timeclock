package com.karlkeith.paychex.timeclock.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "breaks")
public class Breaks extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "shiftid")
	private Shift shiftid;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "breaktype")
	private BreakType breaktype;
	
	@Column(name = "starttime")
	private LocalDateTime starttime;
	
	@Column(name = "endtime")
	private LocalDateTime endtime;
	
	public Breaks(Shift shift, BreakType type) {
		this.shiftid = shift;
		this.breaktype = type;
		this.starttime = LocalDateTime.now();
	}
	
	public void endBreak() {
		this.endtime = LocalDateTime.now();
	}
}
