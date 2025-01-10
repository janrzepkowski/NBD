package mappers;

import com.datastax.oss.driver.api.mapper.annotations.*;
import dao.VehicleDao;

@Mapper
public interface VehicleMapper {
    @DaoFactory
    VehicleDao vehicleDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    VehicleDao vehicleDao();
}