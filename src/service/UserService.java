package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.scene.control.Label;
import model.User;
import model.MoodEntry;
import model.Enums.MoodState;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserService {
    private MongoCollection<User> getUsersCollection() {
        return MongoDBService.getDatabase().getCollection("users", User.class);
    }

    public List<String> getRecommendationsByMoodState(String moodState) {
        MongoCollection<Document> collection = MongoDBService.getDatabase().getCollection("recomendations");
        Document query = new Document("moodState", moodState);
        Document result = collection.find(query).first();

        if (result != null) {
            return result.getList("recommendations", String.class);
        }
        return Collections.emptyList();
    }

    public boolean registerUser(String name, String surname, Date birthdate, String email,
                                String avatar, String password, Label... errorLabels) {
        resetErrors(errorLabels);
        boolean isValid = validateRegistration(name, surname, birthdate, email, password, errorLabels);

        if (!isValid) return false;

        try {
            User newUser = new User(
                    name,
                    surname,
                    birthdate,
                    email,
                    avatar,
                    BCrypt.hashpw(password, BCrypt.gensalt())
            );
            newUser.setRecords(new ArrayList<>());

            getUsersCollection().insertOne(newUser);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean validateRegistration(String name, String surname, Date birthdate,
                                         String email, String password, Label[] errorLabels) {
        boolean isValid = true;

        if (name.isEmpty()) {
            errorLabels[0].setText("Nombre requerido");
            isValid = false;
        }

        if (surname.isEmpty()) {
            errorLabels[1].setText("Apellido requerido");
            isValid = false;
        }

        if (birthdate == null) {
            errorLabels[2].setText("Fecha requerida");
            isValid = false;
        }

        if (email.isEmpty() || !email.contains("@")) {
            errorLabels[3].setText("Email inválido");
            isValid = false;
        } else if (getUsersCollection().find(Filters.eq("email", email)).first() != null) {
            errorLabels[3].setText("Email ya registrado");
            isValid = false;
        }

        if (password.length() < 6) {
            errorLabels[4].setText("Contraseña muy corta");
            isValid = false;
        }

        return isValid;
    }

    public User login(String email, String password) {
        User user = getUsersCollection().find(Filters.eq("email", email)).first();
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean addMoodEntry(ObjectId userId, MoodEntry entry) {
        try {
            if (getUsersCollection().find(Filters.eq("_id", userId)).first() == null) {
                System.out.println("Usuario no encontrado.");
                return false;
            }

            getUsersCollection().updateOne(
                    Filters.eq("_id", userId),
                    Updates.push("records", entry)
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void resetErrors(Label... labels) {
        for (Label label : labels) {
            label.setText("");
        }
    }

    public List<MoodEntry> getMoodEntries(ObjectId userId) {
        MongoCollection<User> usersCollection = getUsersCollection();
        User user = usersCollection.find(Filters.eq("_id", userId)).first();
        return user != null ? user.getRecords() : new ArrayList<>();
    }
}