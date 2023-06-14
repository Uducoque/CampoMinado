import random

class CampoMinado:
    def __init__(self):
        self.linhas = 8
        self.colunas = 8
        self.minas = random.randint(5, 10)
        self.tabuleiro = [[0] * self.colunas for _ in range(self.linhas)]
        self.cobertura = [["-"] * self.colunas for _ in range(self.linhas)]
        self.jogadas_restantes = self.linhas * self.colunas - self.minas      
        self.gerar_minas()

    def gerar_minas(self):
        minas_plantadas = 0
        while minas_plantadas < self.minas:
            x = random.randint(0, self.linhas - 1)
            y = random.randint(0, self.colunas - 1)
            if self.tabuleiro[x][y] != -1:
                self.tabuleiro[x][y] = -1
                minas_plantadas += 1
                self.atualizar_numeros(x, y)

    def atualizar_numeros(self, x, y):
        for i in range(max(0, x - 1), min(self.linhas - 1, x + 1) + 1):
            for j in range(max(0, y - 1), min(self.colunas - 1, y + 1) + 1):
                if self.tabuleiro[i][j] != -1:
                    self.tabuleiro[i][j] += 1

    def jogar(self, x, y):
        if self.tabuleiro[x][y] == -1:
            return False
        elif self.tabuleiro[x][y] == 0:
            self.expandir_zeros(x, y)
        else:
            self.cobertura[x][y] = str(self.tabuleiro[x][y])
        self.jogadas_restantes -= 1
        return True

    def expandir_zeros(self, x, y):
        fila = [(x, y)]
        visitados = set([(x, y)])
        while fila:
            i, j = fila.pop(0)
            self.cobertura[i][j] = "0"
            for a in range(max(0, i - 1), min(self.linhas - 1, i + 1) + 1):
                for b in range(max(0, j - 1), min(self.colunas - 1, j + 1) + 1):
                    if self.tabuleiro[a][b] == 0 and (a, b) not in visitados:
                        fila.append((a, b))
                        visitados.add((a, b))
                    elif self.tabuleiro[a][b] != -1:
                        self.cobertura[a][b] = str(self.tabuleiro[a][b])
        self.jogadas_restantes -= len(visitados) - 1

    def imprimir_tabuleiro(self):
        print("  ", end="")
        for coluna in range(self.colunas):
            print(coluna, end=" ")
        print()
        for i, linha in enumerate(self.cobertura):
            print(i, end=" ")
            print(" ".join(linha))

    def imprimir_tabuleiro_com_minas(self):
        print("  ", end="")
        for coluna in range(self.colunas):
            print(coluna, end=" ")
        print()
        for i in range(self.linhas):
            print(i, end=" ")
            for j in range(self.colunas):
                if self.tabuleiro[i][j] == -1:
                    print("*", end=" ")
                else:
                    print(self.tabuleiro[i][j], end=" ")
            print()

# Exemplo de uso
jogo = CampoMinado()
jogo.imprimir_tabuleiro()

while True:
    x = int(input("Digite o número da linha: "))
    y = int(input("Digite o número da coluna: "))
    if not jogo.jogar(x, y):
        print("Você perdeu!")
        print("Tabuleiro com as minas:")
        jogo.imprimir_tabuleiro_com_minas()
        break
    jogo.imprimir_tabuleiro()
    if jogo.jogadas_restantes == 0:
        print("Você ganhou!")
        break
