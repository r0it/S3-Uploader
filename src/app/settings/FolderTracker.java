/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.settings;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author Naga Srinivas @Canvass
 */
public class FolderTracker implements Serializable {

    private String folderPath;
    private HashMap<String, Long> info = new HashMap<String, Long>();
    private transient File folder;
    private transient List<String> modifiedFiles;

    private FolderTracker() {
    }

    private FolderTracker init(String path) throws IllegalAccessException {
        folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalAccessException("Given path is not exist or its not a folder");
        }
        folderPath = folder.getAbsolutePath();
        return this;
    }

    private File getFile() {
        if (folder == null) {
            folder = new File(folderPath);
        }
        return folder;
    }

    private void checkDirectory(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                checkDirectory(file);
            } else {
                //info.put(folderPath, Long.MIN_VALUE);
                String path = file.getAbsolutePath();
                path = path.replace(folderPath, "");
                boolean modified;
                if (info.get(path) == null) {
                    modified = true;
                    // adding the enrty yo info
                    info.put(path, file.lastModified());
                } else {
                    modified = file.lastModified() != info.get(path);
                }
                if (modified) {
                    modifiedFiles.add(path);
                }
            }
        }
    }

    public void check() {
        modifiedFiles = new ArrayList<String>();
        checkDirectory(folder);
    }

    public List<Vector> getAsTableRows() {
        //Object[][] rows;
        List<Vector> rows = new ArrayList<Vector>();
        if (info.isEmpty()) {
            track();
        }
        check(); // first checking the current directory
        Iterator<Entry<String, Long>> iterator = info.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Long> entry = iterator.next();
            Vector row = new Vector();
            String file = entry.getKey();
            row.add(modifiedFiles.indexOf(file) != -1);
            row.add(file.substring(file.lastIndexOf(File.separator) + 1));
            row.add(folderPath + file);
            String type = "-";
            if (file.contains(".")) {
                type = file.substring(file.lastIndexOf(".") + 1);
            }
            row.add(type);
            rows.add(row);
        }
        return rows;
    }

    private void traverseDirectory(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                traverseDirectory(file);
            } else {
                //info.put(folderPath, Long.MIN_VALUE);
                String path = file.getAbsolutePath();
                path = path.replace(folderPath, "");
                info.put(path, file.lastModified());
            }
        }
    }

    public void track() {
        traverseDirectory(folder);
    }

    public static void save(FolderTracker tracker) {
        try {
            String objectName = tracker.folderPath;
            //System.out.println(File.separator);
            objectName = objectName.substring(objectName.lastIndexOf(File.separator) + 1);
            //System.out.println(objectName);
            OutputStream out = new FileOutputStream(objectName);
            ObjectOutputStream oo = new ObjectOutputStream(out);
            oo.writeObject(tracker);
            oo.flush();
            oo.close();
        } catch (Exception e) {
        }
    }

    public static FolderTracker load(String folderPath) throws IllegalAccessException {
        FolderTracker tracker = null;
        try {
            String objectName = folderPath;
            objectName = objectName.substring(objectName.lastIndexOf(File.separator) + 1);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(objectName));
            tracker = (FolderTracker) in.readObject();
            tracker.init(folderPath);
        } catch (Exception e) {
            System.out.println("Unable to load the File:" + e.toString());
            //tracker = new FolderTracker();
            //tracker.init(folderPath);
        }
        return tracker;
    }

    public static FolderTracker getInstance(String path) throws IllegalAccessException {
        return new FolderTracker().init(path);
    }

    public static void main(String[] args) throws Exception {
        String folderPath = "D:\\Projects\\Canvass-E-Comm-test\\web\\src\\main\\webapp\\res";
        //String folderPath = "D:\\Projects\\Canvass-E-Comm-test\\web\\src\\main\\webapp\\res";
        FolderTracker tracker = FolderTracker.load(folderPath);
        ///FolderTracker tracker = FolderTracker.getInstance(folderPath);
        //tracker.track();
        //FolderTracker.save(tracker);
        tracker.check();
        System.out.println(tracker.modifiedFiles);
    }
}
