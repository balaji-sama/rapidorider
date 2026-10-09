package com.rapido.rider.dto;

public class CordinateDto {

	private double Longitude;
	private double Lattitude;
	public CordinateDto(double longitude, double lattitude) {
		super();
		Longitude = longitude;
		Lattitude = lattitude;
	}
	public CordinateDto() {
		super();
	}
	public double getLongitude() {
		return Longitude;
	}
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
	public double getLattitude() {
		return Lattitude;
	}
	public void setLattitude(double lattitude) {
		Lattitude = lattitude;
	}
	
}
