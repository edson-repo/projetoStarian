package br.com.softplan.report.dto;

import java.time.LocalDateTime;

public class ApiError {

    // Momento em que o erro foi gerado (útil pra rastrear logs e reproduzir o problema).
    private LocalDateTime timestamp;

    // Código HTTP (ex.: 400, 404, 500).
    private int status;

    // Nome curto do erro (ex.: "Bad Request", "Not Found").
    private String error;

    // Mensagem principal que você quer devolver pro cliente.
    private String message;

    // Caminho da rota que causou o erro (ex.: "/observacoes/gerar").
    private String path;

    public ApiError(LocalDateTime timestamp, int status, String error, String message, String path) {
        // Classe simples: só carrega os dados do erro pra devolver na resposta.
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
