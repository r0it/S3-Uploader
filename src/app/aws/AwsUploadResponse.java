/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.aws;

import com.google.gson.Gson;

/**
 *
 * @author Owner
 */
public class AwsUploadResponse {

    private String url;
    private String eTag;
    private String fileName;
    private Boolean dir = false;

    public AwsUploadResponse() {
    }

    public AwsUploadResponse(String url, String eTag, String fileName) {
        this.url = url;
        this.eTag = eTag;
        this.fileName = fileName;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getDir() {
        if(dir == null) {
            return false;
        }
        return dir;
    }

    public void setDir(Boolean dir) {
        if(dir == null) {
            this.dir = false;
        } else {
            this.dir = dir;
        }
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static AwsUploadResponse parseJson(String json) {
        AwsUploadResponse res = new AwsUploadResponse();
        try {
            res = new Gson().fromJson(json, AwsUploadResponse.class);
        } catch (Exception e) {
        } finally {
        }
        return res;
    }
}