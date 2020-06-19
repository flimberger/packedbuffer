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

import com.purplekraken.packedbuffer.example.AClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class PackedBufferTest {

    @Test
    public void run() {
        AClass aClass = new AClass(23, 42, 3.1418);
        PackedBuffer<AClass> packedBuffer = PackedBuffer.allocate(AClass.CODEC, 1);
        packedBuffer.put(aClass);
        packedBuffer.flip();
        AClass bClass = AClass.CODEC.defaultItem();
        packedBuffer.get(bClass);
        assertThat(bClass).as("The deserialized class is equal to the serialized class").isEqualTo(aClass);
    }
}
