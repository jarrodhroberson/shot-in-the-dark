package com.vertigrated.fluent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public interface Composable<T>
{
    @Nonnull
    @SuppressWarnings({"unchecked","varargs"})
    public Composable<T> compose(@Nonnull final Composable<T> ... composables);

    @Nonnull
    public Composable<T> compose(@Nonnull final Set<Composable<T>> composables);

    @Nonnull
    public Composable<T> compose(@Nonnull final List<Composable<T>> composables);
}
