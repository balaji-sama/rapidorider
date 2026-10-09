package com.rapido.rider.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rapido.rider.dto.AcceptBookingDto;
import com.rapido.rider.dto.CompleteRideResponseDto;
import com.rapido.rider.dto.CordinateDto;
import com.rapido.rider.dto.EarningSummaryDto;
import com.rapido.rider.dto.ResponseStructure;
import com.rapido.rider.dto.RiderBookingRequest;
import com.rapido.rider.dto.RiderDto;
import com.rapido.rider.dto.RiderHistoryDto;
import com.rapido.rider.dto.VehicleDto;
import com.rapido.rider.dto.VerifyOtpResponseDto;
import com.rapido.rider.entity.Rider;
import com.rapido.rider.entity.Vehicle;
import com.rapido.rider.exception.RiderAlreadyAcceptedException;
import com.rapido.rider.exception.RiderNotFoundException;
import com.rapido.rider.repository.RiderRepository;
import com.rapido.rider.repository.VehicleRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class RiderService {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private RedisService redisService;
	@Autowired
	RiderRepository riderrepository;
	@Autowired
	VehicleRepository vehiclerepository;
	@Autowired
	RedisTemplate<String, String> redistemplate;

	public void createaccservice(RiderDto riderdto) {
		Rider rider = new Rider();
		rider.setRidername(riderdto.getRidername());
		rider.setMobile(riderdto.getRidermobile());
		rider.setRidergender(riderdto.getRidergender());
		rider.setRidermail(riderdto.getRidermail());
		rider.setRiderlicense(riderdto.getRiderlicense());
		rider.setStatus("offline");
		VehicleDto vehicledto = riderdto.getVdto();
		Vehicle vehicle = new Vehicle();
		vehicle.setVehicletype(vehicledto.getVehicletype());
		vehicle.setVehicleno(vehicledto.getVehicleno());
		vehicle.setVehiclename(vehicledto.getVehiclename());
		vehicle.setVehiclemodel(vehicledto.getVehiclemodel());
		rider.setVehicle(vehicle);
		riderrepository.save(rider);
	}

	public ResponseStructure<Rider> deleteByID(int id) {
		Rider rider = riderrepository.findById(id).orElseThrow(() -> new RiderNotFoundException());
		riderrepository.delete(rider);
		ResponseStructure<Rider> response = new ResponseStructure<Rider>();
		response.setData(rider);
		response.setMessage("rider with id is deleted");
		response.setStatuscode(HttpStatus.FOUND.value());
		return response;

	}

	public ResponseStructure<Rider> findById(int id) {
		Rider rider = riderrepository.findById(id).orElseThrow(() -> new RiderNotFoundException());
		ResponseStructure<Rider> response = new ResponseStructure<Rider>();
		response.setData(rider);
		response.setMessage("rider with id is found");
		response.setStatuscode(HttpStatus.FOUND.value());
		return response;
	}

	public void updateVehicleService(int id, VehicleDto vehicledto) {
		Rider rider = riderrepository.findById(id).orElseThrow(() -> new RiderNotFoundException());
		Vehicle oldvehicle = rider.getVehicle();
		if (oldvehicle != null) {
			rider.setVehicle(null);
			riderrepository.save(rider);
			vehiclerepository.delete(oldvehicle);
		}
		Vehicle newVehicle = new Vehicle();
		newVehicle.setVehiclename(vehicledto.getVehiclename());
		newVehicle.setVehiclemodel(vehicledto.getVehiclemodel());
		newVehicle.setVehicleno(vehicledto.getVehicleno());
		newVehicle.setVehicletype(vehicledto.getVehicletype());
		rider.setVehicle(newVehicle);
		riderrepository.save(rider);

	}

	public void updateStatus(int id) {
		Rider rider = riderrepository.findById(id).orElseThrow(() -> new RiderNotFoundException());
		if (rider.getStatus().equals("offline")) {
			rider.setStatus("online");
		} else if (rider.getStatus().equals("online") || rider.getStatus().equals("")) {
			rider.setStatus("offline");
		}
		riderrepository.save(rider);

	}

	public ResponseStructure<String> currentLocationService(int id, String vehicletype, CordinateDto cordinatedto) {
		Rider rider = riderrepository.findById(id).orElseThrow(() -> new RiderNotFoundException());
		if (rider.getStatus().equals("online") || rider.getStatus().equals("available")) {
			String key = vehicletype.toLowerCase();
			Point cordinates = new Point(cordinatedto.getLongitude(), cordinatedto.getLattitude());
			redistemplate.opsForGeo().add(key, cordinates, String.valueOf(id));
			redistemplate.expire(key, Duration.ofDays(30));
		} else {
			ResponseStructure<String> offline = new ResponseStructure<String>();
			offline.setData("rider is offline ");
			offline.setMessage("update the rider status");
			offline.setStatuscode(HttpStatus.NOT_FOUND.value());
			return offline;
		}
		ResponseStructure<String> online = new ResponseStructure<String>();
		online.setData("rider is online ");
		online.setMessage("data is stored in redis");
		online.setStatuscode(HttpStatus.FOUND.value());
		return online;

	}

	public List<RiderBookingRequest> getAssignedRidesService(int riderId) {

		return redisService.getAssignedRides(riderId);
	}

	public AcceptBookingDto acceptBookingService(int riderid, int bookingid) {

		// 1. Check rider
		Rider rider = riderrepository.findById(riderid).orElseThrow(() -> new RiderNotFoundException());

		// 2. Lock booking in Redis
		boolean locked = redisService.lockBooking(bookingid, riderid);

		if (!locked) {
			throw new RiderAlreadyAcceptedException();
		}

		try {

			// 3. Call Customer Server
			String url = "http://localhost:8087/customer/assignrider" + "?riderid=" + riderid + "&bookingid="
					+ bookingid;

			AcceptBookingDto bookingResponse = restTemplate.postForObject(url, null, AcceptBookingDto.class);

			// 4. Customer server did not assign booking
			if (bookingResponse == null) {
				throw new RiderAlreadyAcceptedException();
			}

			// 5. Make rider busy
			rider.setStatus("busy");
			riderrepository.save(rider);

			// 6. Remove all pending requests of this rider
			redisService.deleteRiderRequests(riderid);

			// 7. Remove this booking from other riders
			redisService.removeBookingFromAllRiders(bookingid);

			// 8. Return booking details
			return bookingResponse;

		} catch (Exception e) {

			// Release Redis lock if anything fails
			redisService.deleteBookingLock(bookingid);

			throw e;
		}
	}

	public void moveTowardsPickupService(int bookingid, HttpServletResponse resp) {

		try {

			String url = "http://localhost:8087/customer/moveTowardsPickup" + "?bookingid=" + bookingid;

			String googleMapsUrl = restTemplate.getForObject(url, String.class);

			resp.sendRedirect(googleMapsUrl);

		} catch (Exception e) {

			throw new RuntimeException("Unable to move towards pickup", e);
		}
	}

	public void updateToArrivedService(int bookingid) {

		String url = "http://localhost:8087/customer/UpdateToArrived" + "?bookingid=" + bookingid;

		String response = restTemplate.postForObject(url, null, String.class);
		System.out.println(response);
	}

	public void verifyOtpService(int bookingid, String otp) {

		String url = "http://localhost:8087/customer/verifyotp" + "?bookingid=" + bookingid + "&otp=" + otp;

		VerifyOtpResponseDto response = restTemplate.postForObject(url, null, VerifyOtpResponseDto.class);

		if (response == null) {
			throw new RuntimeException("No response from Customer Server");
		}

		if (!response.isVerified()) {
			throw new RuntimeException("Invalid OTP");
		}

		// OTP verified

		// update rider status here
	}

	public void moveTowardsDropService(int bookingid, HttpServletResponse resp) {
		try {

			String url = "http://localhost:8087/customer/moveTowardsDrop" + "?bookingid=" + bookingid;

			String googleMapsUrl = restTemplate.getForObject(url, String.class);

			resp.sendRedirect(googleMapsUrl);

		} catch (Exception e) {

			throw new RuntimeException("Unable to move towards destination", e);
		}
	}

	public void updateToReachedService(int bookingid) {
		String url = "http://localhost:8087/customer/UpdateToReached" + "?bookingid=" + bookingid;

		String response = restTemplate.postForObject(url, null, String.class);

		System.out.println(response);

	}

	public void completeRideService(int bookingid) {

		// 1. Call Customer Server
		String url = "http://localhost:8087/customer/completeRide" + "?bookingid=" + bookingid;

		CompleteRideResponseDto response = restTemplate.postForObject(url, null, CompleteRideResponseDto.class);

		if (response == null) {
			throw new RuntimeException("Unable to complete ride");
		}

		// 2. Find rider
		Rider rider = riderrepository.findById(response.getRiderid()).orElseThrow(() -> new RiderNotFoundException());

		// 3. Get platform charge calculated by Customer Server
		double platformCharge = response.getPlatformcharge();

		// 4. Deduct platform charge from rider wallet
		double wallet = rider.getRiderwallet() - platformCharge;

		rider.setRiderwallet(wallet);

		// 5. Increase number of rides
		rider.setRidernoofrides(rider.getRidernoofrides() + 1);

		// 6. Check rider wallet
		if (rider.getRiderwallet() < -1000) {
			rider.setStatus("blocked");
		} else {
			rider.setStatus("available");
		}

		// 7. Save rider
		riderrepository.save(rider);
	}

	public List<RiderHistoryDto> riderHistoryService(int riderid, String status) {

		String url = "http://localhost:8087/customer/riderhistory" + "?riderid=" + riderid + "&status=" + status;

		RiderHistoryDto[] response = restTemplate.getForObject(url, RiderHistoryDto[].class);

		if (response == null) {
			return new ArrayList<>();
		}

		return Arrays.asList(response);
	}

	public EarningSummaryDto earningSummaryService(int riderid, String from, String to) {

		String url = "http://localhost:8087/customer/earning/summary" + "?riderid=" + riderid + "&from=" + from + "&to="
				+ to;

		EarningSummaryDto response = restTemplate.getForObject(url, EarningSummaryDto.class);

		if (response == null) {
			throw new RuntimeException("Unable to fetch earning summary");
		}

		return response;
	}

}