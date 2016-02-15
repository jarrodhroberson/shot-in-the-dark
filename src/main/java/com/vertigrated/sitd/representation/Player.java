package com.vertigrated.sitd.representation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.vertigrated.fluent.Build;
import com.vertigrated.fluent.Name;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.UUID;

@JsonSerialize(using = Player.Serializer.class)
@JsonDeserialize(using = Player.Deserializer.class)
public class Player implements java.security.Principal
{
    public final UUID id;
    public final String name;

    private Player(@Nonnull final UUID id, @Nonnull String name)
    {
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Player player = (Player) o;
        return Objects.equal(id, player.id);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("id", id)
                          .toString();
    }


    public static class Builder implements com.vertigrated.fluent.Player<Name<Build<Player>>,UUID>
    {
        @Nonnull
        @Override
        public Name<Build<Player>> player(@Nonnull final UUID uuid)
        {
            return new Name<Build<Player>>() {
                @Nonnull
                @Override
                public Build<Player> name(@Nonnull final String name)
                {
                    return new Build<Player>() {
                        @Nonnull
                        @Override
                        public Player build()
                        {
                            return new Player(uuid,name);
                        }
                    };
                }
            };
        }
    }

    static class Serializer extends JsonSerializer<Player>
    {
        @Override
        public void serialize(final Player value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException, JsonProcessingException
        {
            gen.writeStartObject();
            gen.writeObjectField("id", value.id);
            gen.writeStringField("name", value.name);
            gen.writeEndObject();
        }
    }

    static class Deserializer extends JsonDeserializer<Player>
    {
        @Nonnull
        @Override
        public Player deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException
        {
            return null;
        }
    }
}
