package fr.unilim.saes5.persistence.project;

import com.google.gson.*;
import fr.unilim.saes5.model.Word;
import fr.unilim.saes5.model.context.Context;
import fr.unilim.saes5.model.context.PrimaryContext;
import fr.unilim.saes5.model.context.SecondaryContext;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class ContextDeserializer implements JsonDeserializer<Context> {
    @Override
    public Context deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement wordElement = jsonObject.get("word");
        Word word = context.deserialize(wordElement, Word.class);

        float priority = jsonObject.get("priority").getAsFloat();
        Context word1 = getContext(priority, word);
        if (word1 != null) return word1;

        throw new JsonParseException("Unknown context type");
    }

    @Nullable
    private static Context getContext(float priority, Word word) {
        if (priority == PrimaryContext.PRIMARY_CONTEXT_PRIORITY) {
            return new PrimaryContext(word);
        } else if (priority == SecondaryContext.SECONDARY_CONTEXT_PRIORITY) {
            return new SecondaryContext(word);
        }
        return null;
    }
}
