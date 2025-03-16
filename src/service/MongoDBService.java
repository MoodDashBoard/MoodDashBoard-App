package service;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import model.Enums.MoodState;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import env.EnvLoader;
import java.util.Map;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public class MongoDBService {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            Map<String, String> env = EnvLoader.loadEnv("src/env/.env");

            String connectionString = env.get("MONGO_CONNECTION_STRING");

            CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromCodecs(new MoodStateCodec()),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionString))
                    .codecRegistry(codecRegistry)
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase("mooddashboard");
        }
        return database;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    static class MoodStateCodec implements Codec<MoodState> {
        @Override
        public void encode(BsonWriter writer, MoodState value, EncoderContext encoderContext) {
            writer.writeInt32(value.getValue());
        }

        @Override
        public MoodState decode(BsonReader reader, DecoderContext decoderContext) {
            int value = reader.readInt32();
            for (MoodState mood : MoodState.values()) {
                if (mood.getValue() == value) {
                    return mood;
                }
            }
            throw new IllegalArgumentException("Invalid MoodState value: " + value);
        }

        @Override
        public Class<MoodState> getEncoderClass() {
            return MoodState.class;
        }
    }
}