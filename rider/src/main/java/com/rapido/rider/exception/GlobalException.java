package com.rapido.rider.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rapido.rider.dto.ResponseStructure;

public class GlobalException {
	@ExceptionHandler(RiderNotFoundException.class)
	public ResponseStructure<String> handleCustomerException() {
		ResponseStructure<String> rs = new ResponseStructure<String>();
		rs.setData("rider not found");
		rs.setMessage("rider not found");
		rs.setStatuscode(HttpStatus.NOT_FOUND.value());
		return rs;
	}


    @ExceptionHandler(RiderAlreadyAcceptedException.class)
    public ResponseStructure<String> handleRiderAlreadyAcceptedException() {

        ResponseStructure<String> rs = new ResponseStructure<String>();

        rs.setData("Rider has already accepted another ride");
        rs.setMessage("Rider has already accepted another ride");
        rs.setStatuscode(HttpStatus.NOT_FOUND.value());

        return rs;
    }
}