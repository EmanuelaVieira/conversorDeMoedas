import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Convensor {
    public Moedas buscaMoeda(String moeda) { //Faz a requisição e "processa" o Json
        URI link = URI.create("https://v6.exchangerate-api.com/v6/430f553e08284bb5ca57f81f/latest/" + moeda);

        //Requisição
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(link)
                .build();


        //Resposta
        {
            Moedas moedas;
            try {
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                //Trecho a ser aprendido melhor
                JsonObject JsonResponse = JsonParser.parseString(response.body())
                        .getAsJsonObject();
                String baseCode = JsonResponse.get("base_code").getAsString();
                JsonObject conversionRates = JsonResponse.getAsJsonObject("conversion_rates");
                Map<String, Double> ratesMap = new Gson().fromJson(conversionRates, Map.class);
                moedas = new Moedas(baseCode, ratesMap);
                return moedas;

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Preciso entender melhor aqui!!!
    public Map<String, Double> filtrarMoedas(Moedas moedas, String[] currencyCodes) { //Filtra as taxas
        Map<String, Double> taxasFiltradas = new HashMap<>();
        for (String code : currencyCodes) {
            if (moedas.conversion_rates().containsKey(code)) {
                taxasFiltradas.put(code, moedas.conversion_rates().get(code));
            }
        }
        return taxasFiltradas;

    }

    public double converterMoeda(Map<String, Double> taxas, double valor, String moedaOrigem, String moedaDestino) {
        if (!taxas.containsKey(moedaOrigem) || !taxas.containsKey(moedaDestino)) {
            throw new IllegalArgumentException("Opção inválida.");
        }
        double taxaOrigem = taxas.get(moedaOrigem);
        double taxaDestino = taxas.get(moedaDestino);
        return (valor / taxaOrigem) * taxaDestino;
    }
}
