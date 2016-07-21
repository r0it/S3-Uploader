/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.aws;

import app.events.FileEvent;
import app.settings.RecentValues;
import app.settings.UserSettings;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import ng.util.Compressor;
import ng.util.MyCalendar;

/**
 *
 * @author Owner
 */
public class S3StaticFilesUploader {

    public static String basePath = "D:\\s3-static-resources\\";
    public static String parentPath = "D:\\s3-static-resources\\";
    private UserSettings settings;
    private BaseAmazonS3Client s3Client;
    private FileEvent eventHandler;
    private Compressor dataMinifier = new Compressor();

    public static enum ContentType {

        JAVA_SCRIPT("application/x-javascript", ".js"),
        CSS("text/css", ".css"),
        HTML("text/html", ".html", ".htm"),
        PNG("image/png", ".png"),
        JPG("image/jpeg", ".jpg", ".jpeg"),
        GIF("image/gif", ".gif"),
        UN_SUPPORTED("text/plain", ""); // it should always be at last

        private ContentType(String type, String... ext) {
            this.type = type;
            this.ext = ext;
        }
        private String type;
        private String ext[];

        public String getType() {
            return type;
        }

        public String[] getExt() {
            return ext;
        }

        public static ContentType getContentType(String fileName) {
            ContentType contentType = null;
            fileName = fileName.toLowerCase();
            for (ContentType ct : values()) {
                if (contentType != null) {
                    break;
                }
                for (String ex : ct.ext) {
                    if (fileName.endsWith(ex)) {
                        contentType = ct;
                        break;
                    }
                }
            }
            return contentType;
        }
    }

    public S3StaticFilesUploader(UserSettings settings) {
        
        this.settings = settings;
        s3Client = settings.getS3Client();
        basePath = settings.getRecentValues().getLocalFolder();
        File f = new File(basePath);
        parentPath = f.getParent();

    }

    public ByteArrayOutputStream compress(String filePath) throws Exception {
        return readFile(filePath, true);
    }

    private ByteArrayOutputStream readFile(String filePath, boolean compress) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        File file = new File(filePath);
        InputStream in;
        //FileInputStream in = new FileInputStream(filePath);
        OutputStream out;
        ContentType contentType = ContentType.getContentType(filePath);
        // minifying the CSS and JS resources
        switch (contentType) {
            case CSS:
            case JAVA_SCRIPT:
            case HTML:
                StringWriter sw;
                StringWriter writer = (StringWriter) dataMinifier.compress(file, contentType);
                in = new ByteArrayInputStream(writer.getBuffer().toString().getBytes());
                break;
            default:
                in = new FileInputStream(file);
        }
        if (compress) {
            out = new GZIPOutputStream(stream);
        } else {
            out = new BufferedOutputStream(stream);
        }
        // readig data as with 1 K.B buffer
        byte buff[] = new byte[1024];
        int buffSize;
        while ((buffSize = in.read(buff)) != -1) {
            out.write(buff, 0, buffSize);
        }
        out.flush();
        out.close();
        in.close();
        return stream;
    }

    public void uploadToS3(String filePath, String s3FileName, boolean compress) throws Exception {
        ByteArrayOutputStream input = readFile(filePath, compress);
        ByteArrayInputStream in = new ByteArrayInputStream(input.toByteArray());
        ObjectMetadata metaData = new ObjectMetadata();

        ContentType contentType = ContentType.getContentType(filePath);

        metaData.setContentType(contentType.getType());
        metaData.setCacheControl("public, max-age=31536000");
        if (compress) {
            metaData.setContentEncoding("gzip");
        }
        String s3rootFolder = settings.getRecentValues().getS3Bucket();
        //System.out.print(s3rootFolder);
        metaData.setHttpExpiresDate(new Date(MyCalendar.getInstance().addDays(365).getTimeStamp()));
        //s3Client.uploadFile(s3Client.getDefaultRegion(), "app-res", s3FileName, in, metaData, true, -1);
        //s3Client.uploadFile(s3Client.getDefaultRegion(), "localowls-test", s3FileName, in, metaData, true, -1);
        s3Client.uploadFile(s3Client.getDefaultRegion(), s3rootFolder, s3FileName, in, metaData, true, -1);
    }

    public void extractDir(File dir) throws Exception {
        List<String> names = new ArrayList<String>();
        for (File file : dir.listFiles()) {
            names.add(file.getAbsolutePath());
        }
        extractDir(names);
//        for (File file : dir.listFiles()) {
//            if (file.isDirectory()) {
//                extractDir(file);
//            } else {                
//            }
//        }
    }

    public void extractDir(List<String> files) throws Exception {
        for (String file : files) {
            {
                String path = file;
                //System.out.println("Upload the file to s3 target path : " + path);
                path = path.replace(parentPath, "");
                path = path.replaceAll("\\\\", "/");
                if (path.startsWith("/")) {
                    path = path.replaceFirst("/", "");
                }
                System.out.println("Upload the file to s3 target path : " + path);
                ContentType contentType = ContentType.getContentType(path);
                boolean compress = false;
                switch (contentType) {
                    case CSS:
                    case HTML:
                    case JAVA_SCRIPT:
                        compress = true;
                        break;
                }
                uploadToS3(file, path, compress);
                if (eventHandler != null) {
                    eventHandler.uploadComplete("Upload the file to s3 target path : " + path);
                }
            }
        }
    }

    public void setEventHandler(FileEvent eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void export() throws Exception {
        File folder = new File(basePath);
        extractDir(folder);
    }

    public void export(List<String> files) throws Exception {
        File folder = new File(basePath);
        extractDir(files);
    }

    public static void main(String[] args) throws Exception {
        UserSettings settings = new UserSettings(BaseAmazonS3Client.ACCESS_KEY, BaseAmazonS3Client.SECRET_KEY);
        settings.setRecentValues(new RecentValues());
        settings.getRecentValues().setLocalFolder("D:\\s3-static-resources");
        settings.getRecentValues().setS3Bucket("fribbon-app");
        S3StaticFilesUploader uploader = new S3StaticFilesUploader(settings);
        uploader.export();
        System.out.println("Export is done");
    }
}