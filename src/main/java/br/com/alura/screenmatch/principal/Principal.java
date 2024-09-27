package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    }
}
