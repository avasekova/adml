SETLOCAL ENABLEEXTENSIONS
SET ME=%~n0
SET PARENT=%~dp0
SET REPO=%PARENT%\libraries-repo
SET LIBS=%PARENT%\libraries

mvn install:install-file ^
-Dfile=%LIBS%\javaGD.jar ^
-DgroupId=mlp.org.rosuda ^
-DartifactId=javaGD      ^
-Dversion=1.0            ^
-Dpackaging=jar          ^
-DgeneratePom=true       ^
-DlocalRepositoryPath=%REPO%

mvn install:install-file ^
-Dfile=%LIBS%\JRI.jar    ^
-DgroupId=mlp.org.rosuda ^
-DartifactId=JRI         ^
-Dversion=1.0            ^
-Dpackaging=jar          ^
-DgeneratePom=true       ^
-DlocalRepositoryPath=%REPO%

mvn install:install-file ^
-Dfile=%LIBS%\JRIEngine.jar ^
-DgroupId=mlp.org.rosuda ^
-DartifactId=JRIEngine   ^
-Dversion=1.0            ^
-Dpackaging=jar          ^
-DgeneratePom=true       ^
-DlocalRepositoryPath=%REPO%

mvn install:install-file  ^
-Dfile=%LIBS%\REngine.jar ^
-DgroupId=mlp.org.rosuda ^
-DartifactId=REngine     ^
-Dversion=1.0            ^
-Dpackaging=jar          ^
-DgeneratePom=true       ^
-DlocalRepositoryPath=%REPO%

mvn install:install-file  ^
-Dfile=%LIBS%\jxl.jar     ^
-DgroupId=mlp.jxl        ^
-DartifactId=jxl         ^
-Dversion=1.0            ^
-Dpackaging=jar          ^
-DgeneratePom=true       ^
-DlocalRepositoryPath=%REPO%
