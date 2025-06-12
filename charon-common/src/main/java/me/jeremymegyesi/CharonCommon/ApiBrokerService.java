package me.jeremymegyesi.CharonCommon;

public interface ApiBrokerService {
    Object fetchDataFromApi(String port, String endpoint, Class<?> responseType);
}
