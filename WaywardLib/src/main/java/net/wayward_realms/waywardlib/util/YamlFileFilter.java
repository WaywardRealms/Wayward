package net.wayward_realms.waywardlib.util;

import java.io.File;
import java.io.FileFilter;

public class YamlFileFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        return file.getName().endsWith(".yml");
    }
}
