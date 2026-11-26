# 🅿️ Sistema de Gestão de Parque de Estacionamento

![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue)
![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql&logoColor=white)
![Status](https://img.shields.io/badge/Projeto-Acad%C3%A9mico-green)

Sistema de gestão de um parque de estacionamento com interface gráfica em **Java Swing**, ligado a uma base de dados **MySQL**. Permite controlar a entrada e saída de veículos, gerir vagas, manter uma fila de espera persistente, emitir faturas de pagamento e acompanhar o estado do parque em tempo real.

O projeto foi desenvolvido no âmbito da disciplina de Estrutura de Dados e Algoritmos, no 2.º ano, com foco em **estruturas de dados (fila e pilha)** aplicadas a um problema real.

---

## 📑 Índice

1. [Problema identificado](#-problema-identificado)
2. [Solução aplicada](#-solução-aplicada)
3. [Stack utilizada](#-stack-utilizada)
4. [Regras de negócio](#-regras-de-negócio)
5. [Como funciona o sistema](#-como-funciona-o-sistema)
6. [Instalação e configuração](#-instalação-e-configuração)
7. [Base de dados](#-base-de-dados)
8. [Compilar e executar](#-compilar-e-executar)
9. [Testes importantes](#-testes-importantes)
10. [Estruturas de dados aplicadas](#-estruturas-de-dados-aplicadas)
11. [Observações finais](#-observações-finais)
12. [Autor](#-autor)

---

## ❗ Problema identificado

Os parques de estacionamento geridos manualmente enfrentam vários problemas:

- Falta de controlo sobre quantas vagas estão livres ou ocupadas.
- Nenhum registo de quem entrou, quando entrou e quanto deve pagar.
- Ausência de histórico de operações para auditoria.
- Clientes esperam sem qualquer organização quando o parque está cheio.
- Cálculo de preços feito "a olho", gerando erros e prejuízo.
- Perda total de informação (fila, bilhetes, faturas) quando o sistema fecha ou há falha de energia.

---

## ✅ Solução aplicada

O sistema resolve os problemas acima com os seguintes elementos:

- **Interface gráfica** em Java Swing, dividida em cinco abas: *Parque*, *Clientes*, *Veículos*, *Faturas* e *Financeiro*.
- **Base de dados MySQL** que guarda todos os dados de forma permanente.
- **Fila de espera (FIFO)** persistida na base de dados, garantindo que nenhum cliente perde o lugar ao reiniciar o programa.
- **Pilha de operações (LIFO)** para consulta rápida das ações mais recentes.
- **Emissão automática** de bilhete na entrada e de fatura na saída.
- **Cálculo de preço** por tempo exato de permanência (100 Mts por hora).
- **Promoção automática da fila** quando uma vaga de tipo compatível fica livre.
- **Estados visuais das vagas** (livre, ocupada, manutenção) desenhados na interface.

---

## 🛠️ Stack utilizada

| Componente         | Tecnologia                                         |
| ------------------ | -------------------------------------------------- |
| Linguagem          | Java 17 ou superior                                |
| Interface gráfica  | Java Swing                                         |
| Base de dados      | MySQL 8                                            |
| Conector           | MySQL Connector/J 8.x                              |
| IDE recomendada    | IntelliJ IDEA, Eclipse ou VS Code (extensão Java)  |
| Cliente SQL        | DBeaver ou MySQL Workbench                         |

---

## 📋 Regras de negócio

### Entrada de veículo

1. O sistema recebe a matrícula e procura o veículo na base de dados.
2. Se o veículo não existir, a operação é recusada com aviso.
3. Se o veículo já tiver um bilhete aberto, a operação é recusada (não pode estar duas vezes dentro do parque).
4. O sistema procura uma vaga livre compatível com o tipo do veículo (`MOTO`, `LIGEIRO`, `PESADO` ou `TODOS`).
5. Se houver vaga, é emitido um bilhete com código único, o veículo entra e a vaga passa ao estado `OCUPADA`.
6. Se não houver vaga, o veículo entra na fila de espera e é avisado da posição em que ficou.

### Saída de veículo

1. O gestor introduz a matrícula.
2. O sistema calcula o tempo de permanência em horas, com precisão ao minuto.
3. O valor é calculado como: `horas × 100 Mts`.
4. É mostrada a fatura ao gestor, que confirma ou cancela o pagamento.
5. Ao confirmar, o bilhete é fechado, a fatura é emitida e a vaga fica `LIVRE`.
6. O sistema procura na fila o primeiro veículo compatível com o tipo da vaga libertada.
7. Se existir, esse veículo entra automaticamente na vaga, é-lhe emitido um novo bilhete e o gestor é notificado.
8. Se não existir, a vaga fica simplesmente livre.

### Fila de espera

- Funciona por ordem de chegada (**FIFO**).
- É persistida na base de dados, mantendo-se intacta após reinício do programa.
- Cada veículo na fila tem uma posição (1, 2, 3, ...).
- Quando um veículo entra no parque, é removido da fila.
- O gestor pode cancelar manualmente a espera de qualquer veículo (por exemplo, se o cliente desistir).

### Pilha de operações

- Guarda as operações mais recentes (entradas, saídas, movimentos da fila e pagamentos).
- Segue o princípio **LIFO**: a mais recente é consultada primeiro.
- É usada para o gestor ver o histórico da sessão em curso.

### Estados da vaga

| Estado       | Significado                       |
| ------------ | --------------------------------- |
| `LIVRE`      | Disponível para entrada.          |
| `OCUPADA`    | Tem um veículo estacionado.       |
| `MANUTENCAO` | Indisponível para uso.            |

O gestor pode alternar entre `LIVRE` e `MANUTENCAO` com o **botão direito do rato** na vaga.

### Tarifação

- Valor fixo por hora: **100 Mts**.
- O tempo é calculado com base na diferença entre a hora de saída e a hora de entrada, em horas decimais.

| Permanência        | Valor a pagar |
| ------------------ | ------------- |
| 1 hora             | 100 Mts       |
| 1 hora e 30 min    | 150 Mts       |
| 45 minutos         | 75 Mts        |

### Fatura

- É emitida sempre que um bilhete é fechado.
- Contém: número sequencial, bilhete associado, horas, preço por hora e total.
- Fica gravada na base de dados para consulta posterior.

---

## ⚙️ Como funciona o sistema

### Estrutura de pastas

```text
ProjetoParque/
├── Main.java
├── lib/
│   └── mysql-connector-j-8.x.jar
├── util/
│   └── Conexao.java
├── model/
│   ├── Proprietario.java
│   ├── Veiculo.java
│   ├── Vaga.java
│   ├── Bilhete.java
│   ├── Operacao.java
│   └── Fatura.java
├── dao/
│   ├── ProprietarioDAO.java
│   ├── VeiculoDAO.java
│   ├── VagaDAO.java
│   ├── BilheteDAO.java
│   ├── OperacaoDAO.java
│   ├── FaturaDAO.java
│   └── FilaEsperaDAO.java
├── service/
│   ├── FilaEspera.java
│   ├── PilhaOperacoes.java
│   └── ParqueService.java
├── ui/
│   ├── JanelaPrincipal.java
│   ├── PainelParque.java
│   ├── PainelClientes.java
│   ├── PainelVeiculos.java
│   ├── PainelFaturas.java
│   ├── PainelFinanceiro.java
│   └── CartaoVaga.java
└── uihelpers/
    ├── FachadaBilhetes.java
    ├── FachadaVeiculos.java
    └── FachadaProprietarios.java
```

### Camadas

| Camada      | Responsabilidade                                              |
| ----------- | ------------------------------------------------------------- |
| **Model**   | Classes que representam os dados (`Veiculo`, `Bilhete`, etc.) |
| **DAO**     | Acesso à base de dados (consultas e gravações)                |
| **Service** | Regras de negócio (entrada, saída, fila, pilha)               |
| **UI**      | Interface gráfica (Swing)                                     |
| **Util**    | Ligação à base de dados                                       |

### Fluxo de uma entrada

1. O gestor escreve a matrícula na aba **Parque** e clica em **Entrada**.
2. O *Service* procura o veículo, verifica se não tem bilhete aberto e procura vaga livre.
3. Se houver vaga, o DAO insere o bilhete na base de dados.
4. Um *trigger* da base de dados gera o código do bilhete e marca a vaga como `OCUPADA`.
5. A interface atualiza a grade de vagas e o contador de lotação.

### Fluxo de uma saída

1. O gestor escreve a matrícula e clica em **Saída**.
2. O sistema mostra a fatura com horas e valor.
3. Ao confirmar, o bilhete é fechado e a fatura é emitida.
4. A vaga fica `LIVRE`.
5. O sistema promove automaticamente o próximo veículo compatível da fila.
6. Se promoveu alguém, o gestor é notificado.

---

## Instalação e configuração

### 1. Clonar o projeto

```bash
git clone https://github.com/GotaGames/ProjetoParque.git
cd ProjetoParque
```

Depois, abre o projeto na tua IDE.

### 2. Adicionar o MySQL Connector

1. Descarrega o ficheiro `mysql-connector-j-8.x.jar` do [site oficial do MySQL](https://dev.mysql.com/downloads/connector/j/) (ou usa o que já está na pasta `lib/` do projeto).
2. Cria a pasta `lib/` na raiz do projeto (se ainda não existir).
3. Coloca o `.jar` dentro da pasta `lib/`.

<details>
<summary><b>IntelliJ IDEA</b></summary>

1. `File` > `Project Structure` > `Modules` > `Dependencies`.
2. Clica em `+` > `JARs or directories`.
3. Escolhe `lib/mysql-connector-j-8.x.jar`.
4. Aplica e fecha.

</details>

<details>
<summary><b>Eclipse</b></summary>

1. Botão direito no projeto > `Build Path` > `Configure Build Path`.
2. Separador `Libraries` > `Add JARs`.
3. Escolhe o ficheiro dentro de `lib/`.
4. Aplica e fecha.

</details>

<details>
<summary><b>VS Code</b></summary>

1. Cria a pasta `.vscode/` na raiz do projeto.
2. Cria o ficheiro `settings.json` com o seguinte conteúdo:

```json
{
  "java.project.referencedLibraries": ["lib/**/*.jar"]
}
```

</details>

### 3. Configurar a ligação à base de dados

Abre `util/Conexao.java` e ajusta os três campos:

```java
private static final String URL  = "jdbc:mysql://localhost:3306/parque_estacionamento?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASS = "";
```
---

## Base de dados

1. Abre o **DBeaver** (ou MySQL Workbench).
2. Cria uma nova ligação ao MySQL local (utilizador `root`).
3. Abre o **SQL Editor**.
4. Cola e executa os scripts abaixo, **pela ordem apresentada**.

> No DBeaver, executa os scripts com **Alt + X** ("Execute SQL Script") para que as instruções `DELIMITER` das funções e dos triggers sejam interpretadas corretamente.

### Passo 1 — Criar a base de dados e as tabelas

```sql
CREATE DATABASE IF NOT EXISTS parque_estacionamento
  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE parque_estacionamento;

CREATE TABLE Proprietario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    bi VARCHAR(20) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(120)
);

CREATE TABLE Veiculo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    matricula VARCHAR(15) NOT NULL UNIQUE,
    marca VARCHAR(60),
    modelo VARCHAR(60),
    cor VARCHAR(30),
    tipo ENUM('MOTO','LIGEIRO','PESADO') NOT NULL,
    proprietario_id INT NOT NULL,
    FOREIGN KEY (proprietario_id) REFERENCES Proprietario(id)
);

CREATE TABLE Vaga (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(10) NOT NULL UNIQUE,
    zona VARCHAR(5),
    tipo_aceite ENUM('MOTO','LIGEIRO','PESADO','TODOS') DEFAULT 'TODOS',
    estado ENUM('LIVRE','OCUPADA','MANUTENCAO') DEFAULT 'LIVRE',
    veiculo_id INT NULL,
    FOREIGN KEY (veiculo_id) REFERENCES Veiculo(id)
);

CREATE TABLE Tarifa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_veiculo ENUM('MOTO','LIGEIRO','PESADO') NOT NULL,
    valor_primeira_hora DECIMAL(10,2) NOT NULL,
    valor_hora_extra DECIMAL(10,2) NOT NULL
);

CREATE TABLE Bilhete (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(40) NULL UNIQUE,
    veiculo_id INT NOT NULL,
    vaga_id INT NOT NULL,
    data_hora_entrada DATETIME NOT NULL,
    data_hora_saida DATETIME NULL,
    valor_pago DECIMAL(10,2) NULL,
    estado ENUM('ABERTO','FECHADO') DEFAULT 'ABERTO',
    FOREIGN KEY (veiculo_id) REFERENCES Veiculo(id),
    FOREIGN KEY (vaga_id) REFERENCES Vaga(id)
);

CREATE TABLE FilaEspera (
    id INT AUTO_INCREMENT PRIMARY KEY,
    veiculo_id INT NOT NULL,
    posicao INT NOT NULL,
    chegou_em DATETIME NOT NULL,
    FOREIGN KEY (veiculo_id) REFERENCES Veiculo(id)
);

CREATE TABLE Operacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('ENTRADA','SAIDA','FILA_ENTRADA','FILA_SAIDA','PAGAMENTO') NOT NULL,
    descricao VARCHAR(255),
    matricula VARCHAR(15),
    ocorrido_em DATETIME NOT NULL
);

CREATE TABLE Fatura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bilhete_id INT NOT NULL UNIQUE,
    numero VARCHAR(30) NOT NULL UNIQUE,
    horas DECIMAL(10,2) NOT NULL,
    preco_hora DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    emitida_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bilhete_id) REFERENCES Bilhete(id)
);
```

### Passo 2 — Funções

```sql
DELIMITER //

CREATE FUNCTION vagas_livres() RETURNS INT
READS SQL DATA
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total FROM Vaga WHERE estado = 'LIVRE';
    RETURN total;
END//

CREATE FUNCTION calcular_valor(p_tipo VARCHAR(10), p_horas DECIMAL(10,2))
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    RETURN ROUND(p_horas * 100.00, 2);
END//

DELIMITER ;
```

### Passo 3 — Triggers

```sql
DELIMITER //

-- Gera o código único do bilhete, caso não seja fornecido
CREATE TRIGGER trg_gera_codigo_bilhete
BEFORE INSERT ON Bilhete
FOR EACH ROW
BEGIN
    IF NEW.codigo IS NULL THEN
        SET NEW.codigo = CONCAT('BLH-', DATE_FORMAT(NOW(),'%Y%m%d'), '-',
                                UPPER(SUBSTRING(REPLACE(UUID(),'-',''), 1, 8)));
    END IF;
END//

-- Marca a vaga como OCUPADA quando um bilhete é aberto
CREATE TRIGGER trg_bilhete_ocupa_vaga
AFTER INSERT ON Bilhete
FOR EACH ROW
BEGIN
    IF NEW.estado = 'ABERTO' THEN
        UPDATE Vaga SET estado='OCUPADA', veiculo_id=NEW.veiculo_id
        WHERE id = NEW.vaga_id;
    END IF;
END//

-- Liberta a vaga quando o bilhete é fechado
CREATE TRIGGER trg_bilhete_liberta_vaga
AFTER UPDATE ON Bilhete
FOR EACH ROW
BEGIN
    IF OLD.estado='ABERTO' AND NEW.estado='FECHADO' THEN
        UPDATE Vaga SET estado='LIVRE', veiculo_id=NULL
        WHERE id = NEW.vaga_id;
    END IF;
END//

-- Regista a operação de entrada
CREATE TRIGGER trg_op_entrada
AFTER INSERT ON Bilhete
FOR EACH ROW
BEGIN
    INSERT INTO Operacao (tipo, descricao, matricula, ocorrido_em)
    SELECT 'ENTRADA', CONCAT('Entrada na vaga ', v.numero),
           ve.matricula, NEW.data_hora_entrada
    FROM Vaga v, Veiculo ve
    WHERE v.id = NEW.vaga_id AND ve.id = NEW.veiculo_id;
END//

-- Regista a operação de saída
CREATE TRIGGER trg_op_saida
AFTER UPDATE ON Bilhete
FOR EACH ROW
BEGIN
    IF OLD.estado='ABERTO' AND NEW.estado='FECHADO' THEN
        INSERT INTO Operacao (tipo, descricao, matricula, ocorrido_em)
        SELECT 'SAIDA', CONCAT('Saída. Valor pago: ', NEW.valor_pago),
               ve.matricula, NEW.data_hora_saida
        FROM Veiculo ve WHERE ve.id = NEW.veiculo_id;
    END IF;
END//

DELIMITER ;
```

### Passo 4 — Dados iniciais

```sql
INSERT INTO Proprietario (nome, bi, telefone, email) VALUES
('Ana Silva',        '123456789A', '840000001', 'ana.silva@mail.com'),
('Bruno Costa',      '987654321B', '840000002', 'bruno.costa@mail.com'),
('Carla Mendes',     '111222333C', '840000003', 'carla.mendes@mail.com'),
('David Macuácua',   '444555666D', '840000004', 'david.macuacua@mail.com'),
('Elsa Nhampossa',   '777888999E', '840000005', 'elsa.nhampossa@mail.com'),
('Fábio Sitoe',      '222333444F', '840000006', 'fabio.sitoe@mail.com'),
('Gabriela Tembe',   '555666777G', '840000007', 'gabriela.tembe@mail.com'),
('Hélder Cossa',     '888999000H', '840000008', 'helder.cossa@mail.com');

INSERT INTO Veiculo (matricula, marca, modelo, cor, tipo, proprietario_id) VALUES
('AAA-111-MC', 'Toyota',     'Corolla', 'Branco',   'LIGEIRO', 1),
('BBB-222-MC', 'Honda',      'PCX',     'Preto',    'MOTO',    2),
('CCC-333-MC', 'Nissan',     'Navara',  'Cinza',    'LIGEIRO', 3),
('DDD-444-MC', 'Yamaha',     'NMAX',    'Azul',     'MOTO',    4),
('EEE-555-MC', 'Mitsubishi', 'Pajero',  'Vermelho', 'LIGEIRO', 5),
('FFF-666-MC', 'Volvo',      'FH16',    'Branco',   'PESADO',  6),
('GGG-777-MC', 'BMW',        'X5',      'Preto',    'LIGEIRO', 7),
('HHH-888-MC', 'Suzuki',     'Burgman', 'Cinza',    'MOTO',    8);

INSERT INTO Vaga (numero, zona, tipo_aceite) VALUES
('A-01','A','LIGEIRO'),('A-02','A','LIGEIRO'),('A-03','A','LIGEIRO'),
('A-04','A','LIGEIRO'),('A-05','A','LIGEIRO'),
('B-01','B','MOTO'),('B-02','B','MOTO'),('B-03','B','MOTO'),
('B-04','B','MOTO'),('B-05','B','MOTO'),
('C-01','C','PESADO'),('C-02','C','PESADO'),
('C-03','C','PESADO'),('C-04','C','PESADO'),
('D-01','D','TODOS'),('D-02','D','TODOS'),('D-03','D','TODOS'),
('D-04','D','TODOS'),('D-05','D','TODOS');

INSERT INTO Tarifa (tipo_veiculo, valor_primeira_hora, valor_hora_extra) VALUES
('MOTO', 100.00, 100.00),('LIGEIRO', 100.00, 100.00),('PESADO', 100.00, 100.00);
```

### Resumo das vagas iniciais

| Zona | Vagas       | Tipo aceite | Quantidade |
| ---- | ----------- | ----------- | ---------- |
| A    | A-01 a A-05 | `LIGEIRO`   | 5          |
| B    | B-01 a B-05 | `MOTO`      | 5          |
| C    | C-01 a C-04 | `PESADO`    | 4          |
| D    | D-01 a D-05 | `TODOS`     | 5          |

---

## Compilar e executar

### Linha de comandos — Windows

```bat
javac -cp "lib/mysql-connector-j-8.x.jar" -d . *.java util/*.java model/*.java dao/*.java service/*.java ui/*.java uihelpers/*.java
java -cp ".;lib/mysql-connector-j-8.x.jar" Main
```

### Linha de comandos — Linux ou macOS

```bash
javac -cp "lib/mysql-connector-j-8.x.jar" -d . *.java util/*.java model/*.java dao/*.java service/*.java ui/*.java uihelpers/*.java
java -cp ".:lib/mysql-connector-j-8.x.jar" Main
```

### Pela IDE

| IDE            | Como executar                                          |
| -------------- | ------------------------------------------------------ |
| IntelliJ IDEA  | Botão direito em `Main.java` > `Run 'Main.main()'`     |
| Eclipse        | Botão direito em `Main.java` > `Run As` > `Java Application` |
| VS Code        | Abre `Main.java` e clica em **Run** acima do método `main` |

---

## Testes importantes

Segue estes testes pela ordem para validar que tudo funciona corretamente.

> **Dica para os testes de fila (12.3, 12.5 e 12.6):** as vagas da zona **D** aceitam qualquer tipo de veículo (`TODOS`). Para simular um parque cheio com menos veículos, coloca primeiro as vagas D-01 a D-05 em **manutenção** (botão direito do rato em cada vaga livre).

### 1. Entrada simples

- [ ] Na aba **Parque**, escreve a matrícula `AAA-111-MC`.
- [ ] Clica em **Entrada**.

**Resultado esperado:** popup com o bilhete e a vaga `A-01`; a vaga aparece ocupada na grade; a lotação aumenta.

### 2. Entrada duplicada

- [ ] Com `AAA-111-MC` já dentro, clica em **Entrada** novamente.

**Resultado esperado:** aviso *"Veículo já está dentro do parque"*.

### 3. Parque cheio e fila

- [ ] Coloca as vagas da zona D em manutenção (ver dica acima).
- [ ] Enche as 5 vagas de moto com `BBB-222-MC`, `DDD-444-MC`, `HHH-888-MC` e mais duas motos (podes adicioná-las na aba **Clientes**).
- [ ] Tenta entrar com uma sexta moto.

**Resultado esperado:** popup *"Parque cheio. XXXX entrou na fila (posição 1)"*. Em **Ver Fila**, confirma a lista.

### 4. Persistência da fila

- [ ] Com um veículo na fila, fecha a aplicação.
- [ ] Abre-a de novo e clica em **Ver Fila**.

**Resultado esperado:** o veículo continua na fila. Confirma também no DBeaver:

```sql
SELECT * FROM FilaEspera;
```

### 5. Promoção automática da fila

- [ ] Liberta uma vaga de moto fazendo **Saída** de uma das motos que estão dentro.

**Resultado esperado:**
- O popup confirma o pagamento e informa que o primeiro da fila foi promovido automaticamente para essa vaga.
- A grade mostra a vaga ocupada pelo veículo promovido.
- O veículo promovido já não aparece na fila.

### 6. Compatibilidade da fila

- [ ] Põe uma moto e um ligeiro na fila.
- [ ] Liberta uma vaga de `LIGEIRO`.

**Resultado esperado:** o sistema promove o ligeiro, **não** a moto (porque a vaga aceita apenas ligeiros). Se só houvesse motos na fila, nenhuma seria promovida e a vaga ficaria livre.

### 7. Cálculo de preço

- [ ] Entra com um veículo e espera alguns minutos.
- [ ] Faz **Saída**.

**Resultado esperado:** a fatura mostra as horas em decimal e o valor calculado como `horas × 100`. Confirma com o registo na aba **Faturas** e o total na aba **Financeiro**.

### 8. Cancelamento de fila

- [ ] Com um veículo na fila, clica em **Cancelar Fila**.
- [ ] Introduz a matrícula desse veículo.

**Resultado esperado:** mensagem de sucesso e o veículo desaparece da fila (confirma em **Ver Fila** e na base de dados).

### 9. Validação de matrícula

O formato aceite é `AAA-000-AA` (3 letras, 3 números, 2 letras).

- [ ] Na aba **Clientes**, tenta adicionar um veículo com matrícula `ABC123`.

**Resultado esperado:** aviso *"Formato inválido. Usa: AAA-000-AA"*.

- [ ] Tenta com `ABC-123-MC`.

**Resultado esperado:** matrícula aceite.

### 10. Eliminação com confirmação

- [ ] Na aba **Clientes** ou **Veículos**, seleciona uma linha e clica em **Eliminar Selecionado**.

**Resultado esperado:** popup de confirmação antes de eliminar. Se o cliente tiver veículos associados, a operação deve falhar com aviso.

### 11. Manutenção de vaga

- [ ] Na aba **Parque**, clica com o botão direito numa vaga livre.

**Resultado esperado:** a vaga fica cinzenta com o texto `MANUTENÇÃO`.

- [ ] Tenta entrar com um veículo compatível.

**Resultado esperado:** o sistema ignora essa vaga.

- [ ] Clica com o botão direito novamente para a reativar.

### 12. Pesquisa

- [ ] Na aba **Clientes**, escreve parte do nome de um cliente no campo de pesquisa.

**Resultado esperado:** a tabela filtra em tempo real.

- [ ] Repete na aba **Veículos**, pesquisando por matrícula ou marca.

### 13. Fatura na base de dados

- [ ] Faz uma **Saída** completa.
- [ ] Abre o DBeaver e executa:

```sql
SELECT * FROM Fatura ORDER BY id DESC LIMIT 5;
```

**Resultado esperado:** a fatura gravada corresponde ao que a interface mostrou.

### 14. Contadores em tempo real

- [ ] Entra e sai com veículos.
- [ ] Observa o topo da aba **Parque**.

**Resultado esperado:** os números de ocupação e vagas livres atualizam-se automaticamente em até 1 segundo.

---

## Estruturas de dados aplicadas

### Fila (FIFO)

- Implementada em `service/FilaEspera.java`.
- Suportada por `dao/FilaEsperaDAO.java`, que persiste os elementos na tabela `FilaEspera`.
- **Princípio:** o primeiro veículo a chegar é o primeiro a ser atendido quando há vaga.
- A fila é reconstruída automaticamente ao arrancar o programa, lendo a base de dados.

### Pilha (LIFO)

- Implementada em `service/PilhaOperacoes.java`.
- Alimentada pelas operações registadas na tabela `Operacao`.
- **Princípio:** a operação mais recente é a primeira a ser consultada.
- Usada para o gestor ver o histórico recente da sessão.

---

## Observações finais

- O sistema não guarda palavras-passe de clientes, uma vez que o foco é a gestão operacional do parque.
- Todos os dados são persistidos em MySQL, garantindo durabilidade mesmo após reinício.
- Os *triggers* da base de dados garantem integridade mesmo em caso de falha da aplicação (a vaga é marcada como ocupada assim que o bilhete é gravado).
- O tempo de permanência é calculado com precisão ao minuto e o valor é arredondado a duas casas decimais.
- A interface evita depender da consola: todas as mensagens relevantes aparecem em popups ou no registo por baixo da grade de vagas.
- A tabela `Tarifa` já existe para suportar, no futuro, preços diferentes por tipo de veículo. Atualmente, o cálculo usa o valor fixo de 100 Mts por hora.

---

## Autores

Projeto académico desenvolvido no 2.º ano da disciplina de Estrutura de Dados e Algoritmos, por:
> Gota, Denzel 
> 
> Cossa, Alberto
> 
> Mithe, Cleyton
> 
> Paruque, José
> 
> 

Repositório: [github.com/GotaGames/ProjetoParque](https://github.com/GotaGames/ProjetoParque)
