import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            int opcion;
            double cantidad;

            do {
                System.out.println("\nSea bienvenido/a al conversor de Moneda =]\n");

                System.out.println("""
                        1) Dólar => Peso Argentino
                        2) Peso Argentino => Dólar
                        3) Dólar => Real Brasileño
                        4) Real Brasileño => Dólar
                        5) Dólar => Peso Colombiano
                        6) Peso Colombiano => Dólar
                        7) Salir
                        """);

                System.out.print("Elija una opción válida: ");
                opcion = sc.nextInt();

                
                if (opcion >= 1 && opcion <= 6) {
                    System.out.print("Ingrese la cantidad a convertir: ");
                    cantidad = sc.nextDouble();
                } else {
                    cantidad = 0;
                }

                switch (opcion) {
                    case 1:
                        convertirMoneda("USD", "ARS", cantidad);
                        break;
                    case 2:
                        convertirMoneda("ARS", "USD", cantidad);
                        break;
                    case 3:
                        convertirMoneda("USD", "BRL", cantidad);
                        break;
                    case 4:
                        convertirMoneda("BRL", "USD", cantidad);
                        break;
                    case 5:
                        convertirMoneda("USD", "COP", cantidad);
                        break;
                    case 6:
                        convertirMoneda("COP", "USD", cantidad);
                        break;
                    case 7:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción no válida, por favor intente nuevamente.");
                }
            } while (opcion != 7);
            sc.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            
        }
    }

    private static void convertirMoneda(String fromCurrency, String toCurrency, double cantidad) {
        try {
            String direccion = "https://v6.exchangerate-api.com/v6/788071f7d68fcfbb36b9dcec/pair/" + fromCurrency + "/" + toCurrency;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(direccion))
                    .build();

            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();

          
            String searchString = "\"conversion_rate\":";
            int startIndex = responseBody.indexOf(searchString) + searchString.length();

           
            int endIndex = startIndex;
            while (endIndex < responseBody.length() &&
                    (Character.isDigit(responseBody.charAt(endIndex)) ||
                            responseBody.charAt(endIndex) == '.')) {
                endIndex++;
            }

            
            String tasaCambioStr = responseBody.substring(startIndex, endIndex).trim();
            double tasaCambio = Double.parseDouble(tasaCambioStr);

            double resultado = cantidad * tasaCambio;
            System.out.printf("%.2f %s son %.2f %s.%n", cantidad, fromCurrency, resultado, toCurrency);
        } catch (Exception e) {
            System.out.println("Error al convertir la moneda: " + e.getMessage());
        }
    }
}
