package org.spoofax.jsglr2.util;

import java.io.File;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;

public class LocalFileObject {

    public static org.apache.commons.vfs2.FileObject get(String path) {
        StandardFileSystemManager fileSystemManager = new StandardFileSystemManager();

        try {
            fileSystemManager.init();
            org.apache.commons.vfs2.FileObject resource = fileSystemManager.resolveFile(new File("."), path);
            fileSystemManager.close();

            return resource;
        } catch(FileSystemException e) {
            e.printStackTrace();

            return null;
        }
    }

}