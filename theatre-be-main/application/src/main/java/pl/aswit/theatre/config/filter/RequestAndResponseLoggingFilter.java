package pl.aswit.theatre.config.filter;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class RequestAndResponseLoggingFilter extends OncePerRequestFilter {

    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
        MediaType.valueOf("text/*"),
        MediaType.APPLICATION_FORM_URLENCODED,
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML,
        MediaType.valueOf("application/*+json"),
        MediaType.valueOf("application/*+xml"),
        MediaType.MULTIPART_FORM_DATA
    );

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    private void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain)
        throws IOException, ServletException {
        try {
            beforeRequest(request);
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request, response);
            response.copyBodyToResponse();
        }
    }

    private void beforeRequest(ContentCachingRequestWrapper request) {
        if (log.isInfoEnabled()) {
            logRequestHeader(request, request.getRemoteAddr() + "|>");
        }
    }

    private void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (log.isInfoEnabled()) {
            logRequestBody(request, request.getRemoteAddr() + "|>");
            logResponse(response, request.getRemoteAddr() + "|<");
        }
    }

    private static void logRequestHeader(ContentCachingRequestWrapper request, String prefix) {
        log.info("{} {}", prefix, getRequestedPath(request));
        if (log.isTraceEnabled()) {
            Collections
                .list(request.getHeaderNames())
                .forEach(headerName ->
                    Collections.list(request.getHeaders(headerName)).forEach(headerValue -> log.trace("{} {}: {}", prefix, headerName, headerValue))
                );
        }
    }

    private static String getRequestedPath(ContentCachingRequestWrapper request) {
        val queryString = request.getQueryString();
        if (queryString == null) {
            return request.getMethod() + " " + request.getRequestURI();
        } else {
            return (request.getMethod() + " " + request.getRequestURI() + "?" + queryString);
        }
    }

    private static void logRequestBody(ContentCachingRequestWrapper request, String prefix) {
        if (log.isDebugEnabled()) {
            val content = request.getContentAsByteArray();
            if (content.length > 0) {
                logContent(content, request.getContentType(), request.getCharacterEncoding(), prefix);
            }
        }
    }

    private static void logResponse(ContentCachingResponseWrapper response, String prefix) {
        val status = response.getStatus();
        log.info("{} {} {}", prefix, status, HttpStatus.valueOf(status).getReasonPhrase());
        if (log.isTraceEnabled()) {
            response
                .getHeaderNames()
                .forEach(headerName -> response.getHeaders(headerName).forEach(headerValue -> log.trace("{} {}: {}", prefix, headerName, headerValue)));
        }
        if (log.isDebugEnabled()) {
            val content = response.getContentAsByteArray();
            if (content.length > 0) {
                logContent(content, response.getContentType(), response.getCharacterEncoding(), prefix);
            }
        }
    }

    private static void logContent(byte[] content, String contentType, String contentEncoding, String prefix) {
        val visible = VISIBLE_TYPES
            .stream()
            .anyMatch(visibleType -> visibleType.includes(StringUtils.isNotEmpty(contentType) ? MediaType.valueOf(contentType) : null));
        if (visible) {
            try {
                val contentString = new String(content, Charset.forName(contentEncoding));
                log.debug("{} {}", prefix, contentString.replaceAll("[\r\n]+\\s*", ""));
            } catch (Exception e) {
                log.debug("{} [{} bytes content]", prefix, content.length);
            }
        } else {
            log.debug("{} [{} bytes content]", prefix, content.length);
        }
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper wrapper) {
            return wrapper;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper wrapper) {
            return wrapper;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}
