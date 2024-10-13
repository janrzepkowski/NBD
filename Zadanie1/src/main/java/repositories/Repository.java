package repositories;

import java.util.List;
import java.util.UUID;

public interface Repository<T> {

    T get(UUID id);
    List<T> getAll();
    T add(T t);
    void remove(T t);
    void update(T t);
}