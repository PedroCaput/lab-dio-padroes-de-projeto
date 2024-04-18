# Explorando Padrões de Projetos na Prática com Java

Repositório com as implementações dos padrões de projeto explorados no Lab "Explorando Padrões de Projetos na Prática com Java". Especificamente, este projeto explorou alguns padrões usando o Spring Framework, são eles:
- Singleton
- Strategy/Repository
- Facade

## Features:
1- Criado Exceptions para erros

2- Tratado os casos em que os clientes não existam

3- Adicionado CPF

4- Tratado se o CPF é válido

5- Criado Entity de tratamento de erros

6- Tratada a possibilidade de alterar Cliente com dados errados

7- Tratada a resposta de requisição com formato errado

## Testes:
1- GET sem ter cliente

2- GET com ID sem ter cliente

3- POST com cpf errado

4- POST com formato errado

5- POST com formato certo, criando Cliente

6- POST com a mesma requisição do passo 5

7- Repetir passos 1 e 2 agora contendo cliente no BD e utilizando seu ID

8- PUT com cpf inválido

9- PUT com formato inválido

10- PUT com formato e dados certos

11- Repetir passo 6

12- DELETE com ID errado

13- DELETE com ID certo

14- Repetir passo 1 e 2
