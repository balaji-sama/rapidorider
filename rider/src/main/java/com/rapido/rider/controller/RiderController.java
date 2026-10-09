package com.rapido.rider.controller;
	
	import java.util.List;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.web.bind.annotation.DeleteMapping;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PatchMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestBody;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.RestController;
	
	import com.rapido.rider.dto.AcceptBookingDto;
	import com.rapido.rider.dto.CordinateDto;
	import com.rapido.rider.dto.EarningSummaryDto;
	import com.rapido.rider.dto.ResponseStructure;
	import com.rapido.rider.dto.RiderBookingRequest;
	import com.rapido.rider.dto.RiderDto;
	import com.rapido.rider.dto.RiderHistoryDto;
	import com.rapido.rider.dto.VehicleDto;
	import com.rapido.rider.entity.Rider;
	import com.rapido.rider.service.RiderService;
	
	import jakarta.servlet.http.HttpServletResponse;
	
	@RestController
	public class RiderController {
		@Autowired
		RiderService riderservice;
	
		@PostMapping("/rider/createaccount")
		public void createacc(@RequestBody RiderDto riderdto) {
			riderservice.createaccservice(riderdto);
		}
	
		@DeleteMapping("/rider/deleteaccount")
		public ResponseStructure<Rider> deleteacc(@RequestParam int id) {
			return riderservice.deleteByID(id);
		}
	
		@GetMapping("/rider/findrider")
		public ResponseStructure<Rider> findrider(@RequestParam int id) {
			return riderservice.findById(id);
		}
	
		@PatchMapping("/rider/updatevehicle")
		public void updatevehicle(@RequestParam int id, @RequestBody VehicleDto vehicledto) {
			riderservice.updateVehicleService(id, vehicledto);
		}
	
		@PatchMapping("/rider/updatestatus")
		public void updateStatus(@RequestParam int id) {
			riderservice.updateStatus(id);
		}
	
		@PostMapping("/rider/sendcurrentlocation")
		public ResponseStructure<String> currentlocation(@RequestParam int id, @RequestParam String vehicletype,
				@RequestBody CordinateDto cordinatedto) {
			return riderservice.currentLocationService(id, vehicletype, cordinatedto);
		}
	
		@GetMapping("/rider/getassignedriders")
		public List<RiderBookingRequest> getAssignedRiders(@RequestParam int id) {
			return riderservice.getAssignedRidesService(id);
		}
	
		@PostMapping("/rider/acceptbooking")
		public AcceptBookingDto acceptBooking(@RequestParam int riderid, @RequestParam int bookingid) {
			return riderservice.acceptBookingService(riderid, bookingid);
		}
	
		@GetMapping("/rider/moveTowardsPickup")
		public void moveTowardsPickup(@RequestParam int bookingid, HttpServletResponse resp) {
	
			riderservice.moveTowardsPickupService(bookingid, resp);
		}
	
		@PostMapping("/rider/updateArrived")
		public void updateToArrived(@RequestParam int bookingid) {
			riderservice.updateToArrivedService(bookingid);
		}
	
		@PostMapping("/rider/verifyotp")
		public void verifyOtp(@RequestParam int bookingid, @RequestParam String otp) {
	
			riderservice.verifyOtpService(bookingid, otp);
		}
	
		@GetMapping("/rider/moveTowardsDrop")
		public void moveTowardsDrop(@RequestParam int bookingid, HttpServletResponse resp) {
	
			riderservice.moveTowardsDropService(bookingid, resp);
		}
	
		@PostMapping("/rider/updateReached")
		public void updateToReached(@RequestParam int bookingid) {
			riderservice.updateToReachedService(bookingid);
		}
	
		@PostMapping("/rider/completeRide")
		public void completeRide(@RequestParam int bookingid) {
	
			riderservice.completeRideService(bookingid);
		}
	
		@GetMapping("/rider/riderhistory")
		public List<RiderHistoryDto> riderHistory(@RequestParam int riderid, @RequestParam String status) {
	
			return riderservice.riderHistoryService(riderid, status);
		}
	
		@GetMapping("/rider/earning/summary")
		public EarningSummaryDto earningSummary(@RequestParam int riderid, @RequestParam String from,
				@RequestParam String to) {
	
			return riderservice.earningSummaryService(riderid, from, to);
		}
	
	}