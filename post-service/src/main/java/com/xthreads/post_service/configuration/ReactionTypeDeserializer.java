package com.xthreads.post_service.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.xthreads.post_service.constant.ReactionType;

import java.io.IOException;

public class ReactionTypeDeserializer extends StdDeserializer<ReactionType> {

    public ReactionTypeDeserializer() {
        super(ReactionType.class);
    }

    @Override
    public ReactionType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        return ReactionType.valueOf(value.toUpperCase());
    }
}
