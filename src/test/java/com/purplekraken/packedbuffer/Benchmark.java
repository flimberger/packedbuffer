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

import java.util.Arrays;
import java.util.Random;

public class Benchmark {
    private static PackedBuffer<AClass> createBuffer(int nItems) {
        PackedBuffer<AClass> packedBuffer = PackedBuffer.allocate(AClass.CODEC, nItems);
        Random rand = new Random();
        AClass instance = AClass.CODEC.defaultItem();
        for (int i = 0; i < nItems; i++) {
            instance.val1 = rand.nextInt();
            instance.val2 = rand.nextLong();
            instance.val3 = rand.nextDouble();
            packedBuffer.put(instance);
        }
        packedBuffer.flip();

        return packedBuffer;
    }

    private static long runBench(int nItems) {
        PackedBuffer<AClass> packedBuffer = createBuffer(nItems);
        long sums[] = new long[nItems];
        AClass instance = AClass.CODEC.defaultItem();
        long start = System.currentTimeMillis();
        for (int i = 0; i < nItems; i++) {
            packedBuffer.get(instance);
            sums[i] = instance.val1 + instance.val2;
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    @Test
    public void run() {
        int nItems = 1024 * 1024 * 64;
        int nRounds = 128;
        int itemSize = AClass.CODEC.packedSize();
        int sum = nItems * itemSize;
        System.out.println("mem: " + nItems + " * " + itemSize + " = " + sum + " (" + ((double)sum / (1024*1024*1024)) + " GB)" );
        long[] durations = new long[nRounds];
        for (int i = 0; i < nRounds; i++) {
            durations[i] = runBench(nItems);
            System.out.print('.');
            System.out.flush();
        }
        System.out.println();
        double avg = (double)Arrays.stream(durations).sum() / nRounds;
        System.out.println("" + nRounds + " rounds with " + nItems + " items each: " + avg + " ms");
    }
}
