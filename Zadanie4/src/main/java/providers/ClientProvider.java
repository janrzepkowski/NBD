package providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import models.Client;

public class ClientProvider {
    private final CqlSession session;

    public static final CqlIdentifier CLIENTS = CqlIdentifier.fromCql("clients");
    public static final CqlIdentifier CLIENT_ID = CqlIdentifier.fromCql("client_id");
    public static final CqlIdentifier FIRST_NAME = CqlIdentifier.fromCql("first_name");
    public static final CqlIdentifier LAST_NAME = CqlIdentifier.fromCql("last_name");
    public static final CqlIdentifier PHONE_NUMBER = CqlIdentifier.fromCql("phone_number");
    public static final CqlIdentifier RENTS = CqlIdentifier.fromCql("rents");
    public static final CqlIdentifier ARCHIVED = CqlIdentifier.fromCql("archived");

    public ClientProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public void create(Client client) {
        Insert insertClient = QueryBuilder.insertInto(CLIENTS)
                .value(CLIENT_ID, QueryBuilder.literal(client.getClientId()))
                .value(FIRST_NAME, QueryBuilder.literal(client.getFirstName()))
                .value(LAST_NAME, QueryBuilder.literal(client.getLastName()))
                .value(PHONE_NUMBER, QueryBuilder.literal(client.getPhoneNumber()))
                .value(RENTS, QueryBuilder.literal(client.getRents()))
                .value(ARCHIVED, QueryBuilder.literal(client.isArchived()))
                .ifNotExists();

        session.execute(insertClient.build());
    }

    public Client findById(long clientId) {
        Select selectClient = QueryBuilder.selectFrom(CLIENTS)
                .all()
                .where(Relation.column(CLIENT_ID).isEqualTo(QueryBuilder.literal(clientId)));
        ResultSet resultSet = session.execute(selectClient.build());
        Row row = resultSet.one();

        if (row == null) {
            return null;
        }

        return new Client(
                row.getLong(CLIENT_ID),
                row.getString(FIRST_NAME),
                row.getString(LAST_NAME),
                row.getString(PHONE_NUMBER),
                row.getBoolean(ARCHIVED)
        );
    }

    public void update(Client client) {
        Update updateClient = QueryBuilder.update(CLIENTS)
                .setColumn(FIRST_NAME, QueryBuilder.literal(client.getFirstName()))
                .setColumn(LAST_NAME, QueryBuilder.literal(client.getLastName()))
                .setColumn(PHONE_NUMBER, QueryBuilder.literal(client.getPhoneNumber()))
                .setColumn(RENTS, QueryBuilder.literal(client.getRents()))
                .setColumn(ARCHIVED, QueryBuilder.literal(client.isArchived()))
                .where(Relation.column(CLIENT_ID).isEqualTo(QueryBuilder.literal(client.getClientId())));

        session.execute(updateClient.build());
    }

    public void remove(long clientId) {
        Delete deleteClient = QueryBuilder.deleteFrom(CLIENTS)
                .where(Relation.column(CLIENT_ID).isEqualTo(QueryBuilder.literal(clientId)));

        session.execute(deleteClient.build());
    }
}