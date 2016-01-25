package com.vertigrated.fluent;

import javax.annotation.Nonnull;

/**
 * Terminal interface in all proper builder pattern implementations.
 *
 * @param <T> type that is being built.
 */
public interface Build<T>
{
    @Nonnull
    public T build();
}
