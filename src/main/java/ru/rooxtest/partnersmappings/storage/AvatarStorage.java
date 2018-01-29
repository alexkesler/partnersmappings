package ru.rooxtest.partnersmappings.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 *
 * Класс реализующий хранение аватарок
 *
 */
@Component
public class AvatarStorage {
    private static final Logger log = LoggerFactory.getLogger(AvatarStorage.class);

    @Autowired
    private Environment env;

    @Value("${storage.avatars}")
    private String avatarDirString;


    private Path avatarsDir;

    @PostConstruct
    public void checkStorage() {

        if (avatarDirString!=null && !avatarDirString.isEmpty())
            avatarsDir = Paths.get(avatarDirString);
        else
            avatarsDir = Paths.get(System.getProperty("catalina.base"), "avatars");

        if (!Files.exists(avatarsDir)) {
            try {
                Files.createDirectories(avatarsDir);
            } catch (IOException e) {
                log.error("Error creating avatar dir", e);
            }
        }
        log.debug("Avatars dir: " + avatarsDir.toString());
    }

    public byte[] readAvatar(UUID id) {
        log.debug("Reading avatar: " + id);
        Path filePath = avatarsDir.resolve(id.toString());
        if (Files.exists(filePath)) {
            try {
                return Files.readAllBytes(filePath);
            } catch (IOException e) {
                log.error("Error while reading avatar", e);
                return null;
            }
        }  else {
            return null;
        }
    }

    public void writeAvatar(UUID id, byte[] data) {
        if (data == null) return;
        log.debug("Writing avatar: " + id);
        Path filePath = avatarsDir.resolve(id.toString());
        try {
            Files.createDirectories(avatarsDir);
            Files.write(filePath, data, WRITE,CREATE,TRUNCATE_EXISTING);
        } catch (IOException e) {
            log.error("Error while writing avatar", e);
        }
    }

    public void deleteAvatar(UUID id) {
        log.debug("Deleting avatar: " + id);
        Path filePath = avatarsDir.resolve(id.toString());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Error while deleting avatar", e);
        }

    }



}
