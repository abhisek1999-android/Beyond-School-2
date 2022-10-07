package com.maths.beyond_school_new_071020221400.utils;

public interface TypeConverter<T,V> {
    T  mapTo(V v);
    V  mapFrom(T t);
}
