# PackedBuffer

A simple, data-oriented way to serialize struct-like classes into `ByteBuffer`s.

This is an experiment.
Currently,
there are no performance comparisons.

# Rationale

Java does not have non-reference type "structs",
which is pretty bad for CPU caches.
Also,
complex types are a pain to work with in JNI.
To provide data in a fast, cache-friendly way,
it can be serialized into `ByteBuffer` instances,
which can also be "direct allocated".

## Aims

- Provide a simple, data-oriented way to transfer data.

## Inspiration

- [Unity DOTS](https://unity.com/dots)
- [Data-Oriented Design](https://www.dataorienteddesign.com/dodbook)
  ([Wikipedia](https://en.wikipedia.org/wiki/Data-oriented_design))

# Restrictions

Currently,
there is no way to serialize variable-length data like `String` instances.
If strings are necessary,
they should probably be represented as fixed-size UTF-8 encoded bytes.

# Ideas

- The `Codec` instances could be auto-generated via reflection.
- For use with JNI,
  the data representation is split between C and Java.
  Maybe a simple IDL can generate source files for both languages.
