package mappers;

import com.datastax.oss.driver.api.mapper.annotations.*;
import dao.ClientDao;

@Mapper
public interface ClientMapper {
    @DaoFactory
    ClientDao clientDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    ClientDao clientDao();
}