/*
 * Copyright (C) 2019 Tachibana General Laboratories, LLC
 * Copyright (C) 2019 Yaroslav Pronin <proninyaroslav@mail.ru>
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

package com.kiylx.librarykit.store.system.systemcall;

import androidx.annotation.NonNull;

import java.io.FileDescriptor;
import java.io.IOException;

/*
 * A platform dependent interface for system calls.
 */

public interface SysCall
{
    void lseek(@NonNull FileDescriptor fd, long offset) throws IOException, UnsupportedOperationException;

    void fallocate(@NonNull FileDescriptor fd, long length) throws IOException;

    long availableBytes(@NonNull FileDescriptor fd) throws IOException;
}
