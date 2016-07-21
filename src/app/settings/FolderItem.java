/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.settings;

import java.util.HashMap;

/**
 *
 * @author Naga Srinivas @Canvass
 */
public class FolderItem extends AbstractTrackableItem {

    HashMap<String, TrackableItem> fileItems = new HashMap<String, TrackableItem>();

    public FolderItem() {
    }

    public FolderItem(String name) {
        this.name = name;
    }

    public TrackableItem getItem(String name) {
        return fileItems.get(name);
    }

    public void addItem(TrackableItem item) {
        fileItems.put(item.getName(), item);
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        for (TrackableItem item : fileItems.values()) {
            buff.append("\n");
            if (item instanceof FolderItem) {
                buff.append("\t").append(item.getName()).append(item);
            } else {
                buff.append("-->").append(item.getName());
            }
        }
        return buff.toString();
    }

    public static void main(String[] args) {
        FolderItem fi = new FolderItem();
        fi.addItem(new FileItem("1"));
        FolderItem fi2 = new FolderItem("Folder 2");
        fi2.addItem(new FileItem("2.1"));
        fi2.addItem(new FileItem("2.2"));
        fi2.addItem(new FileItem("2.3"));
        fi.addItem(fi2);
        System.out.println(fi);
    }
}
