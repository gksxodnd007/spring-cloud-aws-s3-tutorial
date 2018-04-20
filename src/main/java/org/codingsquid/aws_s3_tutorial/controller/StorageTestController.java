package org.codingsquid.aws_s3_tutorial.controller;

import org.codingsquid.aws_s3_tutorial.dao.PlayerRepository;
import org.codingsquid.aws_s3_tutorial.domain.Player;
import org.codingsquid.aws_s3_tutorial.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class StorageTestController {

    private S3Service s3Service;
    private PlayerRepository playerRepository;
    private static Logger logger = LoggerFactory.getLogger(StorageTestController.class);
    private static Logger fileLogger = LoggerFactory.getLogger("log.Rest");

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
            logger.info("file is empty");
        }

        String profileImageUrl = s3Service.uploadFile(multipartFile, multipartFile.getOriginalFilename());
        logger.info(profileImageUrl);

        //플레이어 정보 조회
        Optional<Player> player = playerRepository.findByUserId(playerId);

        //파일 업로드 후 url 저장
        player.get().setProfileImageUrl(s3Service.uploadFile(multipartFile, multipartFile.getOriginalFilename()));
        playerRepository.save(player.get());
    }

    @DeleteMapping(value = "/spring-cloud-storage/tutorial/{player_id}")
    public void deleteProfileImage(@PathVariable(name = "player_id")String player_id) {
        Optional<Player> player = playerRepository.findByUserId(player_id);
        String profileImageUrl;
        /*
        * 1. 플레이어 정보 조회
        * 2. 플레이어의 프로필 이미지 파일 이름 파싱
        * 3. 파일 이름으로 스토리지에 있는 이미지 파일 삭제
        * 4. 플레이어의 정보 업로드
        * */
        if (player.isPresent()) {
            profileImageUrl = player.get().getProfileImageUrl();
            Stream<String> strStream = Arrays.stream(profileImageUrl.split("/"));
            Optional<String> imageName = strStream.filter(s -> s.contains("profile_image_")).findFirst();
            logger.info(imageName.get());
            //파일 삭제
            s3Service.deleteFileFromS3Bucket(imageName.get());
            //플레이어의 정보 업로드
            playerRepository.updateProfileImageUrl("", player_id);
        }
    }

    @GetMapping(value = "/spring-cloud-storage/log-test")
    public String logTest(@RequestParam(name = "log")String log,
                          HttpServletRequest request) {

        fileLogger.debug("file log: " + log
                + ", method: " + request.getMethod()
                + ", ip: " + request.getRemoteAddr()
                + ", port:" + request.getRemotePort());

        logger.info("info log: " + log);
        logger.debug("debug log: " + log);
        return log;
    }

}
