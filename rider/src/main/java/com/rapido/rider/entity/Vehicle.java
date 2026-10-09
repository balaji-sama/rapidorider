package com.rapido.rider.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Vehicle {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int vehicleid;
	private String vehicletype;
	private String vehicleno;
	private  String vehiclename;
	private String vehiclemodel;
	public Vehicle(String vehicletype, String vehicleno, String vehiclename, String vehiclemodel) {
		super();
		this.vehicletype = vehicletype;
		this.vehicleno = vehicleno;
		this.vehiclename = vehiclename;
		this.vehiclemodel = vehiclemodel;
	}
	public Vehicle() {
		super();
	}
	public int getVehicleid() {
		return vehicleid;
	}
	public void setVehicleid(int vehicleid) {
		this.vehicleid = vehicleid;
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