@echo off
setlocal
call lein cljsbuild once pages
set outexc=publish-pages.exclusions.txt
echo .md>"%outexc%"
echo devcards_out>>"%outexc%"
echo pages_out>>"%outexc%"
xcopy /EXCLUDE:%outexc% /s /y "%~dp0resources\public\*" "%~dp0..\..\..\Generative-Testing_gh-pages\"
del "%~dp0..\..\..\Generative-Testing_gh-pages\js\compiled\gratitude\generative_testing_devcards.js"
ren "%~dp0..\..\..\Generative-Testing_gh-pages\js\compiled\gratitude\generative_testing_pages.js" generative_testing_devcards.js
