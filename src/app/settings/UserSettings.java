/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.settings;

import app.aws.BaseAmazonS3Client;
import java.io.*;

/**
 *
 * @author Naga Srinivas @Canvass
 */
public class UserSettings implements Serializable {

    public static String FILE_NAME = "usr_stng.ini";
    private String accessKey;
    private String secretKey;
    private RecentValues recentValues;
    private transient BaseAmazonS3Client s3Client;

    public UserSettings(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.init();
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void init() {
        s3Client = BaseAmazonS3Client.getClient(accessKey, secretKey);
    }

    public BaseAmazonS3Client getS3Client() {
        return s3Client;
    }

    public boolean isCredentialsValid() {
        return !(accessKey == null || accessKey.isEmpty() || secretKey == null || secretKey.isEmpty());
    }

    public RecentValues getRecentValues() {
        if (recentValues == null) {
            recentValues = new RecentValues();
        }
        return recentValues;
    }

    public void setRecentValues(RecentValues recentValues) {
        this.recentValues = recentValues;
    }

    public static void save(UserSettings settings) {
        try {
            OutputStream out = new FileOutputStream(FILE_NAME);
            ObjectOutputStream oo = new ObjectOutputStream(out);
            oo.writeObject(settings);
            oo.flush();
            oo.close();
        } catch (Exception e) {
        }
    }

    public static UserSettings load() {
        UserSettings settings = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME));
            settings = (UserSettings) in.readObject();
        } catch (Exception e) {
        }
        return settings;
    }
}
