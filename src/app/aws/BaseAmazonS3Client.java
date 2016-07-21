/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.aws;

import static app.aws.BaseAmazonS3Client.MY_UNIQUE_TEST_TAG;
import static app.aws.BaseAmazonS3Client.SINGAPORE_REGION;
import static app.aws.BaseAmazonS3Client.logs;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author Owner
 */
public class BaseAmazonS3Client extends AmazonS3Client {

    public static String USER_NAME = "naga-srinivas";
    public static String SINGAPORE_REGION = "-ap-southeast-1";
    public static String US_REGION = ""; // for US statndard region there is no prepend tag
    public static String ACCESS_KEY = "AKIAJM4X6KSQGUP6NAZA";
    public static String SECRET_KEY = "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";
    /**
     * the following id for localolws s3 buckets *
     */
    //public static String ACCESS_KEY = "AKIAIKAZHK2KKOX2VRFQ";
    //public static String SECRET_KEY = "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";
    public static final String MY_UNIQUE_MAIN_TAG = "canvass-mktg-platform-";
    public static final String MY_UNIQUE_TEST_TAG = "test-canvass-mktg-platform-";
    public static String MY_UNIQUE_TAG = MY_UNIQUE_TEST_TAG; //MY_UNIQUE_TEST_TAG;
    public static String smsFolderName = "sms";
    public static String emailFolderName = "email";
    public static String campaignPagesFolderName = "pages";
    public static String campaignSnapshotsFolderName = "snapshots";
    public static boolean logs = true;

    public static String generateURL(String bucket, String fileName) {
        return generateUrl(SINGAPORE_REGION, bucket, fileName);
    }

    public static String generateUrl(String region, String bucketName, String fileName) {
        String protocol = "https://";
        StringBuilder url = new StringBuilder();
        url.append(protocol).append("s3").append(region).append(".amazonaws.com/").
                append(bucketName).append("/").append(fileName);
        return url.toString();
    }

    // ******************************************************
    // Class Properties
    // ******************************************************
    // making private constructor
    private BaseAmazonS3Client(String key, String secret) {
        super(new BasicAWSCredentials(key, secret));
    }
    private String region = SINGAPORE_REGION;

    public String getDefaultRegion() {
        return region;
    }

    public static BaseAmazonS3Client getClient(String key, String secret) {
        return new BaseAmazonS3Client(key, secret);
    }

    /**
     * This method generates the AWS url for the given file
     *
     * @param bucketName the name of the bucket
     * @param fileName the name of the file
     * @return
     */
    public String generateUrl(String bucketName, String fileName) {
        return generateUrl(region, bucketName, fileName);
    }

    /**
     *
     * @param bucket
     * @param fileName
     * @param pubAccess
     * @return
     */
    public AwsUploadResponse uploadFile(String bucket, String fileName, InputStream input, boolean pubAccess, boolean dir) {
        AwsUploadResponse aur = uploadFile(bucket, fileName, input, pubAccess);
        fileName = fileName.indexOf("/INFO") > 0 ? fileName.substring(0, fileName.indexOf("/INFO")) : fileName;
        String url = aur.getUrl();
        url = url.indexOf("/INFO") > 0 ? url.substring(0, url.indexOf("/INFO")) : url;
        aur.setFileName(fileName);
        aur.setDir(dir);
        return aur;
    }

    public AwsUploadResponse uploadFile(String bucket, String fileName, InputStream input, boolean pubAccess) {
        return uploadFile(bucket, fileName, input, pubAccess, -1);
    }

    public AwsUploadResponse uploadFile(String bucket, String fileName, InputStream input, boolean pubAccess, int expireInMinutes) {
        return uploadFile(SINGAPORE_REGION, bucket, fileName, input, pubAccess, expireInMinutes);
    }

    /**
     *
     * @param region
     * @param bucket
     * @param fileName
     * @param input
     * @param pubAccess
     * @param expireInMinutes
     * @return
     */
    public AwsUploadResponse uploadFile(String region, String bucket, String fileName, InputStream input, boolean pubAccess, int expireInMinutes) {
        ObjectMetadata metaData = new ObjectMetadata(); // creating the empty meta data
        return uploadFile(region, bucket, fileName, input, metaData, pubAccess, expireInMinutes);
    }

    public AwsUploadResponse uploadFile(String region, String bucket, String fileName, InputStream input, ObjectMetadata metaData, boolean pubAccess, int expireInMinutes) {
        try {
            // if the user sets the expiration then apply it
            if (expireInMinutes > 0) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, expireInMinutes);
                System.out.print("This file will expire on :" + cal.getTime());
                metaData.setExpirationTime(cal.getTime());
            }

            PutObjectRequest putObj = new PutObjectRequest(bucket, fileName, input, metaData);
            if (pubAccess) {
                putObj.setCannedAcl(CannedAccessControlList.PublicRead);
            }
            PutObjectResult putRes = putObject(putObj);

            //System.out.print(new Gson().toJson(putRes));

            AwsUploadResponse uploadRes = new AwsUploadResponse();
            uploadRes.setFileName(fileName);
            uploadRes.setUrl(generateUrl(region, bucket, fileName));
            uploadRes.seteTag(putRes.getETag());

            return uploadRes;
        } catch (Exception e) {
            System.out.println("\nError Occured while uploading the file to AWS: " + e);
            e.printStackTrace(System.out);
        }
        return null;
    }

    /**
     * This method destroys the given file under the specified bucket
     *
     * @param bucket
     * @param fileName
     * @return
     */
    public boolean destroyFile(String bucket, String fileName) {
        if (logs) {
            System.out.println("Trying to delete the file: " + fileName + " under the bucket: " + bucket);
        }
        deleteObject(bucket, fileName);
        return true;
    }

    //public specialCopyOfUploadHtmlTemplate(String sourceBucket, String destinationBucket, String source, String destination)
    /**
     * @param bucket The bucket which contents source as well as destination
     * directory/file
     * @param source directory/file as a source
     * @param destination directory/file as a destination
     * @return boolean true when successfully copied, and if it occurred some
     * exception then it returns false
     */
    public boolean doCopyOnSameBucket(String bucket, String source, String destination) {
        return doCopy(bucket, source, bucket, destination);
    }

    /**
     * @param sourceBucket The bucket which contents source directory/file
     * @param destinationBucket The bucket which suppose to contents destination
     * directory/file
     * @param source directory/file as a source
     * @param destination directory/file as a destination
     * @return boolean true when successfully copied, and if it occurred some
     * exception then it returns false
     */
    public boolean doCopy(String sourceBucket, String destinationBucket, String source, String destination) {
        boolean copied = false;
        try {
            // Copying object
            ObjectListing ol = this.listObjects(sourceBucket, source);
            ArrayList<S3ObjectSummary> list = (ArrayList<S3ObjectSummary>) ol.getObjectSummaries();

            for (S3ObjectSummary objSummary : list) {
                String from = objSummary.getKey();
                String to = from.replaceFirst(source, destination);
                CopyObjectRequest copyObjRequest = new CopyObjectRequest(sourceBucket, from, destinationBucket, to);
                copyObjRequest.setCannedAccessControlList(CannedAccessControlList.PublicRead);
                this.copyObject(copyObjRequest);
                System.out.println("Copying object. ['" + from + "' to '" + to + "']");
            }
            copied = true;
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, "
                    + "which means your request made it "
                    + "to Amazon S3, but was rejected with an error "
                    + "response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            copied = false;
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, "
                    + "which means the client encountered "
                    + "an internal error while trying to "
                    + " communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            copied = false;
        } finally {
            return copied;
        }
    }

    /**
     *
     * @param bucket
     * @param fileName
     * @return
     */
    public void makeObjectAsPublic(String bucket, String fileName) {
    }

    /**
     *
     */
    public void createFolder(String bucketName, String folderName) {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(0);
        putObject(bucketName, folderName, in, metaData);
    }

    public void truncateDirectory(String bucket, String folder) {
        try {
            // Copying object
            ObjectListing ol = this.listObjects(bucket, folder);
            ArrayList<S3ObjectSummary> list = (ArrayList<S3ObjectSummary>) ol.getObjectSummaries();
            for (S3ObjectSummary objSummary : list) {
                String key = objSummary.getKey();
                this.destroyFile(bucket, key);
            }
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, "
                    + "which means your request made it "
                    + "to Amazon S3, but was rejected with an error "
                    + "response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, "
                    + "which means the client encountered "
                    + "an internal error while trying to "
                    + " communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        } catch (Exception e) {
            System.out.println("Following Exception occures");
            e.printStackTrace(System.out);
        }
    }

    public static void main(String[] args) throws Exception {
        BaseAmazonS3Client client = BaseAmazonS3Client.getClient(ACCESS_KEY, SECRET_KEY);
    }
}
