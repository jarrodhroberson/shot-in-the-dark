package com.vertigrated.fluent;

import com.vertigrated.sitd.Target;

import javax.annotation.Nonnull;

public interface Place<T>
{
    public T place(@Nonnull Target target);
}
