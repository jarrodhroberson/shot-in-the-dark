package com.vertigrated.sitd.jooq;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vertigrated.sitd.representation.Board;
import com.vertigrated.sitd.representation.Player;
import com.vertigrated.sitd.representation.Shot;
import com.vertigrated.sitd.representation.Target;
import org.jooq.*;

import javax.annotation.Nonnull;

import static java.lang.String.format;

public class SitdRecordMapperProvider implements RecordMapperProvider
{
    private Provider<DSLContext> dslContextProvider;

    @Inject
    public SitdRecordMapperProvider(@Nonnull final Provider<DSLContext> dslContextProvider)
    {
        this.dslContextProvider = dslContextProvider;
    }

    @Override
    public <R extends Record, E> RecordMapper<R, E> provide(final RecordType<R> recordType, final Class<? extends E> type)
    {
        if (type == Player.class)
        {
            return (RecordMapper<R, E>) new PlayerRecordMapper();
        }
        else if (type == Target.class)
        {
            return (RecordMapper<R, E>) new TargetRecordMapper();
        }
        else if (type == Shot.class)
        {
            return (RecordMapper<R, E>) new ShotRecordMapper();
        }
        else if (type == Board.class)
        {
            return (RecordMapper<R, E>) new BoardRecordMapper(this.dslContextProvider.get());
        }
        else
        {
            throw new IllegalArgumentException(format("%s is not a recognized type",type.getName()));
        }
    }
}
