SETLOCAL ENABLEEXTENSIONS
SET ME=%~n0
SET PARENT=%~dp0
java -Djava.security.policy=%PARENT%\java.policy -jar %PARENT%\target\adml-1.0-SNAPSHOT.jar --rmi %*