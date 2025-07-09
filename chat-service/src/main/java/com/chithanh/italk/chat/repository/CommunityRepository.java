package com.chithanh.italk.chat.repository;

import com.chithanh.italk.chat.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CommunityRepository extends JpaRepository<Community, UUID> {

}