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

package com.purplekraken.packedbuffer.example;

import com.purplekraken.packedbuffer.Codec;

import java.nio.ByteBuffer;
import java.util.Objects;

public class AClass {
    public int val1;
    public long val2;
    public double val3;

    public AClass() {
        this(0, 0, 0);
    }

    public AClass(int v1, long v2, double v3) {
        val1 = v1;
        val2 = v2;
        val3 = v3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AClass aClass = (AClass) o;
        return val1 == aClass.val1 &&
                val2 == aClass.val2 &&
                Double.compare(aClass.val3, val3) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(val1, val2, val3);
    }

    static public Codec<AClass> CODEC = new Codec<AClass>() {
        @Override
        public AClass defaultItem() {
            return new AClass();
        }

        @Override
        public int packedSize() {
            return Integer.BYTES + Long.BYTES + Double.BYTES;
        }

        @Override
        public void write(ByteBuffer buf, AClass instance) {
            buf.putInt(instance.val1);
            buf.putLong(instance.val2);
            buf.putDouble(instance.val3);
        }

        @Override
        public void read(AClass instance, ByteBuffer buf) {
            instance.val1 = buf.getInt();
            instance.val2 = buf.getLong();
            instance.val3 = buf.getDouble();
        }
    };
}
