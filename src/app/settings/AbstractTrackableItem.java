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
public abstract class AbstractTrackableItem implements TrackableItem {

    protected String name;
    protected String path;
    protected long lastModified;
    private transient boolean modified = false;
    private transient boolean checked = false;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean isModified() {
        if (!checked) {
            File f = new File(path);
            modified = f.lastModified() != lastModified;
            checked = true;
        }
        return modified;
    }
}
