package br.com.softplan.report.exception;

public class BadRequestException extends RuntimeException {

    // Usamos essa exceção quando o cliente manda algo inválido (ex.: lista vazia).
    // Normalmente o handler global transforma isso em HTTP 400 com a mensagem.
    public BadRequestException(String message) {
        super(message);
    }
}
