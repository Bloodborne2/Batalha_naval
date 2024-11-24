import java.util.Random;
import java.util.Scanner;

public class Main {
    static Random rand = new Random();
    static Scanner ler = new Scanner(System.in);
    public static final String resetar = "\u001B[0m";
    public static final String vermelho = "\u001B[31m";
    public static final String verde = "\033[0;32m";
    public static int dificuldade = 0;
    public static int linha1;
    public static int coluna1;
    public static int tabus;
    public static int jogador01 = 0;
    public static int jogador02 = 0;
    public static int[] dimensoes;
    public static String[][][] tabuleiro;
    public static int[][][] backJogo;

    public static void main(String[] args) {
        menuDificuldade();
        int[][] validacao = new int[20][3];

        dimensoes = difi(dificuldade);
        linha1 = dimensoes[0];
        coluna1 = dimensoes[1];
        tabus = dimensoes[2];
        tabuleiro = new String[tabus][linha1][coluna1];
        backJogo = new int[tabus][linha1][coluna1];
        popularTabu(tabuleiro);
        atribuirBack(backJogo);
        System.out.println("Cada jogador tem 10 jogadas para tentar acertar os alvos. Quem somar mais pontos no final " + verde + "GANHA" + resetar + ".");

        jogarPartida(validacao);

        ganhador(jogador01, jogador02);
    }

    public static void jogarPartida(int[][] validacao) {
        for (int n = 0; n < 20; n++) {
            for (int i = 0; i < tabus; i++) {
                System.out.println("Camada " + i + ":");
                System.out.print("  ");
                for (int k = 0; k < coluna1; k++) {
                    System.out.print(" " + k + " ");
                }
                System.out.println();
                for (int j = 0; j < linha1; j++) {
                    System.out.print(j + " ");
                    for (int k = 0; k < coluna1; k++) {
                        System.out.print(tabuleiro[i][j][k]);
                    }
                    System.out.println();
                }
                System.out.println();
            }

            System.out.println("Escolha a camada de 0 a " + (tabus - 1) + ":\nUma linha e uma coluna de 0 a "
                    + (linha1 - 1) + " para atacar:");

            boolean entradaValida;
            do {
                entradaValida = false;
                int camada = ler.nextInt();
                int linha = ler.nextInt();
                int coluna = ler.nextInt();
                if (camada >= 0 && camada < tabus && linha >= 0 && linha < linha1 && coluna >= 0 && coluna < coluna1) {
                    if (validarVal(validacao, n, camada, linha, coluna)) {
                        atribuirVal(validacao, n, camada, linha, coluna);
                        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////");

                        if (n % 2 == 0) {
                            jogador1(tabuleiro, camada, linha, coluna);
                            jogador01 += backJogo[camada][linha][coluna];

                        } else {
                            jogador2(tabuleiro, camada, linha, coluna);
                            jogador02 += backJogo[camada][linha][coluna];
                        }
                    } else {
                        System.out.println("Já usada. Tente novamente.");
                        entradaValida = true;
                    }
                } else {
                    System.out.println("Posição inválida. Tente novamente.");
                    entradaValida = true;
                }
            } while (entradaValida);
        }
    }

    public static void atribuirBack(int[][][] backJogo) {
        for (int i = 0; i < tabus; i++) {
            for (int j = 0; j < linha1; j++) {
                for (int k = 0; k < coluna1; k++) {
                    backJogo[i][j][k] = rand.nextInt(-1, 3); // valores entre -1 e 2
                }
            }
        }
    }

    public static void popularTabu(String[][][] tabuleiro) {
        for (int i = 0; i < tabus; i++) {
            for (int j = 0; j < linha1; j++) {
                for (int k = 0; k < coluna1; k++) {
                    tabuleiro[i][j][k] = " * ";
                }
            }
        }
    }

    public static void menuDificuldade() {
        do {
            System.out.println("Informe a dificuldade\n1. 3 tabuleiros de 5x5\n2. 3 tabuleiros de 10x10");
            dificuldade = ler.nextInt();
        } while (dificuldade > 2 || dificuldade < 1);
        System.out.println("Você pode acertar 4 tipos de alvos, sendo:\n-1 ponto para as bombas (representado por um 0)\n0 pontos (representado por um ~)\n+1 um bom alvo (representado por um N)\n+2 o melhor acerto possível (representado por M)");
        System.out.println(verde + "BOA SORTE E BOM JOGO" + resetar);
    }

    public static int[] difi(int dificuldade) {
        int linha, coluna, tabus;
        if (dificuldade == 1) {
            linha = 5;
            coluna = 5;
            tabus = 3;
        } else {
            linha = 10;
            coluna = 10;
            tabus = 3;
        }
        return new int[]{linha, coluna, tabus};
    }

    public static void jogador1(String tabuleiro[][][], int camada, int linha, int coluna) {
        tabuleiro[camada][linha][coluna] = vermelho + acerto(backJogo, camada, linha, coluna) + resetar;
    }

    public static void jogador2(String tabuleiro[][][], int camada, int linha, int coluna) {
        tabuleiro[camada][linha][coluna] = verde + acerto(backJogo, camada, linha, coluna) + resetar;
    }

    public static boolean validarVal(int[][] validacao, int n, int camada, int linha, int coluna) {
        for (int i = 0; i < n; i++) {
            if (validacao[i][0] == camada && validacao[i][1] == linha && validacao[i][2] == coluna) {
                return false;
            }
        }
        return true;
    }

    public static void atribuirVal(int[][] validacao, int n, int camada, int linha, int coluna) {
        validacao[n][0] = camada;
        validacao[n][1] = linha;
        validacao[n][2] = coluna;
    }

    public static String acerto(int[][][] backJogo, int camada, int linha, int coluna) {
        String tipo = " ";
        if (backJogo[camada][linha][coluna] == -1) {
            tipo = " 0 ";
        } else if (backJogo[camada][linha][coluna] == 0) {
            tipo = " ~ ";
        } else if (backJogo[camada][linha][coluna] == 1) {
            tipo = " N ";
        } else if (backJogo[camada][linha][coluna] == 2) {
            tipo = " M ";
        }
        return tipo;
    }

    public static void ganhador(int jogador01, int jogador02) {
        if (jogador01 > jogador02) {
            win1();
            System.out.println("Pontuação total do Jogador 1: " + jogador01);
        } else if (jogador02 > jogador01) {
            win2();
            System.out.println("Pontuação total do Jogador 2: " + jogador02);
        } else {
            empate();
            System.out.println("Pontuação total do Jogador 1: " + jogador01);
            System.out.println("Pontuação total do Jogador 2: " + jogador02);
        }
    }

    public static void win1() {
        System.out.println("GGGGG  AAAAA  N   N  H   H  AAAAA  DDDD    OOO   RRRR    " + vermelho + "1" + resetar + "" + vermelho + "1" + resetar + "     GGGGG  AAAAA  N   N  H   H   OOO   U   U ");
        System.out.println("G      A   A  NN  N  H   H  A   A  D   D  O   O  R   R  " + vermelho + "1" + resetar + " " + vermelho + "1" + resetar + "     G      A   A  NN  N  H   H  O   O  U   U ");
        System.out.println("G  GG  AAAAA  N N N  HHHHH  AAAAA  D   D  O   O  RRRR     " + vermelho + "1" + resetar + "     G  GG  AAAAA  N N N  HHHHH  O   O  U   U ");
        System.out.println("G   G  A   A  N  NN  H   H  A   A  D   D  O   0  R  R     " + vermelho + "1" + resetar + "     G   G  A   A  N  NN  H   H  O   O  U   U ");
        System.out.println(" GGG   A   A  N   N  H   H  A   A  DDDD    OOO   R   R    " + vermelho + "1" + resetar + "      GGG   A   A  N   N  H   H   OOO   UUUUU ");
    }

    public static void win2() {
        System.out.println(" JJJJJ   OOO   GGGGG  AAAAA  DDDD    OOO   RRRR    " + verde + " 22222 " + resetar + "       GGGGG  AAAAA  N   N  H   H   OOO   U   U   !");
        System.out.println("  J     O   O  G      A   A  D   D  O   O  R   R   " + verde + " 2     2 " + resetar + "      G      A   A  NN  N  H   H  O   O  U   U   !");
        System.out.println("   J    O   O  G  GG  AAAAA  D   D  O   O  RRRR     " + verde + "    22 " + resetar + "       G  GG  AAAAA  N N N  HHHHH  O   O  U   U   !");
        System.out.println(" J  J   O   O  G   G  A   A  D   D  O   O  R  R      " + verde + "  22   " + resetar + "      G   G  A   A  N  NN  H   H  O   O  U   U   !");
        System.out.println("  JJJ    OOO    GGG   A   A  DDDD    OOO   R   R     " + verde + "222222  " + resetar + "      GGG   A   A  N   N  H   H   OOO   UUUUU   0");
    }

    public static void empate() {
        System.out.println(" EEEEE  M   M  PPPP   AAAAA  TTTTT  EEEEE ");
        System.out.println(" E      MM MM  P   P  A   A    T    E     ");
        System.out.println(" EEEE   M M M  PPPP   AAAAA    T    EEEE  ");
        System.out.println(" E      M   M  P      A   A    T    E     ");
        System.out.println(" EEEEE  M   M  P      A   A    T    EEEEE ");
    }

    public static void pontos() {
        System.out.println();
        System.out.printf("%-30s: %2d%n", "Total de pontos do jogador 1", jogador01);
        System.out.printf("%-30s: %2d%n", "Total de pontos do jogador 2", jogador02);
    }
}
