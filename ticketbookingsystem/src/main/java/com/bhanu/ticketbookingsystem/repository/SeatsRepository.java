package com.bhanu.ticketbookingsystem.repository;

import com.bhanu.ticketbookingsystem.models.Seat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

// Repository interface to query screen database
@Repository
public interface SeatsRepository extends CrudRepository<Seat,Integer> {

    Optional<Seat> findByScreenNameAndRowNameAndSeatNumberAndReserved(String screenName, String rowName, Integer seatNumber, boolean b);

    Optional<ArrayList<Seat>> findByScreenName(String screenName);

    Optional<ArrayList<Seat>> findByScreenNameAndReserved(String screenName, boolean b);

    Optional<ArrayList<Seat>> findByScreenNameAndRowNameAndReservedOrderBySeatNumberAsc(String screenName, String rowName, boolean b);
}
