package com.bhanu.ticketbookingsystem.services;

import com.bhanu.ticketbookingsystem.models.*;
import com.bhanu.ticketbookingsystem.repository.SeatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

@Service
@Transactional
public class BookingService {

    @Autowired
    SeatsRepository seatsRepository;

    //saves the screen information provided by the user
    public void saveScreenInfo(ScreenInfo screenInfo){
        ArrayList<Seat> listOfSeats = getSeatsFromScreenInfo(screenInfo);
        for(Seat seat : listOfSeats){
            seatsRepository.save(seat);
        }
    }

    //returns a list of seats from the given Screen information
    public ArrayList<Seat> getSeatsFromScreenInfo(ScreenInfo screenInfo){
        ArrayList<Seat> result = new ArrayList<>();
        String name = screenInfo.getName();
        Seat seat;
        for(Map.Entry<String, RowInfo> row : screenInfo.getSeatInfo().entrySet()){
            for(int seatNumber=0;seatNumber<row.getValue().getNumberOfSeats();seatNumber++) {
                seat = new Seat(name, row.getKey(), seatNumber, false, row.getValue().getAisleSeats().contains(seatNumber));
                result.add(seat);
            }
        }
        return result;
    }

    //reserves the seats specified by the user, false will be returned if the user wants to book a non-existent seat
    //or an already reserved seat, true is returned otherwise
    //Concurrency is also handled
    @Transactional(isolation=Isolation.REPEATABLE_READ)
    public boolean reserveSeats(Seats seats, String screenName){
        String rowName;
        ArrayList<Seat> listOfSeatsToReserve = new ArrayList<>();
        for(Map.Entry<String,ArrayList<Integer>> row : seats.getSeats().entrySet()){
            rowName = row.getKey();
            for(Integer seatNumber : row.getValue()){
                Optional<Seat> optionalSeat = seatsRepository.findByScreenNameAndRowNameAndSeatNumberAndReserved(screenName, rowName, seatNumber, false);
                if(optionalSeat.isPresent())
                    listOfSeatsToReserve.add(optionalSeat.get());
                else
                    return false;
            }
        }
        for(Seat seat : listOfSeatsToReserve){
            seat.setReserved(true);
            seatsRepository.save(seat);
        }
        return true;
    }

    // Returns a list of all unreserved seats for a given screen
    public Seats getUnreserverdSeats(String screenName) {
        Optional<ArrayList<Seat>> optioanlSeats = seatsRepository.findByScreenName(screenName);
        HashMap<String,Integer> distinctRows = new HashMap<>();
        if(!optioanlSeats.isPresent())
            return null;
        else{
            for(Seat seat : optioanlSeats.get()){
                distinctRows.put(seat.getRowName(),1);
            }
        }
        HashMap<String,ArrayList<Integer>> unreservedSeats = new HashMap<>();
        for(String rowName : distinctRows.keySet())
            unreservedSeats.put(rowName,new ArrayList<>());

        Optional<ArrayList<Seat>> optionalUnreservedSeats = seatsRepository.findByScreenNameAndReserved(screenName,false);
        for(Seat seat : optionalUnreservedSeats.get())
            unreservedSeats.get(seat.getRowName()).add(seat.getSeatNumber());
        Seats result = new Seats();
        result.setSeats(unreservedSeats);
        return result;
    }

    // Used to get a definite number of unreserved contiguous seats according to a specified seat from a given screen
    public AvailableSeats getSpecifiedSeats(String screenName, int numSeats, String choice) {
        String rowName = choice.charAt(0)+"";
        int seatNumber = Integer.parseInt(choice.substring(1,choice.length()));
        Optional<ArrayList<Seat>> optionalValidSeats = seatsRepository.findByScreenNameAndRowNameAndReservedOrderBySeatNumberAsc(screenName,rowName,false);
        if(!optionalValidSeats.isPresent())
            return null;
        ArrayList<Seat> availableSeats = optionalValidSeats.get();
        return getSpecifiedSeatsFromAvailableSeats(availableSeats,numSeats,seatNumber,rowName);
    }

    // Utility method used to get a definite number of unreserved contiguous seats according to a specified seat from a list of unreserved seats
    private AvailableSeats getSpecifiedSeatsFromAvailableSeats(ArrayList<Seat> availableSeats, int numSeats, int seatNumber, String rowName) {
        int length = availableSeats.size();
        int current=0,left=0,right=0;
        for(current=0;current<length;current++){
            if(availableSeats.get(current).getSeatNumber()==seatNumber)
                break;
        }
        if(current==length)
            return null;
        if(current==0)
            left=current;
        else if(availableSeats.get(current).isAisle()&&availableSeats.get(current-1).isAisle())
            left=current;
        else {
            for (left=current-1;left>=0;left--){
                if(availableSeats.get(left+1).getSeatNumber()-availableSeats.get(left).getSeatNumber()>1){
                    left++;
                    break;
                }
                if(availableSeats.get(left).isAisle()==true)
                    break;
            }
        }

        if(current==length-1)
            right=current;
        else if(availableSeats.get(current).isAisle()&&availableSeats.get(current+1).isAisle())
            right=current;
        else{
            for (right=current+1;right<length;right++){
                if(availableSeats.get(right).getSeatNumber()-availableSeats.get(right-1).getSeatNumber()>1){
                    right--;
                    break;
                }
                if(availableSeats.get(right).isAisle()==true)
                    break;
            }
        }
        if(right-left+1<numSeats)
            return null;
        if(current-left+1>=numSeats) {
            left = current - numSeats + 1;
            right = current;
        }else
            right=left+numSeats-1;
        HashMap<String,ArrayList<Integer>> proposedSeats = new HashMap<>();
        proposedSeats.put(rowName,new ArrayList<>());
        for(int x=left;x<=right;x++)
            proposedSeats.get(rowName).add(availableSeats.get(x).getSeatNumber());
        AvailableSeats result = new AvailableSeats();
        result.setAvailableSeats(proposedSeats);
        return result;
    }
}
