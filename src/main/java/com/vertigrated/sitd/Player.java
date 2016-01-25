package com.vertigrated.sitd;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.vertigrated.fluent.Build;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.UUID;

@JsonSerialize(using = Player.Serializer.class)
@JsonDeserialize(using = Player.Deserializer.class)
public class Player
{
    public final UUID id;

    private Player(@Nonnull final UUID id)
    {
        this.id = id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
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

    interface PlayerBuilder extends com.vertigrated.fluent.Player<Build<Player>, UUID> {}

    public static class Builder implements PlayerBuilder
    {
        @Nonnull
        @Override
        public Build<Player> player(@Nonnull final UUID player)
        {
            return new Build<Player>()
            {
                @Override
                public Player build()
                {
                    return new Player(player);
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
            gen.writeEndObject();
        }
    }

    static class Deserializer extends JsonDeserializer<Player>
    {
        @Nonnull
        @Override
        public Player deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException
        {
            final JsonNode n = p.readValueAsTree();
            return new Player.Builder().player(p.getCodec().treeToValue(n.get("id"), UUID.class)).build();
        }
    }
}
