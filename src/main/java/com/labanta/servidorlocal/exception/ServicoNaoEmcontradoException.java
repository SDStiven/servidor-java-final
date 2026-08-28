package com.labanta.servidorlocal.exception;

public class ServicoNaoEmcontradoException extends RuntimeException{

    public ServicoNaoEmcontradoException(String mensagem){
        super(mensagem);
    }
}
