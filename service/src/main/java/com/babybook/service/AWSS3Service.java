package com.babybook.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
public class AWSS3Service
{

    @Autowired
    private AmazonS3 s3Client;

    @Value( "${aws.s3.bucket}" )
    private String bucket;

    public String uploadFile(final MultipartFile multipartFile, String path)
    {
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            return uploadFileToS3Bucket(bucket + "/" + path, file);

        } catch (final AmazonServiceException ex) {
            System.out.println("File upload is failed.");
            System.out.println(("Error= {} while uploading file." + ex.getMessage()));
        }
        return "n/a";
    }

    private File convertMultiPartFileToFile(final MultipartFile multipartFile)
    {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            System.out.println("Error converting the multi-part file to file= " + ex.getMessage());
        }
        return file;
    }

    private String uploadFileToS3Bucket(final String bucketName, final File file)
    {
        String dateTime = String.valueOf(LocalDateTime.now());
        String encodedDate = Base64.getEncoder().encodeToString(dateTime.getBytes());
        final String uniqueFileName = encodedDate + "_" + file.getName();
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
        s3Client.putObject(putObjectRequest);
        return uniqueFileName;
    }

    public List<File> uploadFileList(MultipartFile[] multipartFile, String key_prefix)
    {

        ArrayList<File> files = Arrays.asList(multipartFile).parallelStream().map(this::convertMultiPartFileToFile)
            .collect(Collectors.toCollection(ArrayList::new));
        //  long start = System.currentTimeMillis();
        //for (MultipartFile mf : multipartFile) {
        //   files.add(convertMultiPartFileToFile(mf));
        // }
        //System.out.println("Time taken enhanced loop ==" + (System.currentTimeMillis() - start));

        // long start1 = System.currentTimeMillis();
        // System.out.println("Time taken parallel loop ==" + (System.currentTimeMillis() - start1));
        TransferManager xfer_mgr = TransferManagerBuilder.standard().build();
        try {
            MultipleFileUpload xfer = xfer_mgr.uploadFileList(bucket,
                                                              key_prefix, new File("."), files);

            XferMgrProgress.showTransferProgress(xfer);
            XferMgrProgress.waitForCompletion(xfer);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } finally {
            xfer_mgr.shutdownNow();
        }

        return files;

    }

    public Callable uploadS3TaskRunnable(final MultipartFile multipartFile, String path)
    {
        System.out.println("Thread ========" + Thread.currentThread().getName());
        return () -> uploadFile(multipartFile, path);
    }

}
