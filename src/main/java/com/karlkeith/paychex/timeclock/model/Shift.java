package com.karlkeith.paychex.timeclock.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@EqualsAndHashCode(callSuper=true)
@ToString
@Entity
@Table(name = "shifts")
public class Shift extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "userid")
	private TimeClockUser userid;
	
	@Column(name = "starttime")
	private LocalDateTime starttime;
	
	@Column(name = "endtime")
	private LocalDateTime endtime;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "shiftid")
	private List<Breaks> breaks = new ArrayList<>();
	
	@Column(name = "onbreak")
	private boolean onBreak = false;;
	
	public Shift(TimeClockUser user) {
		this.userid = user;
		this.starttime = LocalDateTime.now();
	}
	
	public void addBreak(Breaks breaks) {
		instanciateBreaks();
		this.breaks.add(breaks);
	}
	
	public void endBreak() {
		if(isOnBreak()) {
			this.breaks.get(breaks.size() - 1).endBreak();
			onBreak = false;
		}
	}
	
	public void endShift() {
		endBreak();
		this.endtime = LocalDateTime.now();
	}
	
	public BreakType getCurrentBreakType() {
		if(onBreak) {
			return this.breaks.get(breaks.size() - 1).getBreaktype();
		}
		return null;
	}
	
	private void instanciateBreaks() {
		if(this.breaks == null) {
			this.breaks = new ArrayList<>();
		}
	}
	
	public void takeBreak(BreakType type) {
		instanciateBreaks();
		onBreak = true;
		this.breaks.add(new Breaks(this, type));
	}

}
