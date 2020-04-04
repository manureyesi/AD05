# Tarefa AD05

Descripción do problema
Imos facer un pequeno Drive cunhas funcionalidades moi básicas.

## Paso 1

Crea unha base de datos de nome “minidrive” uando phpPgAdmin ou outro cliente de PostgreSQL para gardar o seguinte:

* Directorios: débese gardar un identificador para cada directorio e o nome do directorio. A raíz é o directorio “.”, a un directorio entro dese tería o nome “./carpeta”
* Arquivos: débese gardar o id do arquivo, o nome do arquivo, o id do directorio no que se encontra e o arquivo binario.

## Paso 2

Crea un programa que lea o seguinte arquivo json:

```
{
    "dbConnection":{
        "address":"192.168.56.102",
        "name":"minidrive",
        "user":"accesodatos",
        "password":"abc123."    
    },
    "app":{
        "directory":"/home/user/minidrive"
    }
}
```

No atributo “dbConnection” teremos que ter os datos de conexión a base de datos de PostgreSQL e no atributo “app” a configuración do noso programa.

Polo tanto, deberédesvos de conectar a base de datos que proporciona o json. Posteriormente ler todas as carpetas, e para cada carpeta as súas subcarpetas, do directorio que se proporciona o json (neste exemplo “/home/user/minidrive”). Utilizade recursividade para que sexa eficiente. Para cada unha desas carpetas e subcarpetas, gardades unha entrada na base de datos. Recordade tamén engadir o directorio raiz que sería o propio “/home/user/minidrive”.

A continuación, realizades o mesmo pero para cada arquivo de cada carpeta e subcarpeta. Tedes que gardar o nome, o id do directorio é o propio arquivo binario. Tamén recursivamente.

En resumo, tedes que mapear un directorio e gardar todos os arquivos que haxa. Se o arquivo ou directorio xa está na base de datos, non engadades unha entrada. Considerade que un arquivo é o mesmo se ten o mesmo nome e están no mesmo directorio. Non imos detectar se este foi actualizado.

## Paso 3

Agora facemos un pequeno cambio no funcionamento da app. Agora iníciase e realízase o paso anterior. Para todos aqueles arquivos que non están no directorio (en calquera subcarpeta tamén), tedes que descargalos e gardalos na carpeta correspondente.

Para probar a app, é tan facil como primeiro facer unha proba cun directorio. Unha vez finalizado o programa borrades un arquivo e engadides outro. Arrancades de novo o programa e comprobades que tedes o arquivo borrado e que na base de datos se engadiu o novo arquivo.

## Paso 4

Unha vez que se realizan as tarefas do paso 2 e 3 a aplicación deberá quedar a espera e cada un determinado tempo comprobar se hai novos arquivos ou directorios. Se os hai, deberedes de gardar esa información na base de datos. Para realizar isto, o mellor e crear un Thread, e nese fío, ter unha conexión propia a base de datos. Tedes un exemplo en: (https://jdbc.postgresql.org/documentation/head/listennotify.html#listen-notify-example)[].

## Paso 5

Creade unha función e un trigger que cren unha notificación para cando se engada un novo arquivo.

## Paso 6

Creade un novo fío na app que estea a escoita de notificacións de novos arquivos. Este fío ten unha conexión propia a base de datos como no exemplo do paso 4. Cada vez que reciba unha notificación, comprobará se o arquivo existe, e se non existe descargarao.

Non contempledes que se engada un arquivo nun directorio que non temos na base de datos. Porque habería que crear outra notificación de creación de directorios e comprobar sempre que se engada un arquivo pque o directorio existe. Complica o programa.

Para comprobar o funcionamento do programa, podedes iniciar dúas veces o programa con dous directorios distintos. Comprobades que teñan as mesmas carpetas e arquivos nos dous directorios. Despois nun deles engadides un arquivo, e comprobades que se arquivo se engade no outro directorio.
