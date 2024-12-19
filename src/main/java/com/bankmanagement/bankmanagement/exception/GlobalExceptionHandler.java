package com.bankmanagement.bankmanagement.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectWriter objectWriter;
    private final Function<Throwable, HttpStatus> statusResolver;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        this.statusResolver = ex -> {
            if (ex instanceof BadRequestException) {
                return HttpStatus.BAD_REQUEST;
            }
            if (ex instanceof NotFoundException) {
                return HttpStatus.NOT_FOUND;
            }
            if (ex instanceof ValidationException) {
                return HttpStatus.BAD_REQUEST;
            }
            return HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        HttpStatus status = statusResolver.apply(ex);
        String errorMessage =
                status == HttpStatus.INTERNAL_SERVER_ERROR ?
                        "INTERNAL SERVER ERROR" : ex.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(status, errorMessage);

        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Function<ErrorResponse, DataBuffer> errorResponseSerializer = errorResp -> {
            try {
                byte[] bytes = objectWriter.writeValueAsBytes(errorResp);
                return response.bufferFactory().wrap(bytes);
            } catch (JsonProcessingException e) {
                return response.bufferFactory().wrap("{}".getBytes());
            }
        };

        return response.writeWith(Mono.fromSupplier(() ->
                errorResponseSerializer.apply(errorResponse)
        ));
    }

    @Bean
    public WebFluxConfigurer corsConfigurer() {
        return new WebFluxConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
