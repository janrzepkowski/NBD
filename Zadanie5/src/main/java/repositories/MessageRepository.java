package repositories;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.MongoCollection;

public class MessageRepository extends AbstractMongoRepository {

    private MongoCollection<Document> collection;

    public MessageRepository() {
        try {
            MessageRepository messageRepository= new MessageRepository();
             this.collection= messageRepository.getDatabase().getCollection("rental");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMessageToRepository(String message) {
        Document jsonDocument = new Document().append("rent",message);
        collection.insertOne(jsonDocument);
    }

}

