/*
 * Copyright (C) 2019 Tachibana General Laboratories, LLC
 * Copyright (C) 2019 Yaroslav Pronin <proninyaroslav@mail.ru>ru>
 *
 * This file is part of Download Navi.
 *
 * Download Navi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Download Navi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Download Navi.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.kiylx.librarykit.store.system.systemkits;

import android.content.Context;

import androidx.annotation.NonNull;

import com.kiylx.librarykit.store.system.systemcall.SysCallImpl;
import com.kiylx.librarykit.store.system.filekits.FileSystemFacade;
import com.kiylx.librarykit.store.system.filekits.FileSystemFacadeImpl;
import com.kiylx.librarykit.store.system.fsmodule.FsModuleResolverImpl;

/**
 * 用于获得SystemFacadeImpl实例。
 */
public class SystemFacadeHelper
{
    private static SystemFacade systemFacade;
    private static FileSystemFacade fileSystemFacade;

    public synchronized static SystemFacade getSystemFacade(@NonNull Context appContext)
    {
        if (systemFacade == null)
            systemFacade = new SystemFacadeImpl(appContext);

        return systemFacade;
    }

    public synchronized static FileSystemFacade getFileSystemFacade(@NonNull Context appContext)
    {
        if (fileSystemFacade == null)
            fileSystemFacade = new FileSystemFacadeImpl(new SysCallImpl(),
                    new FsModuleResolverImpl(appContext));

        return fileSystemFacade;
    }
}
