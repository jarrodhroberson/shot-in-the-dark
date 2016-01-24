package com.vertigrated.jackson;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.annotation.Nonnull;

import static java.lang.String.format;

public class UnexpectedTokenExpection extends JsonProcessingException
{
    public UnexpectedTokenExpection(@Nonnull final JsonLocation loc, @Nonnull final Throwable rootCause, @Nonnull String format, @Nonnull Object ... args)
    {
        super(format(format,args), loc, rootCause);
    }

    public UnexpectedTokenExpection(@Nonnull final JsonLocation loc, @Nonnull String format, @Nonnull Object ... args)
    {
        super(format(format,args), loc);
    }

    public UnexpectedTokenExpection(@Nonnull final String format, @Nonnull final Object ... args )
    {
        super(format(format,args));
    }
}
