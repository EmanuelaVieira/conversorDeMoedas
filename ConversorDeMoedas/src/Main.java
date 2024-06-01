import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        Convensor convensor = new Convensor();

        boolean continuar = true;

        while (continuar) {
            System.out.println("""
                    \n*************************
                    MOEDAS DISPONIVEIS PARA CONVERSÃO:
                    1 -> ARS - Peso argentino
                    2 -> BOB - Boliviano boliviano
                    3 -> BRL - Real brasileiro
                    4 -> CLP - Peso chileno
                    5 -> COP - Peso colombiano
                    6 -> USD - Dólar americano
                    0 -> SAIR
                    *************************
                    """);
            System.out.println("\nDIGITE OPÇÃO DA MOEDA DE ORIGEM ou 0 PARA SAIR): ");
            int opcao = leitor.nextInt();
            System.out.println(obterOpcao(opcao)); //Retorna o codigo da moeda escolhida

            switch (opcao) {
                case 0:
                    continuar = false;
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:

                    Moedas moedas = convensor.buscaMoeda(obterOpcao(opcao));
                    String[] currencyCodes = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};
                    try {
                        Map<String, Double> taxasFiltradas = convensor.filtrarMoedas(moedas, currencyCodes);

                        System.out.println("\nDIGITE O VALOR A SER CONVERTIDO: ");
                        double valor = leitor.nextDouble();
                        leitor.nextLine();  // Pega a nova linha deixada pelo nextDouble(), sem ela da um erro.

                        System.out.println("\nDIGITE A OPÇAO DA MOEDA DE DESTINO: ");
                        int moedaDestinoOpcao = leitor.nextInt();
                        String moedaDestino = obterOpcao(moedaDestinoOpcao);

                        double valorConvertido = convensor.converterMoeda(taxasFiltradas, valor, moedas.base_code(), moedaDestino);

                        System.out.println("Convertendo para " + moedaDestino + "...");
                        System.out.println("Valor convertido: " + valorConvertido + " " + moedaDestino);

                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        System.out.println("Encerrando a aplicação...");
    }

    private static String obterOpcao(int opcao) {
        switch (opcao) {
            case 0:
                return "Saindo..."; //Necessario pq estava retornando um erro antes da finalizacao
            case 1:
                return "ARS";
            case 2:
                return "BOB";
            case 3:
                return "BRL";
            case 4:
                return "CLP";
            case 5:
                return "COP";
            case 6:
                return "USD";
            default:
                throw new IllegalArgumentException("Código de moeda inválido!");
        }
    }
}
