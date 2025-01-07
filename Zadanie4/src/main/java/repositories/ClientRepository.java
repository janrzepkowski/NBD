package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import dao.ClientDao;
import mappers.ClientMapper;
import mappers.ClientMapperBuilder;
import models.Client;

public class ClientRepository {

    private final CqlSession session;
    private final ClientMapper clientMapper;
    private final ClientDao clientDao;

    public ClientRepository(CqlSession session) {
        this.session = session;
        makeTable();
        this.clientMapper = new ClientMapperBuilder(session).build();
        this.clientDao = clientMapper.clientDao();
    }

    public void makeTable() {
        SimpleStatement createClients =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("clients"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("client_id"), DataTypes.BIGINT)
                        .withColumn("first_name", DataTypes.TEXT)
                        .withColumn("last_name", DataTypes.TEXT)
                        .withColumn("phone_number", DataTypes.TEXT)
                        .withColumn("rents", DataTypes.INT)
                        .build();
        session.execute(createClients);
    }

    public void create(Client client) {
        clientDao.create(client);
    }

    public Client read(long clientId) {
        return clientDao.findById(clientId);
    }

    public void update(Client client) {
        clientDao.update(client);
    }

    public void delete(long clientId) {
        clientDao.remove(clientId);
    }
}