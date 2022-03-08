package com.kiylx.downloader.core.store.system;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.kiylx.downloader.core.store.system.filekits.SafFileSystem;

public class Utils {
    public static boolean isSafPath(@NonNull Context appContext, @NonNull Uri path) {
        return SafFileSystem.getInstance(appContext).isSafPath(path);
    }

    public static boolean isFileSystemPath(@NonNull Uri path) {
        String scheme = path.getScheme();
        if (scheme == null)
            throw new IllegalArgumentException("Scheme of " + path.getPath() + " is null");

        return scheme.equals(ContentResolver.SCHEME_FILE);
    }
}
