package net.wayward_realms.waywardlib.util.file.filter;

import java.io.File;
import java.io.FileFilter;

public class FileExtensionFilter implements FileFilter {

    private String extension;

    public FileExtensionFilter(String extension) {
        this.extension = extension;
    }

    @Override
    public boolean accept(File file) {
        return !file.isDirectory() && file.getName().endsWith("." + extension);
    }

}
