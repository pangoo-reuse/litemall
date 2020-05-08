package org.linlinjava.litemall.wx.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.linlinjava.litemall.core.util.DateTimeUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

//时间序列化时变为时间戳
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//        long milli = localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        String timeDisplayString = DateTimeUtil.getDateTimeDisplayString(localDateTime);
        jsonGenerator.writeString(timeDisplayString);
    }
}