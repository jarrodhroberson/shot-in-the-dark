package com.vertigrated.sitd;

import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.vertigrated.sitd.Coordinate.NATURAL_X;
import static com.vertigrated.sitd.Coordinate.NATURAL_Y;
import static com.vertigrated.sitd.Orientation.HORIZONTAL;
import static com.vertigrated.sitd.Orientation.VERTICAL;
import static java.lang.String.format;

public class Board
{
    private static final Logger L = LoggerFactory.getLogger(Board.class);

    @Nonnull
    public static Board build(@Nonnull final Board b, @Nonnull final List<Target> targets)
    {
        for (final Target t : targets)
        {
            if (!b.place(t))
            {
                throw new IllegalArgumentException(format("%s overlaps with a previously placed target!", t));
            }
        }
        return b;
    }

    @Nonnull
    public static Board build(@Nonnull final Board b, @Nonnull final Integer minTargetSize, @Nonnull final Integer maxTargetSize, @Nonnull Integer targetCount)
    {
        final Random rnd = new Random();
        for (int i = 0; i < targetCount; i++)
        {
            final Integer size = rnd.nextInt(maxTargetSize) + minTargetSize;
            b.place(b.randomlySelectedLocation(size));
        }
        return b;
    }

    public final Integer width;
    public final Integer height;
    private final Random rnd = new Random();
    private final Table<Integer, Integer, Integer> board;

    public Board(@Nonnull final Integer dimension)
    {
        this(dimension, dimension);
    }

    public Board(@Nonnull final Integer width, @Nonnull final Integer height)
    {
        this.width = width;
        this.height = height;
        this.board = ArrayTable.create(ContiguousSet.create(Range.closedOpen(0, width), DiscreteDomain.integers()).asList(), ContiguousSet.create(Range.closedOpen(0, height), DiscreteDomain.integers()).asList());
        for (final Integer r : this.board.rowKeySet())
        {
            for (final Integer c : this.board.columnKeySet())
            {
                this.board.put(r, c, 0);
            }
        }
    }

    @Nonnull
    private Set<Coordinate> findOpenSpaces(@Nonnull final Integer size, @Nonnull final Orientation orientation)
    {
        final ImmutableSortedSet.Builder<Coordinate> ilb = ImmutableSortedSet.orderedBy(NATURAL_X.compound(NATURAL_Y));
        if (HORIZONTAL.equals(orientation))
        {
            for (int r = 0; r < this.height; r++)
            {
                for (int c = 0; c < this.width; c++)
                {
                    final Coordinate start = new Coordinate(r, c);
                    if (this.width - c >= size && !this.coordinatesOverlap(Coordinate.rangeToList(start, new Coordinate(r, c + size - 1))))
                    {
                        ilb.add(start);
                    }
                }
            }
        }
        else
        {
            for (final Integer c : this.board.columnKeySet())
            {
                for (final Integer r : this.board.rowKeySet())
                {
                    final Coordinate start = new Coordinate(r, c);
                    if (this.height - r >= size && !this.coordinatesOverlap(Coordinate.rangeToList(start, new Coordinate(r + size - 1, c))))
                    {
                        ilb.add(start);
                    }
                }
            }
        }
        return ilb.build();
    }

    @Nonnull
    private Target randomlySelectedLocation(@Nonnull final Integer size)
    {
        final Set<Coordinate> coordinates;
        if (this.rnd.nextBoolean())
        {
            coordinates = this.findOpenSpaces(size, HORIZONTAL);
            if (coordinates.isEmpty())
            {
                return this.randomlySelectedLocation(size);
            }
            else
            {
                final Coordinate start = this.pickRandomCoordinate(coordinates);
                final Coordinate end = new Coordinate(start.x, start.y + size);
                return new Target(format("H%02d(%02d:%02d)", size, start.x, start.y), Coordinate.rangeToList(start, end));
            }
        }
        else
        {
            coordinates = this.findOpenSpaces(size, VERTICAL);
            if (coordinates.isEmpty())
            {
                return this.randomlySelectedLocation(size);
            }
            else
            {
                final Coordinate start = this.pickRandomCoordinate(coordinates);
                final Coordinate end = new Coordinate(start.x + size, start.y);
                return new Target(format("V%02d(%02d:%02d)", size, start.x, start.y), Coordinate.rangeToList(start, end));
            }
        }
    }

    public boolean place(@Nonnull final Target t)
    {
        if (targetOverlaps(t)) { return false; }
        else
        {
            for (final Coordinate c : t.coordinates)
            {
                this.board.put(c.x, c.y, 1);
            }
            L.debug("Placed {}", t);
            return true;
        }
    }

    private boolean coordinatesOverlap(@Nonnull final List<Coordinate> coordinates)
    {
        for (final Coordinate c : coordinates)
        {
            if (this.test(c.x, c.y))
            {
                L.warn("{} overlaps with previous target at {}", coordinates, c);
                return true;
            }
        }
        return false;
    }

    private boolean targetOverlaps(final @Nonnull Target t)
    {
        if (this.coordinatesOverlap(t.coordinates))
        {
            L.warn("{} overlaps with previous target", t);
            return true;
        }
        return false;
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

    public boolean test(@Nonnull final Integer row, @Nonnull final Integer column)
    {
        checkElementIndex(row, this.height, "Row");
        checkElementIndex(column, this.width, "Column");
        return !this.board.get(row, column).equals(0);
    }
}
