package com.thecowking.wrought.util;

import javax.annotation.Nullable;
import java.util.function.Supplier;

// Source - https://github.com/Tslat/Advent-Of-Ascension/blob/1.15.2/source/library/misc/MutableSupplier.java
// Makes way more sense then what i was trying to do with fluids


public class MutableSupplier<T> implements Supplier<T> {
    private Supplier<T> supplier;

    public MutableSupplier (@Nullable Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public void update(@Nullable Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Nullable
    @Override
    public T get() {
        return supplier.get();
    }
}