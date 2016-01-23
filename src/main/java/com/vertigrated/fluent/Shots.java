package com.vertigrated.fluent;

import com.vertigrated.sitd.Shot;

import javax.annotation.Nonnull;
import java.util.List;

public interface Shots<T>
{
    public T shots(@Nonnull final List<Shot> shots);
}
