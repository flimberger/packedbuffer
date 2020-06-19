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
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Generic array type for objects of type T.
 *
 * It is basically just a tuple of a {@link ByteBuffer} and {@link Codec}. The heavy lifting is done inside the codec
 * implementation, which is under control of the user.
 *
 * The current design cannot work with variable sized data like {@link String}. If it is necessary, fixed-size buffers
 * should be used instead, or the data needs to be restructured anyway to be really cache-friendly.
 *
 * @param <T> contained type
 */
public final class PackedBuffer<T> implements Iterable<T> {
    private final Codec<T> codec;
    private final ByteBuffer memory;

    private PackedBuffer(Codec<T> codec, ByteBuffer memory) {
        this.codec = codec;
        this.memory = memory;
    }

    public static <V> PackedBuffer<V> allocate(Codec<V> codec, int size) {
        return new PackedBuffer<>(codec, ByteBuffer.allocate(codec.packedSize() * size));
    }

    public static <V> PackedBuffer<V> allocateDirect(Codec<V> codec, int size) {
        return new PackedBuffer<>(codec, ByteBuffer.allocateDirect(codec.packedSize() * size));
    }

    public int position() {
        return memory.position() / codec.packedSize();
    }

    public void position(int index) {
        memory.position(index * codec.packedSize());
    }

    public void flip() {
        memory.flip();
    }

    public int capacity() {
        return memory.capacity() / codec.packedSize();
    }

    public void clear() {
        memory.clear();
    }

    public PackedBuffer<T> duplicate() {
        return new PackedBuffer<>(codec, memory.duplicate());
    }

    public void get(T instance) {
        codec.read(instance, memory);
    }

    public void get(T instance, int index) {
        int oldPosition = memory.position();
        position(index);
        get(instance);
        position(oldPosition);
    }

    public void put(T instance) {
        codec.write(memory, instance);
    }

    public void put(int index, T instance) {
        int oldPosition = memory.position();
        position(index);
        put(instance);
        position(oldPosition);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final T buf = codec.defaultItem();

            @Override
            public boolean hasNext() {
                return memory.position() < memory.limit();
            }

            @Override
            public T next() {
                if (memory.position() >= memory.limit()) {
                    throw new NoSuchElementException();
                }
                get(buf);
                return buf;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (T t : this) {
            action.accept(t);
        }
    }
}
