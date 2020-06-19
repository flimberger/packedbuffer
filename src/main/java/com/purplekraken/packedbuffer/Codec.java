/*
 * SPDX-License-Identifier: ISC
 *
 * Copyright (c) 2020 Florian Limberger <flo@purplekraken.com>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 */

package com.purplekraken.packedbuffer;

import java.nio.ByteBuffer;

/**
 * Interface to serialize a class consisting only of primitive members.
 *
 * Currently, variable-sized member types are <emph>not</emph> supported. It is strongly advised to restructure the
 * classes to avoid such data.
 *
 * @param <T> the actual class
 */
public interface Codec<T> {
    /**
     * Return a default-constructed instance of {@code T}.
     *
     * @return a default-constructed instance of {@code T}
     */
    T defaultItem();

    /**
     * Return the byte-size of a single packed instance.
     *
     * @return size of a packed element in bytes
     */
    int packedSize();

    /**
     * Write an instance into a {@link ByteBuffer}.
     *
     * @param instance the instance to write
     * @param buf the buffer to write into
     */
    void write(ByteBuffer buf, T instance);

    /**
     * Read an instance from a {@link ByteBuffer}
     *
     * @param instance the instance to read into
     * @param buf the buffer to read from
     */
    void read(T instance, ByteBuffer buf);
}
