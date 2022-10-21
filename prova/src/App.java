
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.IntSummaryStatistics;



public class App {
    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner in = new Scanner(System.in);
        List<produto> prod = new ArrayList<>();
        List<venda> vd = new ArrayList<>();
    
        int opcao;
        String buscarP;
        int quant;
    
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        do {
            System.out.println("\n****\nMENU\n****\n");
            System.out.println("1 - Incluir produto");
            System.out.println("2 - Consultar produto ");
            System.out.println("3 - Listagem de produtos");
            System.out.println("4 - Vendas por período");
            System.out.println("5 - Realizar venda");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            opcao = in.nextInt();
            in.nextLine(); // Tira o ENTER que ficou na entrada na instrução anterior

            if (opcao == 1) {
                System.out.println("\nDigite o código do produto: \nDigite o nome do protudo: \nDigite o preço do produto: \nDigite a quantidade total do produto: ");
                produto p = new produto(in.nextLine(), in.nextLine(), in.nextInt(), in.nextInt());

                prod.add(p);
                voltarMenu(in);
            } else if (opcao == 2) {
                if (prod.isEmpty()) {
                    System.out.println("\nNão há produtos para serem consultados.");
                    voltarMenu(in);
                    continue;
                }
                System.out.println("Digite o código do produto: ");
                buscarP = in.nextLine();
                for (produto p : prod) {
                    if (buscarP.equals(p.getCodigo())) {
                        System.out.println("\n- Nome do produto: " + p.getNome() + " - Preço: " + p.getPreco() +
                                " - Quantidade total: " + p.getQuantidadetotal());
                    }
                    if (buscarP != p.getCodigo()) {
                        System.out.println("CÓDIGO NÃO CADASTRADO!");
                    }

                    voltarMenu(in);
                }
            } else if (opcao == 3) {
                if (prod.isEmpty()) {
                    System.out.println("\nNão há produto para listar.");
                    voltarMenu(in);
                    continue;
                }
                for (produto p : prod) {
                    System.out.println("Lista de produtos:\n");
                    System.out.printf("Código: %s - Nome: %s - Preço: %d - Quantidade total: %d\n", p.getCodigo(),
                            p.getNome(), p.getPreco(), p.getQuantidadetotal());

                }
                IntSummaryStatistics resumo = prod.stream().collect(Collectors.summarizingInt(produto::getPreco));
                resumo.getAverage();
                resumo.getMin();
                resumo.getMax();
                System.out.println("Valor médio: " + resumo.getAverage() + "\nValor mínimo: " + resumo.getMin() +
                        "\nValor máximo: " + resumo.getMax());

                voltarMenu(in);
            } else if (opcao == 4) {
                System.out.println("Relatório de vendas:\n");
                DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
                System.out.println("yy/MM/dd HH:mm:ss-> " + dtf2.format(LocalDateTime.now()));
                for (venda sell : vd) {

                    System.out.printf("Data da compra: %s - Nome: %s - Preço: %d - Quantidade Vendida: %d - Valor unitário: %d - Valor Total: %d \n", sell.getData(),
                            sell.getNome(), sell.getPreco(), sell.getquantidadeVendida(), sell.getPreco(), sell.getPreco()*sell.getquantidadeVendida());
                }
                //Rodapé
                Map<LocalDate, Double> valoresMedios = vd.stream()
                        .collect(Collectors.groupingBy(venda::getData, Collectors.averagingDouble(venda::getValorVenda)));

                valoresMedios.entrySet().forEach(
                        item -> System.out.printf("Data: %s - Valor médio do dia: %.2f\n",
                                item.getKey().format(df), item.getValue()));

                voltarMenu(in);
            } else if (opcao == 5) {

                System.out.println("Digite o código do produto: ");
                buscarP = in.nextLine();

                for (produto p : prod) {
                    if (buscarP.equals(p.getCodigo())) {

                        System.out.println(
                                "Informações do produto selecionado: ");
                        System.out.printf(
                                "Nome: %s - Preço: %d\n", p.getNome(), p.getPreco());
                        System.out.printf("Digite a quantidade do produto: ");
                        quant = in.nextInt();

                        venda sell = new venda(LocalDate.now(), p.getNome(), p.getPreco(), quant);

                        if (quant <= p.getQuantidadetotal()) {
                            vd.add(sell);
                            p.setQuantidadetotal(p.getQuantidadetotal() - quant);
                            System.out.println("Venda realizada com sucesso!");
                            voltarMenu(in);
                            continue;
                          
                        } else {
                            System.out.println("QUANTIA MAIOR QUE QUANTIA DISPONIVEL! COMPRA NÃO EFETUADA!");
                            voltarMenu(in);
                            continue;
                        }
                        
                    }
                
                }
            } else if (opcao != 0) {
                System.out.println("\nOpção inválida!");
            }
        } while (opcao != 0);

        System.out.println("Fim do programa!");

        in.close();
    }

    private static void voltarMenu(Scanner in) throws InterruptedException, IOException {
        System.out.println("\nPressione ENTER para voltar ao menu.");
        in.nextLine();

        // Limpa toda a tela, deixando novamente apenas o menu
        if (System.getProperty("os.name").contains("Windows"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else
            System.out.print("\033[H\033[2J");

        System.out.flush();
    }
}
