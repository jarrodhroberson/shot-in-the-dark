package com.vertigrated.sitd.board;

import com.google.common.collect.Iterables;
import com.vertigrated.pattern.Strategy;
import com.vertigrated.sitd.Coordinate;
import com.vertigrated.sitd.Coordinates;
import com.vertigrated.sitd.Orientation;
import com.vertigrated.sitd.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.vertigrated.sitd.Orientation.HORIZONTAL;
import static java.lang.String.format;

public class RandomTargetPlacementStrategy implements Strategy<Board, Set<Target>>
{
    private static final Logger L = LoggerFactory.getLogger(RandomTargetPlacementStrategy.class);

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
        final Set<Coordinate> openSpaces = this.allCoordinates(board);
        for (int i = 0; i < targetCount; i++)
        {
            final Integer size = Math.min(rnd.nextInt(maxTargetSize) + minTargetSize, maxTargetSize);
            while (true)
            {
                final Orientation o = Orientation.values()[this.rnd.nextInt(2)];
                final Target t = this.randomlySelectedLocation(board, size, i, o, openSpaces);
                if (board.place(t))
                {
                    openSpaces.removeAll(t.coordinates.asSet());
                    break;
                }
            }
        }
        return board.targets;
    }

    @Nonnull
    private Coordinate pickRandomStartCoordinate(@Nonnull final Set<Coordinate> coordinates)
    {
        return Iterables.get(coordinates, this.rnd.nextInt(coordinates.size() - 1));
    }

    private Set<Coordinate> allCoordinates(@Nonnull final Board b)
    {
        final Set<Coordinate> allCoordinates = new HashSet<>(b.width*b.height);
        for (int y=0; y < b.height; y++)
        {
            for (int x = 0; x < b.width; x++)
            {
                allCoordinates.add(new Coordinate(x,y));
            }
        }
        return allCoordinates;
    }

    @Nonnull
    private Target randomlySelectedLocation(@Nonnull final Board b, @Nonnull final Integer size, @Nonnull final Integer ordinal, @Nonnull final Orientation orientation, @Nonnull Set<Coordinate> openspaces)
    {
        final Coordinate start = this.pickRandomStartCoordinate(openspaces);
        final Coordinate end;
        if (HORIZONTAL.equals(orientation))
        {

            end = new Coordinate(start.x + size - 1, start.y);
        }
        else
        {
            assert start.y + size - 1 < b.height;
            end = new Coordinate(start.x, start.y + size - 1);
        }
        final Coordinates coordinates = new Coordinates(start, end);
        return new Target(format("%d", ordinal), coordinates);
    }

}
