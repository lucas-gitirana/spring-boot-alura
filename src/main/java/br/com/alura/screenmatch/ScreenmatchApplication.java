package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@java.lang.Override
	public void run(java.lang.String... args) throws Exception {
		var consumoApi = new ConsumoAPI();

		//API do OMDB
		//&apikey=d924e6d8
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=friends&apikey=d924e6d8");
		System.out.println(json);

		//API DE CAFÉ
		//json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
		//System.out.println(json);

		// Instanciando classe conversora
		ConverteDados conversor = new ConverteDados();

		// Obtendo dados da série
		json = consumoApi.obterDados("https://www.omdbapi.com/?t=friends&apikey=d924e6d8");
		DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dadosSerie);

		// Obtendo dados do episódio
		json = consumoApi.obterDados("https://www.omdbapi.com/?t=friends&season=1&episode=1&apikey=d924e6d8");
		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodio);

		//Obtendo dados de temporadas
		List<DadosTemporada> dadosTemporadas = new ArrayList<>();

		for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
			json = consumoApi.obterDados("https://www.omdbapi.com/?t=friends&season=" +i+ "&apikey=d924e6d8");
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			dadosTemporadas.add(dadosTemporada);
		}

		dadosTemporadas.forEach(System.out::println);
	}
}
