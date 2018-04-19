package org.codingsquid.aws_s3_tutorial.controller;

import org.codingsquid.aws_s3_tutorial.dao.PlayerRepository;
import org.codingsquid.aws_s3_tutorial.domain.Player;
import org.codingsquid.aws_s3_tutorial.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class StorageTestController {

    private S3Service s3Service;
    private PlayerRepository playerRepository;

    @Autowired
    public StorageTestController(S3Service s3Service, PlayerRepository playerRepository) {
        this.s3Service = s3Service;
        this.playerRepository = playerRepository;
    }

    @PostMapping(value = "/spring-cloud-storage/tutorial/{player_id}",
            consumes = "multipart/form-data")
    public void uploadProfileImage(@PathVariable(name = "player_id")String playerId,
                                   @RequestPart(name = "file")MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            System.out.println("file is empty");
        }

        String profileImageUrl = s3Service.uploadFile(multipartFile, multipartFile.getOriginalFilename());
        System.out.println(profileImageUrl);

        Optional<Player> player = playerRepository.findByUserId(playerId);

        //파일 업로드 후 url 저장
        player.get().setProfileImageUrl(s3Service.uploadFile(multipartFile, multipartFile.getOriginalFilename()));
        playerRepository.save(player.get());
    }

    @DeleteMapping(value = "/spring-cloud-storage/tutorial/{player_id}")
    public void deleteProfileImage(@PathVariable(name = "player_id")String player_id) {
        Optional<Player> player = playerRepository.findByUserId(player_id);
        String profileImageUrl;

        if (player.isPresent()) {
            profileImageUrl = player.get().getProfileImageUrl();
            Stream<String> strStream = Arrays.stream(profileImageUrl.split("/"));
            Optional<String> imageName = strStream.filter(s -> s.contains("profile_image_")).findFirst();
            System.out.println(imageName.get());
            //파일 삭제
            s3Service.deleteFileFromS3Bucket(imageName.get());
        }
    }
}
