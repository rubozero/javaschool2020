# javaschool2020
Repositorio público Java School grupo 2 [rvaldes]

La aplicación se encuentra dividido en dos funcionalidades:
La primera funcionalidad es para crear url cortas a partir de urls "normales".
De acuerdo con la url que se requiera acortar, existen tres variantes en la url corta obtenida:
Para las url que contienen la palabra "google" o "yahoo" se creará una url corta alfanumérica (números de 0 al 9 y los caracteres del alfabeto en mayúsculas y minúsculas).
En el caso específico de la palabra "google" la longitud de la url alfanumérica será de 5.
En el caso específico de la palabra "yahoo" la longitud de la url alfanumérica será de 7.
En caso de que la url que se desea acortar no contenga la palabra "google" ni la palabra "yahoo", la url generada será a partir de la misma url removiendo caracteres especiales y vocales, sin límite de longitud.

Ejemplos:
Si la url que se desea acortar es la siguiente: google.com
Se generará una url de 5 dígitos como la siguiente: 2GyqQ

Si la url que se desa acortar es la siguiente: yahoo.com
Se generará una url de 5 dígitos como la siguiente: sq7aT3v

Si la url que se desa acortar es la siguiente: nearsoft.com
Se generará una url de 5 dígitos como la siguiente: nrsftcm

Para obtener una url acortada se realiza una petición POST hacia http://localhost:8080/api/url/register
En el cuerpo de la petición se agrega un objeto conteniendo la propiedad "url", por ejemplo:
{"url":"yahoo.com"}
En la respuesta de la petición, la propiedad "alias" contendrá la url acortada correspondiente, por ejemplo:
{
    "url": "yahoo.com",
    "alias": "k8C8gIO"
}

Cada vez que se realiza una petición POST para obtener una url acortada, la aplicación almacena la url y su alias en una tabla de base de datos H2 en memoria.
Antes de almacenar un registro, la aplicación valida en la base de datos si ya existe algún registro con la url que se requiere acortar. 
Si el registro ya existe en la base de datos, la respuesta contendrá el alias que ya se tiene almacenado para dicha url.

Si el registro no existe en la base de datos, se valida el tipo de url (de acuerdo con los tres escenarios descritos) y se genera el alias correspondiente al tipo de url.
Para la generación de url cortas de urls que contiene "google" o "yahoo" se obtiene un número generado aleatoriamente sumándole una "sal" que consiste de la hora en milisegundos dividida entre, 62 (base numérica para la url acortada) multiplicada por la longitud requerida.
La "sal" se genera de la siguiente manera: ZonedDateTime.now().toInstant().toEpochMilli()/(base*urlLength), para las urls que contienen "google" urlLength será 5, para las urls que contienen la palabra "yahoo" urlLength será 7.
Una vez que se tiene el número aleatorio con "sal" se obtiene el "resto" de la división de dicho número entre 62 (la base utilizada para generar la url acortada).
De acuerdo al tipo de url se almacenan 5 o 7 "restos" en un arreglo; se recorre el arreglo y para cada posición se obtiene el carácter correspondiente en base 62. El resultado de esa conversión es la url acortada.
Si la url no contiene "google" ni "yahoo", se toma la url proporcionada y se eliminan caracteres especiales y vocales para obtener la url acortada.


La segunda funcionalidad es para redireccionar a la url "original" a partir de un alias (url acortada).
Para ser redireccionado a la url "original" se realiza una petición GET hacia http://localhost:8080/api/url/<<alias>>
De acuerdo con el alias proporcionado se realiza una consulta a la base de datos para obtener la url correspondiente.
Si el alias se encuentra en la base de datos, el aplicativo realizará un redireccionamiento hacia la url correspondiente con el alias.
Si el alias no se encuentra en la base de datos, el aplicativo realiza un redireccionamiente hacia una página de error del mismo aplicativo.
