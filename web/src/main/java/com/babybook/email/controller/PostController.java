package com.babybook.email.controller;

import com.babybook.email.constans.ApiBaseUrl;
import com.babybook.email.constans.PostType;
import com.babybook.email.exceptions.ResourceNotFoundException;
import com.babybook.email.model.Media;
import com.babybook.email.model.Post;
import com.babybook.email.utils.AppUtils;
import com.babybook.service.AWSS3Service;
import com.babybook.service.PostStorageService;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@RestController
@RequestMapping( ApiBaseUrl.API )
public class PostController
{
    private static final String S3_END_POINT = "https://babybook-00.s3.us-east-2.amazonaws.com/";
    private final PostStorageService postStorageService;
    private final AWSS3Service awss3Service;

    public PostController(PostStorageService postStorageService, AWSS3Service awss3Service)
    {
        this.postStorageService = postStorageService;
        this.awss3Service = awss3Service;
    }

    @PostMapping( "v1/secured/submit-post" )
    public ResponseEntity<Post> createPost(
        @RequestParam( value = "title", required = false ) String title,
        @RequestParam( value = "description", required = false ) String description,
        @RequestParam( value = "file", required = false ) MultipartFile[] files,
        @RequestParam( value = "album", required = false ) String album, HttpServletRequest request)
        throws ExecutionException, InterruptedException

    {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ip = request.getHeader("X-FORWARDED-FOR");

        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setAlbumName(album);
        List<Media> mediaList = new ArrayList<>();
        String username = request.getUserPrincipal().getName();
        String path = username.substring(0, username.indexOf('@'));
        List<String> uploadedFiles = new ArrayList<>();
       // int threads = files.length == 1 ? files.length / 2 : 1;
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < files.length; i++) {
            Future<String> filesFuture = executorService.submit(awss3Service.uploadS3TaskRunnable(files[i], path));
            uploadedFiles.add(filesFuture.get());
        }

        post.setIp(ip);
        post.setBrowser(userAgent.getBrowser() + "-" + userAgent.getOperatingSystem());
        post.setCreated(new Date());
        post.setLastUpdated(new Date());
        post.setMedia(mediaList);
        post.setPostedBy(request.getUserPrincipal().getName());
        post.setPostType(PostType.PRIVATE.name());

        for (String f : uploadedFiles) {
            Media media = new Media();
            media.setMediaType("image");
            media.setMediaLocation(S3_END_POINT + path + "/" + f);
            media.setMediaDescription(null);
            media.setCreated(new Date());
            media.setLastUpdated(new Date());
            media.setPost(post);
            mediaList.add(media);

        }
        Post savedPost = null;
        try {
            savedPost = postStorageService.savePosts(post);
        } catch (Exception ex) {

        }
        executorService.shutdown();
        AppUtils.shutdownAndAwaitTermination(executorService, 3600, TimeUnit.SECONDS);
        return new ResponseEntity<>(savedPost, HttpStatus.OK);
    }

    @GetMapping( "v1/secured/fetch-post" )
    public ResponseEntity<List<Post>> getPostsByUser(HttpServletRequest request)
    {
        Optional<List<Post>> posts = postStorageService.getPosts(request.getUserPrincipal().getName());
        if (!posts.isPresent()) {
            throw new ResourceNotFoundException("No post found for user:" + request.getUserPrincipal().getName());
        }
        return new ResponseEntity<>(posts.get(), HttpStatus.OK);
    }

}
