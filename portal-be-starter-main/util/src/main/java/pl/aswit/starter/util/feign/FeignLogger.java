package pl.aswit.starter.util.feign;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.Request;
import feign.Response;
import feign.Util;
import feign.slf4j.Slf4jLogger;
import lombok.val;

import static feign.Util.UTF_8;
import static feign.Util.decodeOrDefault;
import static feign.Util.valuesOrEmpty;

public class FeignLogger extends Slf4jLogger {

    private final Logger logger;

    public FeignLogger(Class<?> clazz) {
        super(clazz);
        this.logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (logger.isInfoEnabled()) {
            info(configKey, "---> %s %s HTTP/1.1", request.httpMethod().name(), request.url());
            if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {
                for (String field : request.headers().keySet()) {
                    for (String value : valuesOrEmpty(request.headers(), field)) {
                        trace(configKey, "%s: %s", field, value);
                    }
                }

                int bodyLength = 0;
                val body = request.body();
                if (body != null) {
                    bodyLength = body.length;
                    if (logLevel.ordinal() >= Level.FULL.ordinal()) {
                        String bodyText = request.charset() != null ? new String(body, request.charset()) : null;
                        debug(configKey, "%s", bodyText != null ? bodyText : "Binary data");
                    }
                }
                debug(configKey, "---> END HTTP (%s-byte body)", bodyLength);
            }
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        if (logger.isInfoEnabled()) {
            String reason = response.reason() != null && logLevel.compareTo(Level.NONE) > 0 ? " " + response.reason() : "";
            int status = response.status();
            info(configKey, "<--- HTTP/1.1 %s%s (%sms)", status, reason, elapsedTime);
            if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {
                for (String field : response.headers().keySet()) {
                    for (String value : valuesOrEmpty(response.headers(), field)) {
                        trace(configKey, "%s: %s", field, value);
                    }
                }

                int bodyLength = 0;
                if (response.body() != null && !(status == 204 || status == 205)) {
                    // HTTP 204 No Content "...response MUST NOT include a message-body"
                    // HTTP 205 Reset Content "...response MUST NOT include an entity"
                    byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                    bodyLength = bodyData.length;
                    if (logLevel.ordinal() >= Level.FULL.ordinal() && bodyLength > 0) {
                        debug(configKey, "%s", decodeOrDefault(bodyData, UTF_8, "Binary data"));
                    }
                    debug(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
                    return response.toBuilder().body(bodyData).build();
                } else {
                    debug(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
                }
            }
            return response;
        }
        return response;
    }

    private void trace(String configKey, String format, Object... args) {
        if (logger.isTraceEnabled()) {
            logger.trace(getContent(configKey, format, args));
        }
    }

    private void debug(String configKey, String format, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(getContent(configKey, format, args));
        }
    }

    private void info(String configKey, String format, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(getContent(configKey, format, args));
        }
    }

    private String getContent(String configKey, String format, Object... args) {
        return String.format(methodTag(configKey) + format, args);
    }
}
