package com.andrew.ers.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

@Service
public class S3Service {
	
	public static final Regions REGION = Regions.US_EAST_1;
	public static final String BUCKET_NAME = "ers-app-receipts";
	public static final String KEY_PREFIX = "receipt";
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final AmazonS3 s3Client =
		AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(REGION)
                .build();
	
    public String getBucketKey(String username, long expenseId) {
		return String.format("%s/%s-%d", username, KEY_PREFIX, expenseId);
    }
	
	private Date getExpireDate(int hoursLater) {
		// Set the pre-signed URL to expire after one hour.
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60 * hoursLater;
        expiration.setTime(expTimeMillis);
        return expiration;
	}
	
	public URL uploadReceipt(String username, long expenseId, MultipartFile file) throws IOException {
		String objectKey = getBucketKey(username, expenseId);
		try {
            Date expires = getExpireDate(1);

            URL url = getPresignedReceiptUrl(objectKey, expires, HttpMethod.PUT);

            // Create the connection and use it to upload the new object using the pre-signed URL.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            OutputStream out = connection.getOutputStream();
            InputStream source = file.getInputStream();
            IOUtils.copy(source, out);
            source.close();
            out.close();

            // Check the HTTP response code. To complete the upload and make the object available, 
            // you must interact with the connection object in some way.
            connection.getResponseCode();
            log.info("HTTP response code: " + connection.getResponseCode());

            // Check to make sure that the object was uploaded successfully.
            S3Object object = s3Client.getObject(BUCKET_NAME, objectKey);
            log.info("Object " + object.getKey() + " created in bucket " + object.getBucketName());
            return url;
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response and 
            log.error(e.getErrorMessage());
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client  
            // couldn't parse the response from Amazon S3.
            log.error(e.getMessage());
        }
		return null;
	}

    public URL getPresignedReceiptUrl(String key, Date expiration, HttpMethod method) throws IOException {
    		// Generate the pre-signed URL.
        log.info("Generating pre-signed URL.");
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, key)
                .withMethod(method)
                .withExpiration(expiration);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        log.info("Presigned URL: " + url.toString());
        return url;
    }

}
