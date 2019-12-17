# Лабораторная работа #2. Ручное построение нисходящих парсеров

## Вариант 7. Описание заголовка функции в Паскале

Заголовок функции в Паскале. Заголовок начинается ключевым
словом “function” или “procedure”, далее идет имя функции, скобка,
несколько описаний аргументов. Описание аргументов содержит имена
переменных через запятую, затем двоеточие, затем имя типа. Достаточно рассматривать только примитивные типы (массивы, записи и т. п. не
требуется). После этого в случае функции следует двоеточие и имя типа.
Используйте один терминал для всех имен переменных и имен типов.
Используйте один терминал для ключевых слов function и т. п. (не
несколько ‘f’, ‘u’, ‘n’, ...).
Пример: function fib(n: integer): integer;



### Грамматика:

* S -> function(arguments):type;
* S -> procedure(arguments);
* arguments -> eps
* arguments -> names:type
* arguments -> arguments;names:type
* names -> names
* names -> names,singleName

Нетерминалы:
* S - сигнатура всей функции (процедуры)
* arguments - аргументы функции
* names - имена аргументов определённого типа

### Грамматика без левой рекурсии:

* S -> function ( arguments ) : type ;
* S -> procedure ( arguments ) ;
* arguments -> eps
* arguments -> names : type arguments'
* arguments' -> eps
* arguments' -> ; names : type arguments'
* names -> singleName names'
* names' -> eps
* names' -> , singleName names'

Нетерминалы:
* S - сигнатура всей функции (процедуры)
* arguments - аргументы функции (начало аргументов)
* arguments' - продолжение аргументов функции
* names - имена аргументов определённого типа(начало)
* names' - продолжение имён аргументов определённого типа 


### Множества FIRST и FOLLOW
 
|            | first                 | follow |
|------------|-----------------------|--------|
| S          | { function, procedure } |  { $ }   |
| arguments  |   { eps, singleName }   | { ')' }  |
| arguments' |      { eps, ';' }       | { ')' }  |
| names      |     { singleName }      | { ':' }  |
| names'     |      { eps, ',' }       | { ':' }  |