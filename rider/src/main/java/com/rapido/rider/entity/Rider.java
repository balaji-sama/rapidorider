package com.rapido.rider.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Rider {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String status;
	private String ridername;
	private long ridermobno;
	private String ridermail;
	private String ridergender;
	
	private double riderwallet;
	
	private int ridernoofrides;
	
	private String riderlicense;
	@OneToOne(cascade = CascadeType.ALL)
	private Vehicle vehicle;
	public Rider(String status, String ridername, long ridermobno, String ridermail, String ridergender,
			double riderwallet, int ridernoofrides, String riderlicense, Vehicle vehicle) {
		super();
		this.status = status;
		this.ridername = ridername;
		this.ridermobno = ridermobno;
		this.ridermail = ridermail;
		this.ridergender = ridergender;
		this.riderwallet = riderwallet;
		this.ridernoofrides = ridernoofrides;
		this.riderlicense = riderlicense;
		this.vehicle = vehicle;
	}
	public Rider() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRidername() {
		return ridername;
	}
	public void setRidername(String ridername) {
		this.ridername = ridername;
	}
	public long getRidermobno() {
		return ridermobno;
	}
	public void setRidermobno(long ridermobno) {
		this.ridermobno = ridermobno;
	}
	public String getRidermail() {
		return ridermail;
	}
	public void setRidermail(String ridermail) {
		this.ridermail = ridermail;
	}
	public String getRidergender() {
		return ridergender;
	}
	public void setRidergender(String ridergender) {
		this.ridergender = ridergender;
	}
	public double getRiderwallet() {
		return riderwallet;
	}
	public void setRiderwallet(double riderwallet) {
		this.riderwallet = riderwallet;
	}
	public int getRidernoofrides() {
		return ridernoofrides;
	}
	public void setRidernoofrides(int ridernoofrides) {
		this.ridernoofrides = ridernoofrides;
	}
	public String getRiderlicense() {
		return riderlicense;
	}
	public void setRiderlicense(String riderlicense) {
		this.riderlicense = riderlicense;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public void setMobile(long ridermobile) {
		// TODO Auto-generated method stub
		
	}
	
	
}
