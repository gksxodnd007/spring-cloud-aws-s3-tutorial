package org.codingsquid.aws_s3_tutorial.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT(11)")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

}
