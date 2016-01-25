package com.vertigrated.sitd;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.vertigrated.converter.JsonNodeToObject;
import com.vertigrated.fluent.Build;
import com.vertigrated.fluent.Id;
import com.vertigrated.fluent.Player;
import com.vertigrated.fluent.Shots;
import com.vertigrated.sitd.board.Board;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

@JsonSerialize(using = Game.Serializer.class)
@JsonDeserialize(using = Game.Deserializer.class)
public class Game
{
    public final UUID id;
    public final UUID player;
    public final Board board;
    public final List<Shot> shots;

    Game(@Nonnull final UUID id, @Nonnull final UUID player, @Nonnull final Board board, @Nonnull final List<Shot> shots)
    {
        this.id = id;
        this.player = player;
        this.board = board;
        this.shots = shots;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(player);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Game game = (Game) o;
        return Objects.equal(player, game.player) &&
               Objects.equal(board, game.board) &&
               Objects.equal(shots, game.shots);
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
            gen.writeArrayFieldStart("shots");
            for (final Shot s : value.shots)
            {
                gen.writeObject(s);
            }
            gen.writeEndArray();
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
                                     .board(this.boardConverter.convert(n.get("board")))
                                     .shots(new Function<JsonNode, List<Shot>>()
                                     {
                                         @Nonnull
                                         @Override
                                         public List<Shot> apply(@Nullable final JsonNode input)
                                         {
                                             final ImmutableList.Builder<Shot> ilb = ImmutableList.builder();
                                             if (checkNotNull(input).isArray())
                                             {
                                                 for (final JsonNode jn : input)
                                                 {
                                                     try { ilb.add(p.getCodec().treeToValue(jn, Shot.class)); }
                                                     catch (JsonProcessingException e) { throw new RuntimeException(e); }

                                                 }
                                             }
                                             return ilb.build();
                                         }
                                     }.apply(n.get("shots")))
                                     .build();
        }
    }

    public static class Builder implements Id<Player<com.vertigrated.fluent.Board<Shots<Build<Game>>>, UUID>, UUID>
    {
        @Nonnull
        @Override
        public Player<com.vertigrated.fluent.Board<Shots<Build<Game>>>, UUID> id(@Nonnull final UUID id)
        {
            return new Player<com.vertigrated.fluent.Board<Shots<Build<Game>>>, UUID>()
            {
                @Nonnull
                @Override
                public com.vertigrated.fluent.Board<Shots<Build<Game>>> player(@Nonnull final UUID player)
                {
                    return new com.vertigrated.fluent.Board<Shots<Build<Game>>>()
                    {
                        @Nonnull
                        @Override
                        public Shots<Build<Game>> board(@Nonnull final Board board)
                        {
                            return new Shots<Build<Game>>()
                            {
                                @Nonnull
                                @Override
                                public Build<Game> shots(@Nonnull final Shot... shots)
                                {
                                    return this.shots(Arrays.asList(shots));
                                }

                                @Nonnull
                                @Override
                                public Build<Game> shots(@Nonnull final List<Shot> shots)
                                {
                                    return new Build<Game>()
                                    {
                                        @Nonnull
                                        @Override
                                        public Game build()
                                        {
                                            return new Game(id, player, board, shots);
                                        }
                                    };
                                }

                                @Nonnull
                                @Override
                                public Build<Game> shots(@Nonnull final Set<Shot> shots)
                                {
                                    return this.shots(ImmutableList.copyOf(shots));
                                }
                            };
                        }
                    };
                }
            };
        }
    }
}
