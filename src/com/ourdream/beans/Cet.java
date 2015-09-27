package com.ourdream.beans;

public class Cet {

	private String id;
	private String name;
	private String school;
	private String category;
	private String date;
	private String tatal;
	private String listening;
	private String reading;
	private String writingAndTranslation;
	private String error;

	public Cet() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTatal() {
		return tatal;
	}

	public void setTatal(String tatal) {
		this.tatal = tatal;
	}

	public String getListening() {
		return listening;
	}

	public void setListening(String listening) {
		this.listening = listening;
	}

	public String getReading() {
		return reading;
	}

	public void setReading(String reading) {
		this.reading = reading;
	}

	public String getWritingAndTranslation() {
		return writingAndTranslation;
	}

	public void setWritingAndTranslation(String writingAndTranslation) {
		this.writingAndTranslation = writingAndTranslation;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "Cet [id=" + id + ", name=" + name + ", school=" + school + ", category=" + category + ", date=" + date
				+ ", tatal=" + tatal + ", listening=" + listening + ", reading=" + reading + ", writingAndTranslation="
				+ writingAndTranslation + ", error=" + error + "]";
	}

}
