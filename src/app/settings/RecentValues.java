/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.settings;

import java.io.*;

/**
 *
 * @author Naga Srinivas @Canvass
 */
public class RecentValues implements Serializable {

    private String localFolder;
    private String s3Bucket;
    private String s3Folder;

    public String getLocalFolder() {
        return localFolder;
    }

    public void setLocalFolder(String localFolder) {
        this.localFolder = localFolder;
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public void setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String getS3Folder() {
        return s3Folder;
    }

    public void setS3Folder(String s3Folder) {
        this.s3Folder = s3Folder;
    }

    public static void main(String[] args) throws Exception {
        OutputStream out = new FileOutputStream("a.cls");
        ObjectOutputStream oo = new ObjectOutputStream(out);
        RecentValues obj = new RecentValues();
        obj.localFolder = "d:/folder1/folder2/f";
        obj.s3Bucket = "appRes";
        obj.s3Folder = "appFolder";
        oo.writeObject(obj);
        oo.flush();
        oo.close();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream("a.cls"));
        RecentValues obj1 = (RecentValues) in.readObject();
        System.out.println(obj1.localFolder);
        System.out.println(obj1.s3Bucket);
        System.out.println(obj1.s3Folder);
    }
}
