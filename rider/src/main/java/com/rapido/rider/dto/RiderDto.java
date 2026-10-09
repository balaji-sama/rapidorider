package com.rapido.rider.dto;

public class RiderDto {

	private String ridername;
	private long ridermobile;
	private String ridergender;
	private String ridermail;
	private String riderlicense;
	private VehicleDto vdto;
	
	
	public RiderDto(String ridername, long ridermobile, String ridergender, String ridermail, String riderlicense,
			VehicleDto vdto) {
		super();
		this.ridername = ridername;
		this.ridermobile = ridermobile;
		this.ridergender = ridergender;
		this.ridermail = ridermail;
		this.riderlicense = riderlicense;
		this.vdto = vdto;
	}
	public RiderDto() {
		super();
	}
	public String getRidername() {
		return ridername;
	}
	public void setRidername(String ridername) {
		this.ridername = ridername;
	}
	public long getRidermobile() {
		return ridermobile;
	}
	public void setRidermobile(long ridermobile) {
		this.ridermobile = ridermobile;
	}
	public String getRidergender() {
		return ridergender;
	}
	public void setRidergender(String ridergender) {
		this.ridergender = ridergender;
	}
	public String getRidermail() {
		return ridermail;
	}
	public void setRidermail(String ridermail) {
		this.ridermail = ridermail;
	}
	public String getRiderlicense() {
		return riderlicense;
	}
	public void setRiderlicense(String riderlicense) {
		this.riderlicense = riderlicense;
	}
	public VehicleDto getVdto() {
		return vdto;
	}
	public void setVdto(VehicleDto vdto) {
		this.vdto = vdto;
	}
	
}
