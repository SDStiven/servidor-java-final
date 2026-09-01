package com.labanta.servidorlocal.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    
    public void enviarEmailBoasVindas(String EmailDestino, String nomeUtilizador) {
        try {
            // criar um email simples (testo limpo)
            SimpleMailMessage mensagem = new SimpleMailMessage();

            mensagem.setTo(EmailDestino);
            mensagem.setSubject("Bem-vindo ao Marketplace!");
            mensagem.setText("Olá " + nomeUtilizador + "!\n\n" +
                    "A tua conta foi criada com suceso . já podes fazer login." +
                    "e explorar os nossos serviços .\n\n" +
                    "Com os melhores cumprimentos,\nEquipe do Marketplace");

            // enviar!
            mailSender.send(mensagem);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void enviarEmailOrcamento(String emailDestino, String nomeServico, Double precoConvertido, String moeda) {

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(emailDestino);
        mensagem.setSubject("O teu Orçamento do Marketplace");

        // Criar o corpo do corpo do email
        String corpo = String.format(
                "Olá!\n\nAqui tens o orçamento solicitado para o serviço:\n\n" +
                        "Serviço: %s\n" +
                        "Preço Final: %.2f %s\n\n" +
                        "Este valor foi calculado com a taxa de câmbio em tempo real.\n" +
                        "Obrigado por usares o nosso Marketplace!",
                nomeServico, precoConvertido, moeda
        );

        mensagem.setText(corpo);
        mailSender.send(mensagem);
    }

    public void enviarAlertaSeguranca(String emailDestino, String cidade, String pais){
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(emailDestino);
        mensagem.setSubject("Aviso de Segurança:");

        mensagem.setText(
                "Detetámos uma nova atividade na tua conta do Marketplace a partir de "+ cidade +
                ","+ pais +" Se não foste tu, altera a tua password imediatamente!"
        );

        mailSender.send(mensagem);

    }


}
