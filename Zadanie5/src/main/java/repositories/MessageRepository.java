package repositories;

import org.bson.Document;

public class MessageRepository extends AbstractMongoRepository {

    public MessageRepository() {
        initDbConnection();
    }

    public void saveMessage(String message) {
        Document doc = new Document().append("rent", message);
        getDatabase().getCollection("messages").insertOne(doc);
    }

    public void close() {
        getMongoClient().close();
    }
}