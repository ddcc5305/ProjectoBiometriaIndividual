En la carpeta src se encuentran las 3 sub carpetas de la app Android, la página web y el código de Arduino.

Para Java teléfono: Para montarlo y hacerlo funcionar hay que descargar la apk que se encuentra en la carpeta Android y aceptar los permisos.

Para la parte de página web: Acceder a la página web desde este enlace: https://dbayluj.upv.edu.es/index.html , y pulsar el botón de recargar mediciones cada cierto tiempo para actualizar la tabla.

Para c++ Arduino y placa: Acceder al archivo HolaMundoIBeacon.ino que se encuentra en la carpeta de Arduino, conectar la placa y compilar, una vez compilado, abrir el serial para que empiece a enviar datos mediante IBeacon.

Los diseños se encuentran en sus respectivas carpetas dentro de doc.

Para hacer funcionar los tests como lo he montado se necesita acceder a Plesk subir todos los archivos de la página web, instalar la extensión de node.js, dentro del package.json hay que poner los nombres de los test.
Una vez preparado solo hay que abrir run node.js commands desde su icono y darle a ejecutar todos los tests.