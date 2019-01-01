@echo off
setlocal

:: force rebuild
del "%~dp0resources\public\js\compiled\gratitude\generative_testing_*"
call lein cljsbuild once pages

set outexc=publish-pages.exclusions.txt
echo .md>"%outexc%"
echo \devcards_out>>"%outexc%"
echo \pages_out>>"%outexc%"
echo \gratitude\cljs>>"%outexc%"
echo \gratitude\clojure>>"%outexc%"
echo \gratitude\devcards>>"%outexc%"
echo \gratitude\goog>>"%outexc%"
echo \gratitude\gratitude\>>"%outexc%"
echo \gratitude\process>>"%outexc%"
echo \gratitude\sablono>>"%outexc%"
echo \gratitude\us\sellars>>"%outexc%"
echo js\compiled\gratitude\inferred_externs.js>>"%outexc%"
xcopy /EXCLUDE:%outexc% /s /y "%~dp0resources\public\*" "%~dp0..\..\..\Generative-Testing_gh-pages\"
del "%~dp0..\..\..\Generative-Testing_gh-pages\js\compiled\gratitude\generative_testing_devcards.js"
ren "%~dp0..\..\..\Generative-Testing_gh-pages\js\compiled\gratitude\generative_testing_pages.js" generative_testing_devcards.js
del "%~dp0..\..\..\Generative-Testing_gh-pages\js\compiled\gratitude\generative_testing_devcards.js.map"
ren "%~dp0..\..\..\Generative-Testing_gh-pages\js\compiled\gratitude\generative_testing_pages.js.map" generative_testing_devcards.js.map
