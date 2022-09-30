package com.maths.beyond_school_280720220930.utils;

public interface TypeConverter<T,V> {
    T  mapTo(V v);
    V  mapFrom(T t);
}
