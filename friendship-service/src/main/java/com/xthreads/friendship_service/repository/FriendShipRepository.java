package com.xthreads.friendship_service.repository;

import com.xthreads.friendship_service.entity.FriendShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, String> {

}
