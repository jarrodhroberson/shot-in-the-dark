package com.vertigrated.sitd.board;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.vertigrated.fluent.Build;
import com.vertigrated.fluent.Dimension;
import com.vertigrated.fluent.Targets;
import com.vertigrated.pattern.Strategy;
import com.vertigrated.sitd.Coordinate;
import com.vertigrated.sitd.Coordinates;
import com.vertigrated.sitd.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkNotNull;

public class Board
{
    private static final Logger L = LoggerFactory.getLogger(Board.class);

    public final UUID id;
    public final Integer width;
    public final Integer height;
    final Set<Target> targets;

    public Board(@Nonnull final Integer dimension, @Nonnull final Set<Target> targets)
    {
        this(dimension, dimension, targets);
    }

    public Board(@Nonnull final Integer width, @Nonnull final Integer height, @Nonnull final Set<Target> targets)
    {
        this.width = width;
        this.height = height;
        this.targets = targets;
        this.id = UUID.nameUUIDFromBytes(this.toString().getBytes(UTF_8));
    }

    Set<Coordinates> taken()
    {
        return ImmutableSortedSet.copyOf(Iterables.transform(this.targets, new Function<Target, Coordinates>()
        {
            @Nullable @Override public Coordinates apply(@Nullable final Target input)
            {
                return checkNotNull(input).coordinates;
            }
        }));
    }

    final boolean place(@Nonnull final Target target)
    {
        final Coordinate end = target.coordinates.end();
        if (end.x >= this.width || end.y >= this.height) { return false; }
        for (final Target t : this.targets) { if (target.intersects(t)) { return false; } }
        this.targets.add(target);
        //L.debug("Placed {}", target);
        return true;
    }

    @Nullable
    public Target at(@Nonnull final Integer x, @Nonnull final Integer y)
    {
        final Coordinate key = new Coordinate(x, y);
        return Iterables.tryFind(this.targets, new Predicate<Target>()
        {
            @Override public boolean apply(@Nullable final Target target)
            {
                return checkNotNull(target).contains(key);
            }
        }).orNull();
    }

    public boolean test(@Nonnull final Integer x, @Nonnull final Integer y)
    {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) { throw new IllegalArgumentException(); }
        else
        {
            final Coordinate c = new Coordinate(x, y);
            for (final Target t : this.targets)
            {
                if (t.contains(c)) { return true; }
            }
            return false;
        }
    }

    interface BoardBuilder extends Dimension<Targets<Build<com.vertigrated.sitd.board.Board>>> {}

    public static class Builder implements BoardBuilder
    {
        @Override public Targets<Build<Board>> dimension(@Nonnull final Integer width, @Nonnull final Integer height)
        {
            return new Targets<Build<Board>>()
            {
                @Override public Build<Board> targets(@Nonnull final Set<Target> targets)
                {
                    return new Build<Board>()
                    {
                        @Override public Board build()
                        {
                            return new Board(width, height, targets);
                        }
                    };
                }

                @Override public Build<Board> targets(@Nonnull final Strategy<Board, Set<Target>> strategy)
                {
                    return this.targets(strategy.apply(new Board(width, height, Sets.<Target>newTreeSet())));
                }
            };
        }

        @Override public Targets<Build<Board>> dimension(@Nonnull final Integer side)
        {
            return this.dimension(side, side);
        }
    }

}
