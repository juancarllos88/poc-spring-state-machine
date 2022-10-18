# labs

1 - O que é e para que serve uma máquina de estados?
É uma estutura de desenvolvimento que permite gerenciar estados bem definidos de um objeto de negócio. 
Serve para que os objetos mudem de estado de maneira simples, centralizada e de fácil entendimento. Evitando o uso de lógicas 
complexas e espalhadas no código do sistema. 

2 - Quando usar e quando não usar a maquina de estados? Descreva uma situação onde podemos utilizar.
Devemos usar quando nosso negócio tem varios estados bem definidos, como por exemplo estados de um pedido de compras. 
Nesse contexto existem várias regras para que um estado seja modificado. Acredito que nao devemos adicionar essa complexidade a 
um negócio com um escopo bem pequeno.

3 - O que é e quando usar um listener? Descreva uma situação onde podemos utilizar.
é um classe que fica monitorando quaisquer mudança de estado de um objeto. 
Quando determinado objeto atingir um estado X, ele execute algo particular, tipo um envio de email, envio para uma mensagem pra um broker.

4 - Descreve brevemente como funciona a troca de estados e quem pode enviar eventos.
A lógica é basicamente controlada por enums de estados e de transições. 
Configuramos uma estrutura em que declaramos a mudança de um estado de origem pra uma estado de destino, se determiando evento for disparado.

5 - O que são Guards e Actions e quando devo usá-los?
Guard é uma condição que afeta diretamento o comportamento da maquina de estado, 
habilitando ou não a transição de um estado. Já a Actions é um comportamento executado durante o disparo de uma transição.

6 - Existem 2 formas de persistir e recuperar a maquina de estado por request. Descreva essas 2 formas de forma sucinta.
Uma seria fazer o sync da maquina de estado com a entidade e persiste no banco de dados, 
e a outra seria fazer o sync da maquina com a entidade, mas deixando a camada de serviço chamar o repositório pra fazer a persistencia.
