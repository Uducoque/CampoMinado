package blackjack;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CampoMinado {
    private int linhas;
    private int colunas;
    private int minas;
    private int[][] tabuleiro;
    private String[][] cobertura;
    private int jogadas_restantes;

    public CampoMinado() {
        this.linhas = 8;
        this.colunas = 8;
        this.minas = new Random().nextInt(6) + 5;
        this.tabuleiro = new int[this.linhas][this.colunas];
        this.cobertura = new String[this.linhas][this.colunas];
        this.jogadas_restantes = this.linhas * this.colunas - this.minas;
        gerarMinas();
        inicializarCobertura();
    }

    private void gerarMinas() {
        int minas_plantadas = 0;
        Random random = new Random();
        while (minas_plantadas < this.minas) {
            int x = random.nextInt(this.linhas);
            int y = random.nextInt(this.colunas);
            if (this.tabuleiro[x][y] != -1) {
                this.tabuleiro[x][y] = -1;
                minas_plantadas++;
                atualizarNumeros(x, y);
            }
        }
    }

    private void atualizarNumeros(int x, int y) {
        for (int i = Math.max(0, x - 1); i <= Math.min(this.linhas - 1, x + 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(this.colunas - 1, y + 1); j++) {
                if (this.tabuleiro[i][j] != -1) {
                    this.tabuleiro[i][j]++;
                }
            }
        }
    }

    public boolean jogar(int x, int y) {
        if (this.tabuleiro[x][y] == -1) {
            return false;
        } else if (this.tabuleiro[x][y] == 0) {
            expandirZeros(x, y);
        } else {
            this.cobertura[x][y] = String.valueOf(this.tabuleiro[x][y]);
        }
        this.jogadas_restantes--;
        return true;
    }

    private void expandirZeros(int x, int y) {
        List<int[]> fila = new ArrayList<>();
        fila.add(new int[]{x, y});
        boolean[][] visitados = new boolean[this.linhas][this.colunas];
        visitados[x][y] = true;

        while (!fila.isEmpty()) {
            int[] posicao = fila.remove(0);
            int i = posicao[0];
            int j = posicao[1];
            this.cobertura[i][j] = "0";

            for (int a = Math.max(0, i - 1); a <= Math.min(this.linhas - 1, i + 1); a++) {
                for (int b = Math.max(0, j - 1); b <= Math.min(this.colunas - 1, j + 1); b++) {
                    if (this.tabuleiro[a][b] == 0 && !visitados[a][b]) {
                        fila.add(new int[]{a, b});
                        visitados[a][b] = true;
                    } else if (this.tabuleiro[a][b] != -1) {
                        this.cobertura[a][b] = String.valueOf(this.tabuleiro[a][b]);
                    }
                }
            }
        }
        this.jogadas_restantes -= getNumeroVisitados(visitados) - 1;
    }

    private int getNumeroVisitados(boolean[][] visitados) {
        int count = 0;
        for (boolean[] linha : visitados) {
            for (boolean visitado : linha) {
                if (visitado) {
                    count++;
                }
            }
        }
        return count;
    }

    public void imprimirTabuleiro() {
        System.out.print("  ");
        for (int coluna = 0; coluna < this.colunas; coluna++) {
            System.out.print(coluna + " ");
        }
        System.out.println();
        for (int i = 0; i < this.linhas; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < this.colunas; j++) {
                System.out.print(this.cobertura[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void imprimirTabuleiroComMinas() {
        System.out.print("  ");
        for (int coluna = 0; coluna < this.colunas; coluna++) {
            System.out.print(coluna + " ");
        }
        System.out.println();
        for (int i = 0; i < this.linhas; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < this.colunas; j++) {
                if (this.tabuleiro[i][j] == -1) {
                    System.out.print("* ");
                } else {
                    System.out.print(this.tabuleiro[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private void inicializarCobertura() {
        for (int i = 0; i < this.linhas; i++) {
            for (int j = 0; j < this.colunas; j++) {
                this.cobertura[i][j] = "-";
            }
        }
    }
    
    public static void main(String[] args) {
        CampoMinado jogo = new CampoMinado();
        jogo.imprimirTabuleiro();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Digite o número da linha: ");
            int x = scanner.nextInt();
            System.out.print("Digite o número da coluna: ");
            int y = scanner.nextInt();
            if (!jogo.jogar(x, y)) {
                System.out.println("Você perdeu!");
                System.out.println("Tabuleiro com as minas:");
                jogo.imprimirTabuleiroComMinas();
                break;
            }
            jogo.imprimirTabuleiro();
            if (jogo.jogadas_restantes == 0) {
                System.out.println("Você ganhou!");
                break;
            }
        }
    }
}
