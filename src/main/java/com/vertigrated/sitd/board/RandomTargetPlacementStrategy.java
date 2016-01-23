package com.vertigrated.sitd.board;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Range;
import com.vertigrated.pattern.Strategy;
import com.vertigrated.sitd.Coordinate;
import com.vertigrated.sitd.Orientation;
import com.vertigrated.sitd.Target;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import static com.vertigrated.sitd.Orientation.HORIZONTAL;
import static com.vertigrated.sitd.Orientation.VERTICAL;
import static java.lang.String.format;

public class RandomTargetPlacementStrategy implements Strategy<Board, Set<Target>>
{
    private final Random rnd;
    private final Integer minTargetSize;
    private final Integer maxTargetSize;
    private final Integer targetCount;

    public RandomTargetPlacementStrategy(@Nonnull final Integer minTargetSize, @Nonnull final Integer maxTargetSize, @Nonnull final Integer targetCount)
    {
        this.rnd = new Random();
        this.minTargetSize = minTargetSize;
        this.maxTargetSize = maxTargetSize;
        this.targetCount = targetCount;
    }

    @Override
    public Set<Target> apply(@Nonnull final Board board)
    {
        final ImmutableSortedSet.Builder<Target> issb = ImmutableSortedSet.naturalOrder();
        for (int i = 0; i < targetCount; i++)
        {
            final Integer size = Math.min(rnd.nextInt(maxTargetSize) + minTargetSize, maxTargetSize);
            while (true)
            {
                final Target t = this.randomlySelectedLocation(board, size);
                if (board.place(t))
                {
                    issb.add(t);
                    break;
                }
            }
        }
        return issb.build();
    }

    @Nonnull
    private Coordinate pickRandomCoordinate(@Nonnull final Set<Coordinate> coordinates)
    {
        final Iterator<Coordinate> it = coordinates.iterator();
        for (int i = 0; i < new Random().nextInt(coordinates.size() - 1); i++)
        {
            it.next();
        }
        return it.next();
    }

    @Nonnull
    private Set<Coordinate> findOpenSpaces(@Nonnull final Board b, @Nonnull final Integer size, @Nonnull final Orientation orientation)
    {
        final ImmutableSet.Builder<Coordinate> ilb = ImmutableSet.builder();
        if (HORIZONTAL.equals(orientation))
        {
            for (int c = 0; c <= b.height; c++)
            {
                for (int r = 0; r < b.width; r++)
                {
                    final Coordinate start = new Coordinate(r, c);
                    final Coordinate end = new Coordinate(r + size - 1, c);
                    final Range<Coordinate> range = Range.closed(start, end);
                    final Set<Range<Coordinate>> taken = b.taken().asRanges();
                    if (size < b.height - r - 1 && taken.isEmpty()) { ilb.add(start); }
                    else
                    {
                        for (final Range<Coordinate> rc : taken)
                        {
                            if (size > b.height - r - 1 || rc.isConnected(range)) { break; }
                            else { ilb.add(start); }
                        }
                    }
                }
            }
        }
        else
        {
            for (int r = 0; r < b.height; r++)
            {
                for (int c = 0; c < b.width; c++)
                {
                    final Coordinate start = new Coordinate(r, c);
                    final Coordinate end = new Coordinate(r, c + size - 1);
                    final Range<Coordinate> range = Range.closed(start, end);
                    final Set<Range<Coordinate>> taken = b.taken().asRanges();
                    if (size < b.width - c - 1 && taken.isEmpty()) { ilb.add(start); }
                    else
                    {
                        for (final Range<Coordinate> rc : taken)
                        {
                            if (size > b.width - c - 1 || rc.isConnected(range)) { break; }
                            else { ilb.add(start); }
                        }
                    }
                }
            }
        }
        return ilb.build();
    }

    @Nonnull
    private Target randomlySelectedLocation(@Nonnull final Board b, @Nonnull final Integer size)
    {
        final Set<Coordinate> coordinates;
        if (this.rnd.nextBoolean())
        {
            coordinates = this.findOpenSpaces(b, size, HORIZONTAL);
            if (coordinates.isEmpty())
            {
                return this.randomlySelectedLocation(b, size);
            }
            else
            {
                final Coordinate start = this.pickRandomCoordinate(coordinates);
                final Coordinate end = new Coordinate(start.x, start.y + size);
                final Range<Coordinate> range = Range.closed(start, end);
                return new Target(format("H%02d(%02d:%02d)", size, start.x, start.y), range);
            }
        }
        else
        {
            coordinates = this.findOpenSpaces(b, size, VERTICAL);
            if (coordinates.isEmpty())
            {
                return this.randomlySelectedLocation(b, size);
            }
            else
            {
                final Coordinate start = this.pickRandomCoordinate(coordinates);
                final Coordinate end = new Coordinate(start.x + size, start.y);
                final Range<Coordinate> range = Range.closed(start, end);
                return new Target(format("V%02d(%02d:%02d)", size, start.x, start.y), range);
            }
        }
    }
}
