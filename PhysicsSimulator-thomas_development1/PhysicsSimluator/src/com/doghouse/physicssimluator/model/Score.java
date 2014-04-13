package com.doghouse.physicssimluator.model;

import com.doghouse.physicssimluator.views.StarViewsHelper;

public class Score implements Comparable<Score> {

	private String name;
	private long time;
	private String timeString;
	private int stars;
	private int level;
	
	public Score(String name, long time,int stars){
		this.name = name;
		this.time = time;
		this.stars = stars;
		
		timeString = StarViewsHelper.formatMilisecondTime(time);
			
	}
	
	public Score(String name, long time,int stars,int level){
		this(name,time,stars);
		this.level = level;
	}
	
	public Score(int level, int stars, long time){
		this(null,time,stars);
	}
	
	
	
	public String getName() {
		return name;
	}
	public long getTime() {
		return time;
	}
	
	public String getTimeString() {
		return timeString;
	}
	
	public int getStars() {
		return stars;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public int compareTo(Score rhs) {
		if(getTime() == rhs.getTime()){
			return 0;
		}else if(getTime() > rhs.getTime()){
			return 1;
		}else{
			return -1;
		}
	}


}
