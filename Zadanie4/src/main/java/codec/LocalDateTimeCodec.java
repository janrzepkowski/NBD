package codec;

import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeCodec implements TypeCodec<LocalDateTime> {

    private final TypeCodec<Instant> instantCodec = TypeCodecs.TIMESTAMP;

    @NonNull
    @Override
    public GenericType<LocalDateTime> getJavaType() {
        return GenericType.of(LocalDateTime.class);
    }

    @NonNull
    @Override
    public DataType getCqlType() {
        return instantCodec.getCqlType();
    }

    @Nullable
    @Override
    public ByteBuffer encode(@Nullable LocalDateTime localDateTime, @NonNull ProtocolVersion protocolVersion) {
        if (localDateTime == null) {
            return null;
        }
        Instant instant = localDateTime.atZone(ZoneId.of("UTC")).toInstant();
        return instantCodec.encode(instant, protocolVersion);
    }

    @Nullable
    @Override
    public LocalDateTime decode(@Nullable ByteBuffer byteBuffer, @NonNull ProtocolVersion protocolVersion) {
        Instant instant = instantCodec.decode(byteBuffer, protocolVersion);
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
    }

    @NonNull
    @Override
    public String format(@Nullable LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "NULL";
        }
        return instantCodec.format(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
    }

    @Nullable
    @Override
    public LocalDateTime parse(@Nullable String s) {
        Instant instant = instantCodec.parse(s);
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
    }
}