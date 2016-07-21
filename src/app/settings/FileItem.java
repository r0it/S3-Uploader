/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.settings;

import java.io.File;

/**
 *
 * @author Naga Srinivas @Canvass
 */
public class FileItem extends AbstractTrackableItem {

    public FileItem() {
    }

    public FileItem(String name) {
        this.name = name;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}