package com.rapido.rider.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.rapido.rider.dto.RiderBookingRequest;

import tools.jackson.databind.ObjectMapper;

@Service
public class RedisService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	// 1. Get assigned rides for one rider
	public List<RiderBookingRequest> getAssignedRides(int riderId) {

		String key = "rider:" + riderId + ":requests";

		List<String> requests = redisTemplate.opsForList().range(key, 0, -1);

		List<RiderBookingRequest> result = new ArrayList<>();

		if (requests != null) {

			for (String json : requests) {

				try {

					RiderBookingRequest request = objectMapper.readValue(json, RiderBookingRequest.class);

					result.add(request);

				} catch (Exception e) {

					throw new RuntimeException("Failed to read ride request from Redis", e);
				}
			}
		}

		return result;
	}

	// 2. Remove complete request list of rider
	public void deleteRiderRequests(int riderId) {

		String key = "rider:" + riderId + ":requests";

		redisTemplate.delete(key);
	}

//		// 3. Remove a particular booking from all riders
//		public void removeBookingFromAllRiders(int bookingId) {
//
//			for (int riderId = 52; riderId <= 72; riderId++) {
//
//				String key = "rider:" + riderId + ":requests";
//
//				List<String> requests = redisTemplate.opsForList().range(key, 0, -1);
//
//				if (requests == null) {
//					continue;
//				}
//
//				for (String json : requests) {
//
//					try {
//
//						RiderBookingRequest request = objectMapper.readValue(json, RiderBookingRequest.class);
//
//						if (request.getBookingid() == bookingId) {
//
//							redisTemplate.opsForList().remove(key, 1, json);
//						}
//
//					} catch (Exception e) {
//
//						throw new RuntimeException("Failed to remove booking from Redis", e);
//					}
//				}
//			}
//		}

	public boolean lockBooking(int bookingId, int riderId) {

		String key = "booking:" + bookingId + ":lock";

		Boolean locked = redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(riderId), Duration.ofMinutes(10));

		return Boolean.TRUE.equals(locked);
	}

	public void deleteBookingLock(int bookingId) {

		String key = "booking:" + bookingId + ":lock";

		redisTemplate.delete(key);
	}

	public void removeBookingFromAllRiders(int bookingId) {

		Set<String> keys = redisTemplate.keys("rider:*:requests");

		if (keys == null || keys.isEmpty()) {
			return;
		}

		for (String key : keys) {

			List<String> requests = redisTemplate.opsForList().range(key, 0, -1);

			if (requests == null) {
				continue;
			}

			for (String json : requests) {

				try {

					RiderBookingRequest request = objectMapper.readValue(json, RiderBookingRequest.class);

					if (request.getBookingid() == bookingId) {

						redisTemplate.opsForList().remove(key, 1, json);
					}

				} catch (Exception e) {

					throw new RuntimeException("Failed to remove booking from rider requests", e);
				}
			}
		}
	}
}