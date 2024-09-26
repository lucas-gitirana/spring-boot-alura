package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.service.ConsumoAPI;

import java.util.Scanner;

public class Principal {

    private Scanner s = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();

    private static final String ENDERECO = "https://www.omdbapi.com/?t=";
    private static final String API_KEY = "&apikey=d924e6d8";

    public void exibeMenu() {
        System.out.println("Informe o nome da série: ");
        var nomeSerie = s.nextLine();
        var json = consumo.obterDados(ENDERECO +nomeSerie.replace(" ", "+")+ API_KEY);
    }
}
