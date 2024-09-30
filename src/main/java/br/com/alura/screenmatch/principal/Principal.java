package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner s = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    private static final String ENDERECO = "https://www.omdbapi.com/?t=";
    private static final String API_KEY = "&apikey=d924e6d8";

    public void exibeMenu() {
        System.out.println("Informe o nome da série: ");
        var nomeSerie = s.nextLine();
        var json = consumo.obterDados(ENDERECO +nomeSerie.replace(" ", "+")+ API_KEY);

        // Carregando série
        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        //Carregando temporadas
        List<DadosTemporada> dadosTemporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" +i+ API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            dadosTemporadas.add(dadosTemporada);
        }
        dadosTemporadas.forEach(System.out::println);

        dadosTemporadas.forEach(t -> t.episodios().forEach( e -> System.out.println(e.titulo())));

        // Top 5
        List<DadosEpisodio> dadosEpisodios = dadosTemporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTOP 5 EPISÓDIOS:");
        /*dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);*/



        // Episódios usando classe
        List<Episodio> episodios = dadosTemporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(e -> new Episodio(t.numero(), e))
                )
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);




        // Manipulando datas
        /*System.out.println("A partir de qual ano você deseja assistir à série?");
        var ano = s.nextInt();
        s.nextLine();

        LocalDate dataBase = LocalDate.of(ano, 1, 1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBase))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getNumeroTemporada() +
                                " Episódio: " +e.getNumero()+
                                    " Data de Lançamento: " + e.getDataLancamento().format(dtf)
                ));*/


        // Utilizando peek para debuggar os streams
        /*dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Filtrando os episódios com avaliação != de N/A " + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .peek(e -> System.out.println("Ordenando episódios por avaliação DESCRESCENTE " + e))
                .limit(10)
                .peek(e -> System.out.println("Limitando os 10 primeiros " + e))
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println("Mapeando o título dos episódios " + e))
                .forEach(System.out::println);*/



        // Econtrando a primeira ocorrência de valores
        System.out.println("Digite o nome do episódio para encontrar sua temporada: ");
        var trechoTitulo = s.nextLine();
        Optional<Episodio> episodioEncontrado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if (episodioEncontrado.isPresent()) {
            System.out.println("Episódio encontrado! \nTemporada: " + episodioEncontrado.get().getNumeroTemporada());
        } else {
            System.out.println("Episódio não encontrado!");
        }


    }
}
