package providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import models.Client;

public class ClientProvider {
    private final CqlSession session;
    private final EntityHelper<Client> clientHelper;

    public ClientProvider(MapperContext ctx, EntityHelper<Client> clientHelper) {
        this.session = ctx.getSession();
        this.clientHelper = clientHelper;
    }

    public void create(Client client) {
        session.execute(
                session.prepare(clientHelper.insert().build())
                        .bind()
                        .setLong("client_id", client.getClientId())
                        .setString("first_name", client.getFirstName())
                        .setString("last_name", client.getLastName())
                        .setString("phone_number", client.getPhoneNumber())
                        .setInt("rents", client.getRents())
                        .setBoolean("archived", client.isArchived())
        );
    }

    public Client findById(long clientId) {
        Select selectClient = QueryBuilder.selectFrom(CqlIdentifier.fromCql("clients"))
                .all()
                .where(Relation.column(CqlIdentifier.fromCql("client_id")).isEqualTo(QueryBuilder.literal(clientId)));
        try {
            Row row = session.execute(selectClient.build()).one();
            if (row == null) {
                return null;
            }
            return getClient(row);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private Client getClient(Row row) {
        return new Client(
                row.getLong("client_id"),
                row.getString("first_name"),
                row.getString("last_name"),
                row.getString("phone_number")
        );
    }

    public void update(Client client) {
        try {
            session.execute(
                    session.prepare(clientHelper.updateByPrimaryKey().build())
                            .bind()
                            .setLong("client_id", client.getClientId())
                            .setString("first_name", client.getFirstName())
                            .setString("last_name", client.getLastName())
                            .setString("phone_number", client.getPhoneNumber())
                            .setInt("rents", client.getRents())
                            .setBoolean("archived", client.isArchived())
            );
        } catch (NullPointerException e) {
            System.out.println("Client does not exist");
        }
    }

    public void remove(long clientId) {
        Delete deleteClient = QueryBuilder.deleteFrom(CqlIdentifier.fromCql("clients"))
                .where(Relation.column(CqlIdentifier.fromCql("client_id")).isEqualTo(QueryBuilder.literal(clientId)));
        session.execute(deleteClient.build());
    }
}
