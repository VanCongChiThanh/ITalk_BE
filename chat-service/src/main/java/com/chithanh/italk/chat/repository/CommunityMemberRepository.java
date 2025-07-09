package com.chithanh.italk.chat.repository;

import com.chithanh.italk.chat.domain.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommunityMemberRepository extends JpaRepository<CommunityMember, UUID> {
    Optional<CommunityMember> findByCommunityIdAndUserId(UUID communityId, UUID userId);
}