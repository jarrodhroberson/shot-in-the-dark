package com.vertigrated.sitd;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.vertigrated.fluent.Build;
import com.vertigrated.fluent.On;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Date;

@JsonSerialize(using = Shot.Serializer.class)
@JsonDeserialize(using = Shot.Deserializer.class)
public class Shot
{
    static class Serializer extends JsonSerializer<Shot>
    {
        @Override public void serialize(final Shot value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException, JsonProcessingException
        {
            gen.writeStartObject();
            gen.writeObjectField("coordinate", value.coordinate);
            gen.writeObjectField("placed", value.placed);
            gen.writeEndObject();
        }
    }

    static class Deserializer extends JsonDeserializer<Shot>
    {
        @Override public Shot deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException
        {
            final JsonNode n = p.readValueAsTree();
            final ObjectCodec codec = p.getCodec();
            final Coordinate c = codec.treeToValue(n.get("coordinates"), Coordinate.class);
            final Date d = codec.treeToValue(n.get("placed"), Date.class);
            return new Shot.Builder().coordinate(c).on(d).build();
        }
    }

    public final Coordinate coordinate;
    public final Date placed;

    private Shot(@Nonnull final Integer x, @Nonnull final Integer y, @Nonnull final Date placed)
    {
        this(new Coordinate(x,y), placed);
    }
    private Shot(@Nonnull final Coordinate coordinate, @Nonnull final Date placed)
    {
        this.coordinate = coordinate;
        this.placed = placed;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Shot shot = (Shot) o;
        return Objects.equal(coordinate, shot.coordinate) &&
               Objects.equal(placed, shot.placed);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(coordinate, placed);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("coordinate", coordinate)
                          .add("placed", placed)
                          .toString();
    }

    interface ShotBuilder extends com.vertigrated.fluent.Coordinate<On<Build<Shot>,Date>,Integer> {}

    public static class Builder implements ShotBuilder
    {
        @Override public On<Build<Shot>, Date> coordinate(@Nonnull final Coordinate coordinate)
        {
            return new On<Build<Shot>, Date>()
            {
                @Override public Build<Shot> on(@Nonnull final Date on)
                {
                    return new Build<Shot>()
                    {
                        @Override public Shot build()
                        {
                            return new Shot(coordinate, on);
                        }
                    };
                }
            };
        }

        @Override public On<Build<Shot>, Date> coordinate(@Nonnull final Integer x, @Nonnull final Integer y)
        {
            return this.coordinate(new Coordinate(x,y));
        }
    }
}
