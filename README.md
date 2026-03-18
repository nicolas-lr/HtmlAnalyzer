# HTML Depth Analyzer
Este projeto é uma ferramenta de linha de comando desenvolvida em Java que analisa a estrutura de um documento HTML a partir de uma URL, identifica se o código está bem formado e extrai o conteúdo de texto localizado no nível mais profundo da hierarquia de tags.

Este projeto faz parte do meu portfólio para o Programa de Formação Direction Systems (2026).

## Tecnologias e Conceitos Utilizados
Linguagem: Java 17+

Estrutura de Dados: Pilha (Stack) para análise sintática.

Processamento de Texto: Expressões Regulares (Regex) para identificação de padrões de tags.

Rede: HttpURLConnection com foco em segurança e resiliência.

## Como o Algoritmo Funciona
A lógica central utiliza uma Pilha (Stack) para validar a simetria do HTML. O algoritmo percorre o código linha a linha:

Tag de Abertura: Quando encontra uma tag como &lt;div&gt;, o nome da tag ("div") é empilhado.

Tag de Fechamento: Quando encontra &lt;/div&gt;, o algoritmo verifica se o topo da pilha corresponde a essa tag. Se sim, remove-a; se não, o HTML é marcado como malformed.

Conteúdo de Texto: Se a linha não é uma tag, o algoritmo verifica a quantidade de elementos na pilha (o nível de profundidade atual). Se for maior que o recorde anterior, esse texto é armazenado como o "mais profundo".

## Camadas de Segurança e Resiliência

Proteção contra SSRF: O sistema valida o protocolo da URL (apenas http e https) e bloqueia tentativas de acesso a endereços locais/loopback.

Prevenção de DoS (Denial of Service): Implementei um limite de leitura de 10.000 linhas (MAX_LINES). Isso impede que o servidor esgote sua memória RAM ao tentar processar arquivos maliciosamente gigantes.

Gestão de Timeouts: Defini limites de 5 segundos para conexão e leitura, evitando que o programa fique travado indefinidamente caso o site alvo esteja lento ou instável.

Robustez: O código trata falhas de rede e URLs malformadas, retornando mensagens sem expor o stack trace do sistema.

## Como Executar

1. Certifique-se de ter o JDK 17 ou superior instalado.


2. Clone esse repositorio
   ```bash
   git clone https://github.com/nicolas-lr/HtmlAnalyzer.git

3. Acesse a pasta do projeto que acabou de ser criada:
   ```bash
   cd HtmlAnalyzer
   
4. Compile o arquivo: 
   ```bash
   javac HtmlAnalyzer.java

5. Execute passando uma URL como argumento:
   ```bash
   java HtmlAnalyzer https://www.google.com


### 📂 Meus outros projetos
Além deste analisador de HTML, desenvolvi um sistema de comunicação completa e uma solução IOT:

[Mobile: Remote PC Controller (Android Nativo)](https://github.com/nicolas-lr/Mobile-App)

[Backend Desktop: JustEnoughWires Server (.NET)](https://github.com/nicolas-lr/JustEnoughWires)

[Solução IOT de monitoramento de variáveis ambientais (Temperatura, umidade e pressão)](https://github.com/nicolas-lr/WebServer-ESP32)
