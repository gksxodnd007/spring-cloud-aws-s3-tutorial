package org.codingsquid.aws_s3_tutorial.dao;

import org.codingsquid.aws_s3_tutorial.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Transactional(readOnly = true)
    Optional<Player> findByUserId(String userId);

    @Modifying
    @Query(value = "UPDATE `players` SET `profile_image_url`=:profile_image_url WHERE `user_id`=:user_id",
            nativeQuery = true)
    int updateProfileImageUrl(@Param("profile_image_url")String profileImageUrl,
                               @Param("user_id")String userId);

}
