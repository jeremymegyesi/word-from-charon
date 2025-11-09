package me.jeremymegyesi.CharonCommon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateApiBrokerServiceImpl implements ApiBrokerService {

    private final RestTemplate restTemplate;
    
    @Value("${charonenv.baseUrl}")
    private String baseUrl;

    public RestTemplateApiBrokerServiceImpl() {
        this.restTemplate = new RestTemplate();
    }
    
    public Object fetchDataFromApi(String port, String endpoint, Class<?> responseType) {
        String url = this.baseUrl + port + endpoint;
        try {
            return restTemplate.getForObject(url, responseType);
        } catch (Exception e) {
            // Handle exceptions such as connection errors or timeouts
            e.printStackTrace();
            return null; // or throw a custom exception
        }
    }
    
}
