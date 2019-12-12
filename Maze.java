import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Maze implements IMaze {
	private Point[][] pontos;
	private boolean[][] visitados;
	private int colunas;
	private int linhas;
	private Stack<Point> pilha;
	private boolean saidaEncontrada;

	@Override
	public void load(File file) throws IOException {
		Scanner s = null;
		try {
			s = new Scanner(new BufferedReader(new FileReader(file)));
			String[] line;
			line = s.nextLine().split(" ");

			// inicializa as v�riaveis/atributos
			linhas = Integer.parseInt(line[0]);
			colunas = Integer.parseInt(line[1]);
			pontos = new Point[linhas][colunas];
			visitados = new boolean[linhas][colunas];
			int totalPontos = linhas * colunas;
			pilha = new StaticStack<>(totalPontos);
			char[][] aux = new char[linhas][colunas];
			saidaEncontrada = false;

			// inclui os caracteres ao array auxiliar
			for (int i = 0; i < aux.length; i++)
				aux[i] = s.nextLine().toCharArray();

			// cria os pontos, incluindo suas coordenadas (linha e coluna) e seu s�mbolo
			// (char)
			for (int i = 0; i < pontos.length; i++)
				for (int j = 0; j < pontos[i].length; j++)
					pontos[i][j] = new Point(i, j, aux[i][j]);
		} finally {
			s.close();
		}
	}

	// verifica se o ponto procurado est� dentro dos limites da matriz
	private boolean isForaDaMatriz(int i, int j) {
		if (i < 0 || i >= linhas || j < 0 || j >= colunas)
			return true;
		else
			return false;
	}

	// verifica se o ponto procurado � uma parede "x"
	private boolean isParede(int i, int j) {
		if (!isForaDaMatriz(i, j)) {
			if (pontos[i][j].getSimbolo() == 'x')
				return true;
			else
				return false;
		}
		return false;
	}

	// verifica se o ponto procurado � a sa�da do labirinto "#"
	private boolean isSaida(int i, int j) {
		if (!isForaDaMatriz(i, j)) {
			if (pontos[i][j].getSimbolo() == '#') {
				pontos[i][j].setStatus("Sa�da");
				return true;
			}
			else
				return false;
		}
		return false;
	}

	// verifica se o ponto procurado n�o foi visitado, n�o est� sendo repitido, est�
	// na matriz e n�o � uma parede,
	// se tudo estiver ok (caminho v�lido ou saida), empilha, seta o status como
	// visitado e diz que foi empilhado
	private void checarVizinho(int i, int j) {
		if (!isForaDaMatriz(i, j))
			if (!isParede(i, j))
				if (!visitados[i][j] && !pontos[i][j].isEmpilhado()) {
					pilha.push(pontos[i][j]);
					pontos[i][j].setStatus("Visitado");
					pontos[i][j].setEmpilhado(true);
					return;
				}
	}

	// verifica quais pontos s�o abandonados
	private void checaAbandonados() {
		for (int i = 0; i < linhas; i++)
			for (int j = 1; j < colunas; j++) {
				// verifica a coluna da esquerda, a da direita e a linha de baixo para ver se
				// s�o paredes, se sim o ponto � abandonado
				if (pontos[i][j].getSimbolo() == '*' && isParede(i, j - 1) && isParede(i, j + 1)
						&& isParede(i + 1, j)) {
					pontos[i][j].setSimbolo('.');
					pontos[i][j].setStatus("Abandonado");
					if (i > 0)
						if (pontos[i - 1][j].getSimbolo() == '*' && isParede(i-1, j + 1) && isParede(i-1,j-1)) {
							pontos[i - 1][j].setSimbolo('.');
							pontos[i - 1][j].setStatus("Abandonado");
						}
					if (i > 1)
						if (pontos[i - 2][j].getSimbolo() == '*' && isParede(i-2, j + 1) && isParede(i-2, j-1)) {
							pontos[i - 2][j].setSimbolo('.');
							pontos[i - 2][j].setStatus("Abandonado");
							break;
						}
				}

				// verifica a coluna da esquerda, a da direita e a linha de cima para ver se
				// s�o paredes, se sim o ponto � abandonado
				if (pontos[i][j].getSimbolo() == '*' && isParede(i, j - 1) && isParede(i, j + 1)
						&& isParede(i - 1, j)) {
					pontos[i][j].setSimbolo('.');
					pontos[i][j].setStatus("Abandonado");
					//ve se os pontos das 2 prox linhas devem ser abandonados
					if (i + 1 < linhas)
						if (pontos[i + 1][j].getSimbolo() == '*' && isParede(i+1, j + 1) && isParede(i+1, j - 1)) {
							pontos[i + 1][j].setSimbolo('.');
							pontos[i + 1][j].setStatus("Abandonado");
						}
					if (i + 2 < linhas)
						if (pontos[i + 2][j].getSimbolo() == '*' && isParede(i+2, j + 1) && isParede(i+2, j - 1)) {
							pontos[i + 2][j].setSimbolo('.');
							pontos[i + 2][j].setStatus("Abandonado");
							break;
						}
				}

				// verifica a coluna da esquerda, a linha de baixo e a de cima para ver se
				// s�o paredes, se sim o ponto � abandonado
				if (pontos[i][j].getSimbolo() == '*' && isParede(i, j - 1) && isParede(i - 1, j)
						&& isParede(i + 1, j)) {
					if (j + 1 < colunas)
						if (pontos[i][j + 1].getSimbolo() == '*' && isParede(i+1, j+1) && isParede(i - 1, j+1)) {
							pontos[i][j + 1].setSimbolo('.');
							pontos[i][j + 1].setStatus("Abandonado");
						}
					if (j + 2 < colunas)
						if (pontos[i][j + 2].getSimbolo() == '*' && isParede(i + 1, j+2) && isParede(i - 1, j+1)) {
							pontos[i][j + 2].setSimbolo('.');
							pontos[i][j + 2].setStatus("Abandonado");
							break;
						}
				}

				// verifica a coluna da direita, a linha de baixo e a de cima para ver se
				// s�o paredes, se sim o ponto � abandonado
				if (pontos[i][j].getSimbolo() == '*' && isParede(i, j + 1) && isParede(i - 1, j)
						&& isParede(i + 1, j)) {
					if (j - 1 > 0)
						if (pontos[i][j - 1].getSimbolo() == '*' && isParede(i + 1, j-1) && isParede(i - 1, j-1)) {
							pontos[i][j - 1].setSimbolo('.');
							pontos[i][j - 1].setStatus("Abandonado");
						}
					if (j - 2 > 0)
						if (pontos[i][j - 2].getSimbolo() == '*' && isParede(i + 1, j-2) && isParede(i - 1, j-1)) {
							pontos[i][j - 2].setSimbolo('.');
							pontos[i][j - 2].setStatus("Abandonado");
							break;
						}
				}
			}
	}

	@Override
	public void findOut() {
		// inicio
		int i = 0;
		int j = 0;

		// em quanto a sa�da n�o for encontrada, o algoritmo continuar� rodando
		while (!saidaEncontrada) {
			if (!visitados[i][j]) {
				visitados[i][j] = true;
				pontos[i][j].setSimbolo('*');

				checarVizinho(i - 1, j);
				checarVizinho(i, j - 1);
				checarVizinho(i, j + 1);
				checarVizinho(i + 1, j);
			}

			// cria uma v�riavel com o ponto atual (da pilha) e se ela for a sa�da, acaba a
			// execu��o
			Point current = pilha.pop();
			if (current != null) {
				i = current.getLinha();
				j = current.getColuna();
			}

			if (isSaida(i, j))
				saidaEncontrada = true;
		}

		checaAbandonados();
	}

	@Override
	public void show() {
		for (int i = 0; i < pontos.length; i++) {
			for (int j = 0; j < pontos[i].length; j++)
				System.out.print(pontos[i][j].getSimbolo() + " ");
			System.out.println(" ");
		}
	}

	@Override
	public void showTracking() {
		for (int i = 0; i < pontos.length; i++) {
			for (int j = 0; j < pontos[i].length; j++)
				if (pontos[i][j].getStatus() != null)
					System.out.println(pontos[i][j].toString() + " " + pontos[i][j].getStatus());
		}
	}

}
