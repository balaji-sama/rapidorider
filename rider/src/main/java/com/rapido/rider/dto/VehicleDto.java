package com.rapido.rider.dto;

public class VehicleDto {

	private String  vehicletype;
	private String  vehicleno;
	private String  vehiclename;
	private String  vehiclemodel;
	
	public VehicleDto(String vehicletype, String vehicleno, String vehiclename, String vehiclemodel) {
		super();
		this.vehicletype = vehicletype;
		this.vehicleno = vehicleno;
		this.vehiclename = vehiclename;
		this.vehiclemodel = vehiclemodel;
	}
	public VehicleDto() {
		super();
	}
	public String getVehicletype() {
		return vehicletype;
	}
	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}
	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	public String getVehiclename() {
		return vehiclename;
	}
	public void setVehiclename(String vehiclename) {
		this.vehiclename = vehiclename;
	}
	public String getVehiclemodel() {
		return vehiclemodel;
	}
	public void setVehiclemodel(String vehiclemodel) {
		this.vehiclemodel = vehiclemodel;
	}
	
	
	
}
