package com.phh.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * util for json,xml dependency jackson
 *
 * @author phh
 * @version V1.0
 * @project: spring
 * @package com.github.phh.benefit.common.utils
 * @date 2019/4/16
 */
@Slf4j
public class JacksonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final XmlMapper XMLMAPPER = new XmlMapper();

    static {
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //忽略未知字段,add by phh at 2017-2-9
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * json to list
     *
     * @param json
     * @return
     */
    public static final List json2List(String json) {
        return json2Object(json, List.class);
    }

    /**
     * json to map
     *
     * @param json
     * @return
     */
    public static final Map json2Map(String json) {
        return json2Object(json, Map.class);
    }

    /**
     * json to object
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static final <T> T json2Object(String json, Class<T> type) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        }
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json transform object error");
        }
    }

    /**
     * json 转换对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T json2Object(String json, TypeReference<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("json transform object error");
        }
    }

    /**
     * 对象转换json
     *
     * @param value
     * @return
     */
    public static String writeAsString(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("object transform json error");
        }
    }

    /**
     * 对象转换xml
     *
     * @param value
     * @return
     */
    public static String writeAsXml(Object value) {
        try {
            return XMLMAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("object transform xml error");
        }
    }

    public static <T> T xml2Object(String xml, Class<T> clazz) {
        try {
            return XMLMAPPER.readValue(xml, clazz);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("xml transform object error");
        }
    }

    public static <T> T xml2Object(String xml, TypeReference<T> type) {
        try {
            return XMLMAPPER.readValue(xml, type);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("xml transform object error");
        }
    }

    public static String xml2json(String xml) {
        try {
            StringWriter w = new StringWriter();
            JsonParser jp = XMLMAPPER.getFactory().createParser(xml);
            JsonGenerator jg = MAPPER.getFactory().createGenerator(w);
            while (jp.nextToken() != null) {
                jg.copyCurrentEvent(jp);
            }
            jp.close();
            jg.close();
            return w.toString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("xml transform json error");
        }
    }

}
