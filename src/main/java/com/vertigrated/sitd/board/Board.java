package com.vertigrated.sitd.board;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.RangeSet;
import com.google.common.collect.Sets;
import com.vertigrated.fluent.Build;
import com.vertigrated.fluent.Dimension;
import com.vertigrated.fluent.Targets;
import com.vertigrated.pattern.Strategy;
import com.vertigrated.sitd.Coordinate;
import com.vertigrated.sitd.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkElementIndex;

public class Board
{
    private static final Logger L = LoggerFactory.getLogger(Board.class);

    public final UUID id;
    public final Integer width;
    public final Integer height;
    private final Random rnd = new Random();
    private final Set<Target> targets;
    private final Map<Coordinate, Target> targetByCoordinate;

    public Board(@Nonnull final Integer dimension, @Nonnull final Set<Target> targets)
    {
        this(dimension, dimension, targets);
    }

    public Board(@Nonnull final Integer width, @Nonnull final Integer height, @Nonnull final Set<Target> targets)
    {
        this.width = width;
        this.height = height;
        this.targets = targets;
        final ImmutableMap.Builder<Coordinate, Target> imb = ImmutableMap.builder();
        for (final Target t : targets)
        {
            final Set<Coordinate> coordinates = Coordinate.coordinates(t.coordinates);
            for (final Coordinate c : coordinates)
            {
                imb.put(c, t);
            }
        }
        this.targetByCoordinate = imb.build();
//        this.board = Iterables.mergeSorted(Iterables.transform(this.targets, new Function<Target, Iterable<Coordinate>>()
//        {
//            @Nullable @Override public Iterable<Coordinate> apply(@Nullable final Target input)
//            {
//                return new Iterable<Coordinate>()
//                {
//                    final Target target = checkNotNull(input);
//
//                    @Override public Iterator<Coordinate> iterator()
//                    {
//                        final DiscreteDomain<Coordinate> dd = target.orientation.equals(Orientation.HORIZONTAL) ? new Coordinate.HorizontalDiscreteDomain(target.coordinates) : new Coordinate.VerticalDiscreteDomain(target.coordinates);
//                        return new Iterator<Coordinate>()
//                        {
//                            private Coordinate current = dd.minValue();
//
//                            @Override public boolean hasNext() { return dd.next(current).equals(dd.maxValue()); }
//
//                            @Override public Coordinate next()
//                            {
//                                if (!hasNext()) { return null; }
//                                else { current = dd.next(current); }
//                                return current;
//                            }
//                        };
//                    }
//                };
//            }
//        }), Coordinate.NATURAL);
        final Iterator<Target> ti = targets.iterator();
        for (int i = 0; ti.hasNext(); i++)
        {
            this.place(ti.next());
        }
        this.id = UUID.nameUUIDFromBytes(this.toString().getBytes(UTF_8));
    }

    final RangeSet<Coordinate> taken()
    {
        final ImmutableRangeSet.Builder<Coordinate> irsb = ImmutableRangeSet.builder();
        for (final Target t : this.targets) { irsb.add(t.coordinates); }
        return irsb.build();
    }

    final boolean place(@Nonnull final Target target)
    {
        for (final Target t : this.targets)
        {
            if (target.coordinates.isConnected(t.coordinates)) { return false; }
        }
        this.targets.add(target);
        L.debug("Placed {}", target);
        return true;
    }

    public Target at(@Nonnull final Integer row, @Nonnull final Integer column)
    {
        final Coordinate key = new Coordinate(row, column);
        return this.targetByCoordinate.get(key);
    }

    public boolean test(@Nonnull final Integer row, @Nonnull final Integer column)
    {
        checkElementIndex(row, this.height, "Row");
        checkElementIndex(column, this.width, "Column");
        return this.targetByCoordinate.keySet().contains(new Coordinate(row, column));
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
