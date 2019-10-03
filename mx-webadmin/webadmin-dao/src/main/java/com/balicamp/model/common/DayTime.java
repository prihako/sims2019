package com.balicamp.model.common;

import java.util.Calendar;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class DayTime 
	extends BaseObject 
	implements Comparable<DayTime>
{
	
	private static final long serialVersionUID = 6419765169693844434L;
	
	private int hour;
	private int minute;
	private int second;
	
	public DayTime(){
	}

	public DayTime(int hour, int minute, int second){
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	
	public DayTime(Calendar nowCalendar){
		this(
			nowCalendar.get(Calendar.HOUR_OF_DAY),
			nowCalendar.get(Calendar.MINUTE),
			nowCalendar.get(Calendar.SECOND)
		);
	}
	
	public DayTime(String input){
		String[] inputSplitted = input.split(":");
		int len = inputSplitted.length;
		
		if ( len > 0 )
			setHour(Integer.parseInt(inputSplitted[0]));
		else 
			setHour(0);
		
		if ( len > 1 )
			setMinute(Integer.parseInt(inputSplitted[1]));
		else {
			setMinute (0);
		}
		
		if ( len == 3 )
			setSecond(Integer.parseInt(inputSplitted[2]));
		else 
			setSecond(0);
	}


	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if ( object == this ) return true;	
		if (!(object instanceof DayTime)) return false;
		
		DayTime rhs = (DayTime) object;
		return new EqualsBuilder()
			.append(this.hour, rhs.hour)
			.append(this.minute, rhs.minute)
			.append(this.second, rhs.second)
			.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(1085379259, 1481806375)
			.append(this.hour)
			.append(this.minute)
			.append(this.second)
			.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return 
			new StringBuilder()
			.append(getHour())
			.append(":")
			.append(getMinute())
			.append(":")
			.append(getSecond())
			.toString();
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(DayTime object) {
		DayTime dayTime2 = (DayTime) object;
		return new CompareToBuilder()
			.append(this.hour, dayTime2.hour)
			.append(this.minute, dayTime2.minute)
			.append(this.second, dayTime2.second)
			.toComparison();
	}


}
