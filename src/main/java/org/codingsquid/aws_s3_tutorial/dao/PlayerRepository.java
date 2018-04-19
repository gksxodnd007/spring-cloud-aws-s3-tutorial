package org.codingsquid.aws_s3_tutorial.dao;

import org.codingsquid.aws_s3_tutorial.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Transactional(readOnly = true)
    Optional<Player> findByUserId(String userId);

}
