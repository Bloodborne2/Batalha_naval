import java.util.Random;
import java.util.Scanner;

public class Main {

    // Declaração de variáveis globais e constantes de cor
    static Random rand = new Random();
    static Scanner ler = new Scanner(System.in);
    public static final String resetar = "\u001B[0m";
    public static final String vermelho = "\u001B[31m";
    public static int dificuldade;
    public static void main(String[] args) {

        // Inicialização de variáveis
        int[] dimensoes;
        int linha1, coluna1, tabus;
        String tabB = "0123456789";

        // Chamando a seleção da dificuldade
        difculdade();

        dimensoes = difi(dificuldade);
        linha1 = dimensoes[0];
        coluna1 = dimensoes[1];
        tabus = dimensoes[2];

        String[][][] tabuleiro = new String[tabus][linha1][coluna1];
        int[][][] backJogo = new int[tabus][linha1][coluna1];

        // Inicialização dos tabuleiros e posicionamento dos navios
        inicializarTabuleiros(tabuleiro, backJogo, tabus, linha1, coluna1);
        navios(tabuleiro, 5,5,5,5, dificuldade);  // Posiciona 5 navios nos tabuleiros

        //troca os * por 0 e N por 1 para fazer o calculo depois (incompleto), exibirBackJogo exibi os numeros para propositos de teste
        calculoDosPontos(backJogo, tabuleiro, tabus, linha1, coluna1);
        exibirBackJogo(backJogo, tabus, linha1, coluna1);

        // Início do jogo
        System.out.println("Você tem 20 jogadas para tentar acertar os alvos");
        executarJogadas(tabuleiro, backJogo, tabB, tabus, linha1, coluna1);
    }

    public static void difculdade(){
        do {
            System.out.println("Informe a dificuldade\n1. 3 tabuleiros de 5x5\n2. 3 tabuleiros de 10x10");
            dificuldade = ler.nextInt();
        } while (dificuldade > 2 || dificuldade < 1);
    }

    // Função de configuração da dificuldade
    public static int[] difi(int dificuldade) {
        int linha, coluna, tabus = 3;

        if (dificuldade == 1) {
            linha = 5;
            coluna = 5;
        } else {
            linha = 10;
            coluna = 10;
        }

        return new int[] { linha, coluna, tabus };
    }

    // Função para inicializar os tabuleiros
    public static void inicializarTabuleiros(String[][][] tabuleiro, int[][][] backJogo, int tabus, int linha1, int coluna1) {
        for (int i = 0; i < tabus; i++) {
            for (int j = 0; j < linha1; j++) {
                for (int k = 0; k < coluna1; k++) {
                    tabuleiro[i][j][k] = " * ";
                    backJogo[i][j][k] = rand.nextInt(2) - 1;  // Gera números entre -1 e 1 pode mu
                }
            }
        }
    }

    // Função para executar jogadas
    public static void executarJogadas(String[][][] tabuleiro, int[][][] backJogo, String tabB, int tabus, int linha1, int coluna1) {
        for (int n = 0; n < 20; n++) {
            exibirTabuleiros(tabuleiro, tabB, tabus, linha1, coluna1);

            System.out.println("Escolha a camada de 0 a " + (tabus - 1) + ":\nUma linha e uma coluna de 0 a " + (linha1 - 1) + " para atacar:");
            int camada = ler.nextInt();
            int linha = ler.nextInt();
            int coluna = ler.nextInt();

            System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////");
            if (camada >= 0 && camada < tabus && linha >= 0 && linha < linha1 && coluna >= 0 && coluna < coluna1) {
                if (tabuleiro[camada][linha][coluna].equals(" N ")) {
                    System.out.println("Você acertou um navio!");
                }
                tabuleiro[camada][linha][coluna] = vermelho + " * " + resetar;
            } else {
                System.out.println("Posição inválida. Tente novamente.");
            }
        }
    }

    // Função para exibir os tabuleiros
    public static void exibirTabuleiros(String[][][] tabuleiro, String tabB, int tabus, int linha1, int coluna1) {
        for (int i = 0; i < tabus; i++) {
            System.out.println("Camada " + i + ":");
            System.out.print("   ");
            for (int k = 0; k < coluna1; k++) {
                System.out.print(" " + tabB.charAt(k) + " ");
            }
            System.out.println();
            for (int j = 0; j < linha1; j++) {
                System.out.print(" " + tabB.charAt(j) + " ");
                for (int k = 0; k < coluna1; k++) {
                    System.out.print(tabuleiro[i][j][k]);
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    // Função para posicionar navios no tabuleiro
    public static void navios(String[][][] tabuleiro, int naviosTotais, int bombasTotais, int naviosGrandeTotais, int suprimentosTotais, int dificuldade) {
        int naviosNoTabuleiro = 0;
        int bombasNoTabuleiro = 0;
        int naviosGrandeNoTabuleiro = 0;
        int suprimentosNoTabuleiro = 0;
        int linha, coluna, camada;
        String[] tipos = {" S ", " B ", " N ", " n "};

        while (naviosNoTabuleiro < naviosTotais || bombasNoTabuleiro < bombasTotais || naviosGrandeNoTabuleiro < naviosGrandeTotais || suprimentosNoTabuleiro < suprimentosTotais) {
            camada = rand.nextInt(3);  // Escolhe uma camada aleatória sd
            if (dificuldade == 1) {
                linha = rand.nextInt(5);
                coluna = rand.nextInt(5);
            } else {
                linha = rand.nextInt(10);
                coluna = rand.nextInt(10);
            }

            if (tabuleiro[camada][linha][coluna].equals(" * ")) {
                // Determina o que colocar, se necessário
                if (naviosNoTabuleiro < naviosTotais) {
                    tabuleiro[camada][linha][coluna] = " n "; // Coloca um navio
                    naviosNoTabuleiro++;
                } else if (bombasNoTabuleiro < bombasTotais) {
                    tabuleiro[camada][linha][coluna] = " B "; // Coloca uma bomba
                    bombasNoTabuleiro++;
                } else if (naviosGrandeNoTabuleiro < naviosGrandeTotais) {
                    tabuleiro[camada][linha][coluna] = " N "; // Coloca um navio grande
                    naviosGrandeNoTabuleiro++;
                } else if (suprimentosNoTabuleiro < suprimentosTotais) {
                    tabuleiro[camada][linha][coluna] = " S "; // Coloca suprimentos
                    suprimentosNoTabuleiro++;
                }
            }
        }
    }

    //troca a String por numeros nos valores especificos
    public static int[][][] calculoDosPontos (int[][][] backJogo, String[][][] tabuleiro, int tabus, int linha1, int coluna1) {
        for (int i = 0; i < tabus; i++) {
            for (int j = 0; j < linha1; j++) {
                for (int k = 0; k < coluna1; k++) {
                    if (tabuleiro[i][j][k].equals(" * ")) {
                        backJogo[i][j][k] = 0;
                    }
                    if (tabuleiro[i][j][k].equals(" n ")) {
                        backJogo[i][j][k] = 1;
                    }
                    if (tabuleiro[i][j][k].equals(" N ")) {
                        backJogo[i][j][k] = 1;
                    }
                    if (tabuleiro[i][j][k].equals(" S ")) {
                        backJogo[i][j][k] = 4;
                    }
                    if (tabuleiro[i][j][k].equals(" B ")) {
                        backJogo[i][j][k] = -3;
                    }

                }
            }
        }
        return backJogo;
    }


    //imprimi imprimindo a função calcular pontos para propositos de teste
    public static void exibirBackJogo(int[][][] backJogo, int tabus, int linha1, int coluna1) {
        for (int i = 0; i < tabus; i++) {
            System.out.println("Camada " + i + ":");
            for (int j = 0; j < linha1; j++) {
                for (int k = 0; k < coluna1; k++) {
                    System.out.print(backJogo[i][j][k] + " ");  // Imprime o valor na posição [i][j][k]
                }
                System.out.println();  // Nova linha após imprimir uma linha do tabuleiro
            }
            System.out.println();  // Nova linha após imprimir uma camada
        }
    }
}
