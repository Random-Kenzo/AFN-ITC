//Nome: Guilherme Kenzo Silva Oshiro              NUSP:11314988

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.FileWriter;

public class afn{
  public static void criarArquivo(){                            //método para criar o arquivo de saida.
    try {
      File myObj = new File("saida.txt");                       //cria arquivo de saida chamdo saida.txt, se ele já existe fala para o usúario que ele será reescrito.
      if (myObj.createNewFile()) {
        System.out.println("arquivo de saida criado");
      } else {
        System.out.println("Ja existe arquivo com nome saida.txt, ele sera reescrito com os novos resultados.");
      }
    } catch (IOException e) {
      System.out.println("Error.");
      e.printStackTrace();
    }
  }

  public static void stringToArray (String line, ArrayList <Integer> cadeia) { //metodo para converter String em array de inteiros, usado para auxiliar na leitura de cadeias.
    String split [] = line.split(" ");
    for (int i = 0; i < split.length; i++) cadeia.add(Integer.parseInt(split[i]));
  }

  public static int testaCadeia (ArrayList <Integer> caux, int t, int q0, int [][] tra, List <Integer> f, int a){//metodo para verificar se a cadeia é valida.
    if(a == 0) return 0;                                //se o número de aceitação do afn for 0 a cadeia é automaticamente negada.

    int [][] matriAux = new int [caux.size()+1][20];    //cria uma matriz, o conceito desse metodo é simular a resolução de uma afn como se fosse uma matriz, no caso a primeira linha é o estado inicial e apartir 
                                                        //dela são adicionados nas colunas próximas dela (na mesma linha) as transições de cadeia vazia, e nas linhas subsequentes as transições.
    int l, h, m;                                        //aqui são unicializadas variáveis para auxiliar o programa, no caso, ints para os iteradores.
    for(l=0; l<=caux.size(); l++) for(h=0; h<=19; h++) matriAux [l][h] = -1; //o programa então coloca -1 em todas as casas da matriz, isso vai ser usado para o nosso programa entender quando a linha acabou com 
                                                        //posições validas.
    matriAux[0][0] = q0;                                //adicionamos o estado inicial
    int aux = 1;                                        //aux=1, isso será usado para adicionarmos todas as transições de cadeia vazia em casas da matriz apartir da primeira coluna livre (já que a matriAux[0][0] 
                                                        //está o estado inicial, a posição [0][aux], onde aux=1, é a próxima posição valida).
    h = 0;                                              //h será usado para passar o nosso laço, o próximo while, este serve para adicionarmos as cadeias vazias do estado inicial.
    while(matriAux[0][h] != -1){                        //enquanto a posição [0][h] da matriz for valida (começando em q0 e progredindo), vamos inserindo as demais transições de cadeia vazia.
      for(m=0; m<t; m++){                               //esse for verifica todas as possiveis transições apartir da posição na matriz, então ele vai iterar nas m possiveis transições do automato.
        if((matriAux[0][h] == tra[m][0]) && tra[m][1] == 0){//caso tenhamos uma transição valida, adicionamos ela na matriz.
          matriAux[0][aux] = tra[m][2];
          aux++;
        }
      }
      h++;
    }

    for(l=0; l<caux.size(); l++){                       //laço principal, responsável por adicionar as demais transições na matriz.
      aux = 0;
      h = 0;
      while(matriAux[l][h] != -1){                      //verifica possiveis transições, com exceção das transições de cadeia vazia. Ele funciona similar ao primeiro while, porém as transições são adicionadas na
        int i;                                          //subsequente. 
        
        for(i=0; i<t; i++){
          if((tra[i][0] == matriAux[l][h]) && (tra[i][1] == caux.get(l))){
            matriAux[l+1][aux] = tra[i][2];             //no caso usamos l+1 porque queremos adicionar na linha subsequente e não na atual.
            aux++;                                      //o aux será importante até para depois desse laço acabar.
          }
        }
        h++;
      }
      int n=0;
      while(matriAux[l+1][n] != -1){                    //agora adicionamos as transições de cadeia vazia possiveis apartir das transições que acabamos de adicionar. O aux guarda a proxima posição valida naquela
        int j;                                          //linha apartir do ultimo laço.

        for(j=0; j<t; j++){
          if((tra[j][0] == matriAux[l+1][n]) && (tra[j][1] == 0)){
            matriAux[l+1][aux] = tra[j][2];
            aux++;
          }
        }
        n++;
      }
    }
    h = 0;

    while(matriAux[caux.size()][h] != -1){              //esse último while vai verificar se existe algum estado de aceitação dentro da última linha da matriz.
      for(int j =0; j<a; j++){
        if(matriAux[caux.size()][h] == f.get(j)) return 1;
      }
      
      h++;
    }

    return 0;                                           //caso não seja encontrada, esssa cadeia é invalida.
  }

  public static void main (String[] args){

    int m, q, s, t, q0, a, i, nct;                      //inicialização das variáveis.
    String cadeia;
    criarArquivo();                                     //chamada de metodo para criar o arquivo de saida.

    try {
      File arquivo = new File ("entrada.txt");          //armazena o arquivo de entrada.
      Scanner sc = new Scanner(arquivo);                //scanner que le o arquivo.
      FileWriter myWriter = new FileWriter("saida.txt");//writter que escreve no arquivo de saida.

      m= sc.nextInt();

      for(i =0; i<m; i++){                              //for com número m de automatos de teste.
        q = sc.nextInt();                               //leitura do bloco de linhas com a especificações do automato i: q = quantidade de estados do autômato.
        s = sc.nextInt();                               //s = quantidade de simbolos, incluindo a cadeia vazia.
        t = sc.nextInt();                               //t = quantidade de transições do AFN.
        q0= sc.nextInt();                               //q0 = índice do estado inicial.
        a = sc.nextInt();                               //a =número de estados de aceitação do AFN.

        List<Integer> f = new ArrayList<>();            // lista onde será armazenado os índices dos estados de aceitação.
        int j =0;
        while (j <= a-1){                               //while para aramazenar na lista os j estados de aceitação.
          f.add(sc.nextInt());
          j++;
        }

        int k;
        int [][] tra = new int [t][3];                  //matriz para armazenar as transições, com t transições, onde t0, t1 e t2, representam transição do estado t0 para o estado t2 com o símbolo t1
        for(k = 0; k < t; k++){                         // for para realizar a leitura das especificações de transição.
          j=0;
          while(j < 3){                                 //for = t transições, ele vai passar pelas linhas da entrada; e while = espécificações, ele vai ler os 3 números de cada linha.
            tra [k][j] = sc.nextInt();
            j++;
          }
        }

        nct = sc.nextInt();                             //número de cadeias de teste.
        cadeia = sc.nextLine();                         //usado para ler a quebra de linha, para que nós próximas leituras pegue as linhas.
        j =0;
        for(j=0; j<nct; j++){
          cadeia = sc.nextLine();                       //armazena os valores da cadeia.
          ArrayList<Integer> caux = new ArrayList<>();  //variavel auxiliar para armazenar um array de inteiros.
          stringToArray(cadeia, caux);                  //chamada de metodo para transformar string em array de inteiros.

          int teste = testaCadeia(caux, t, q0, tra, f, a);//chamada para testar a cadeia.
          myWriter.write(teste + " ");                  //escreve no arquivo de saida o resultado do teste.
        }

        myWriter.write("\n");                           //quebra de linha para o próximo automato.
      }
      System.out.println("Processo concluido.");

      myWriter.close();
      sc.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}