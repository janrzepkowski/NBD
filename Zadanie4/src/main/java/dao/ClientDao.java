package dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import models.Client;
import providers.ClientProvider;

@Dao
public interface ClientDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientProvider.class)
    void create(Client client);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = ClientProvider.class)
    Client findById(long clientId);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientProvider.class)
    void update(Client client);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientProvider.class)
    void remove(long clientId);
}