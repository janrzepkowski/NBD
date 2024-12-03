package repositories;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public abstract class AbstractRedisRepository {
    protected static JedisPooled pool;

    public void initDbConnection() {
        Properties properties = loadRedisProperties();
        String host = properties.getProperty("redis.host", "localhost");
        int port = Integer.parseInt(properties.getProperty("redis.port", "6379"));
        JedisClientConfig jedisClientConfig = DefaultJedisClientConfig.builder().build();

        pool = new JedisPooled(new HostAndPort(host, port), jedisClientConfig);
    }

    private Properties loadRedisProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("redis.properties")) {
            if (input == null) {
                throw new FileNotFoundException("redis.properties not found");
            }
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
}