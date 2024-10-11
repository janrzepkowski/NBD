package repositories;

import models.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T> {

    Optional<T> get(UUID id);
    List<T> getAll();
    T add(T t);
    boolean remove(T t);
    T update(T t);
}