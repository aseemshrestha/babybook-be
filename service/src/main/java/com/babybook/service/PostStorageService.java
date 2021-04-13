package com.babybook.service;

import com.babybook.email.model.Post;
import com.babybook.repository.PostStorageRepository;
import exceptions.StorageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostStorageService
{

    private final PostStorageRepository postStorageRepository;

    public PostStorageService(PostStorageRepository postStorageRepository)
    {
        this.postStorageRepository = postStorageRepository;
    }
    // s3 - no uppercase / underscore / no IP - must start with lower case or number
    public List<String> uploadFiles(MultipartFile[] files, String path)
    {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            try (InputStream is = file.getInputStream()) {
                Files.copy(is, Paths.get(path + fileName), StandardCopyOption.REPLACE_EXISTING);
                fileNames.add(fileName);
            } catch (IOException e) {
                // e.printStackTrace();
                String msg = String.format("Failed to store file %f", file.getOriginalFilename());
                throw new StorageException(msg, e);
            }
        }
        return fileNames;
    }

    @Transactional( rollbackFor = Exception.class )
    public Post savePosts(Post post)
    {
        return this.postStorageRepository.save(post);
    }

    public Optional<List<Post>> getPosts(String username)
    {
        return postStorageRepository.findPostByUsername(username);
    }
}
