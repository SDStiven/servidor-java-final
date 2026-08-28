package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.dto.ExchangeRateResponse;
import com.labanta.servidorlocal.dto.GeoLocationResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoServiceService {

    private final RestTemplate restTemplate;

    public GeoServiceService (RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public GeoLocationResponseDTO localizarIP(String ip) {
        String url = "https://ipapi.co/" + ip + "/json/";

        try {
            return restTemplate.getForObject(
                    url,
                    GeoLocationResponseDTO.class
            );

        } catch (HttpClientErrorException.TooManyRequests e) {
            System.out.println("Limite da API ipapi.co foi atingido.");
            return null;
        }
    }
}