package com.bhanu.ticketbookingsystem.controllers;

import com.bhanu.ticketbookingsystem.models.*;
import com.bhanu.ticketbookingsystem.models.ScreenInfo;
import com.bhanu.ticketbookingsystem.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    @Autowired
    BookingService bookingService;

    // Used to save screen information provided
    @RequestMapping(method= RequestMethod.POST,value="/screens")
    public void saveScreenInfo(@RequestBody ScreenInfo screenInfo){
        bookingService.saveScreenInfo(screenInfo);
    }

    // Used to reserve seats for a particular screen
    @RequestMapping(method= RequestMethod.POST,value="/{screenName}/reserve")
    public ResponseEntity reserveSeats(@RequestBody Seats seats, @PathVariable String screenName){
        boolean reserved = bookingService.reserveSeats(seats, screenName);
        if(reserved)
            return new ResponseEntity("All seats reserved successfully.", HttpStatus.OK);
        else
            return new ResponseEntity("Seats could not be reserved as some or all the seats specified by you were already reserved or not present.", HttpStatus.EXPECTATION_FAILED);
    }

    // Used to get all the unreserved seats for a given screen
    // /screens/{screenNname}/seats?status=unreserved
    @RequestMapping(value="/screens/{screenName}/seats",params = "status")
    public ResponseEntity getUnreservedSeats(@PathVariable String screenName, @RequestParam String status){
        Seats unreservedSeats = bookingService.getUnreserverdSeats(screenName);
        if(unreservedSeats!=null)
            return new ResponseEntity(unreservedSeats,HttpStatus.OK);
        else
            return new ResponseEntity("Screen name not found",HttpStatus.NOT_FOUND);
    }

    // Used to get a definite number of unreserved contiguous seats according to a specified seat
    // /screens/{screenName}/seats?numSeats={x}&choice={seat‑row‑and‑number}
    @RequestMapping(value="/screens/{screenName}/seats",params = { "numSeats", "choice" })
    public ResponseEntity getSpecifiedSeats(@PathVariable String screenName, @RequestParam int numSeats, @RequestParam String choice){
        AvailableSeats proposedSeats = bookingService.getSpecifiedSeats(screenName,numSeats,choice);
        if(proposedSeats==null)
            return new ResponseEntity("Cannot reserve the seats as per your requirements.",HttpStatus.EXPECTATION_FAILED);
        else
            return new ResponseEntity(proposedSeats,HttpStatus.OK);
    }
}
