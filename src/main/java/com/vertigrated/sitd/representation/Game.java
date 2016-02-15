package com.vertigrated.sitd.representation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.vertigrated.converter.JsonNodeToObject;
import com.vertigrated.fluent.Build;
import com.vertigrated.fluent.Id;
import com.vertigrated.fluent.Player;

@JsonSerialize(using = Game.Serializer.class)
@JsonDeserialize(using = Game.Deserializer.class)
public class Game
{
    public final UUID id;
    public final UUID player;
    public final UUID board;

    Game(@Nonnull final UUID id, @Nonnull final UUID player, @Nonnull final UUID board)
    {
        this.id = id;
        this.player = player;
        this.board = board;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Game game = (Game) o;
        return Objects.equal(id, game.id) &&
                Objects.equal(player, game.player) &&
                Objects.equal(board, game.board);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id, player, board);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("id", id)
                          .add("player", player)
                          .add("board", board)
                          .toString();
    }

    public static class Serializer extends JsonSerializer<Game>
    {
        @Override
        public void serialize(final Game value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException, JsonProcessingException
        {
            gen.writeStartObject();
            gen.writeObjectField("id", value.id);
            gen.writeObjectField("player", value.player);
            gen.writeObjectField("board", value.board);
            gen.writeEndObject();
        }
    }

    public static class Deserializer extends JsonDeserializer<Game>
    {
        private final JsonNodeToObject<UUID> uuidConverter;
        private final JsonNodeToObject<Board> boardConverter;

        @Inject
        Deserializer(@Nonnull final JsonNodeToObject<UUID> uuidConverter, @Nonnull JsonNodeToObject<Board> boardConverter)
        {
            this.uuidConverter = uuidConverter;
            this.boardConverter = boardConverter;
        }

        @Nonnull
        @Override
        public Game deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException
        {
            final JsonNode n = p.readValueAsTree();
            return new Game.Builder().id(this.uuidConverter.convert(n.get("id")))
                                     .player(this.uuidConverter.convert(n.get("player")))
                                     .board(this.uuidConverter.convert(n.get("board")))
                                     .build();
        }
    }

    public static class Builder implements Id<Player<com.vertigrated.fluent.Board<Build<Game>>, UUID>, UUID>
    {
        @Nonnull
        @Override
        public Player<com.vertigrated.fluent.Board<Build<Game>>, UUID> id(@Nonnull final UUID id)
        {
            return new Player<com.vertigrated.fluent.Board<Build<Game>>, UUID>()
            {
                @Nonnull
                @Override
                public com.vertigrated.fluent.Board<Build<Game>> player(@Nonnull final UUID player)
                {
                    return new com.vertigrated.fluent.Board<Build<Game>>()
                    {
                        @Nonnull
                        @Override
                        public Build<Game> board(@Nonnull final UUID board)
                        {
                            return new Game.Builder().id(id).player(player).board(board);
                        }
                    };
                }
            };
        }
    }
}
