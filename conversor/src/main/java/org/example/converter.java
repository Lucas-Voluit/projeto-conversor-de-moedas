package org.example;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Scanner;

public class converter {
    // CONSUMIR CHAVE API
    private static final String API_KEY = "0df8b9d59ad413b25e28348e";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] currencies = {"USD", "EUR", "GBP", "BRL", "JPY", "CAD"};

        System.out.println("Escolha a moeda de origem: ");
        for (int i = 0; i < currencies.length; i++) {
            System.out.println((i + 1) + ". " + currencies[i]);
        }
        int fromChoice = scanner.nextInt() - 1;

        System.out.println("Escolha a moeda de destino: ");
        for (int i = 0; i < currencies.length; i++) {
            System.out.println((i + 1) + ". " + currencies[i]);
        }
        int toChoice = scanner.nextInt() - 1;

        System.out.println("Digite o valor a ser convertido: ");
        double amount = scanner.nextDouble();

        String fromCurrency = currencies[fromChoice];
        String toCurrency = currencies[toChoice];

        try {
            double rate = getExchangeRate(fromCurrency, toCurrency);
            double convertedAmount = amount * rate;
            System.out.printf("%.2f %s = %.2f %s\n", amount, fromCurrency, convertedAmount, toCurrency);
        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    public static double getExchangeRate(String fromCurrency, String toCurrency) throws Exception {
        String urlStr = API_URL + fromCurrency;
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
        JsonObject conversionRates = jsonResponse.getAsJsonObject("conversion_rates");

        return conversionRates.get(toCurrency).getAsDouble();
    }
}