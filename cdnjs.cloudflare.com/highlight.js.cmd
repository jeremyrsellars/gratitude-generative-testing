:: Get parts of highlight.js from cdnjs.cloudflare.com
md ajax\libs\highlight.js\9.13.1
md ajax\libs\highlight.js\9.13.1\languages
md ajax\libs\highlight.js\9.13.1\styles
::        languages
curl -L -o ajax\libs\highlight.js\9.13.1\languages\clojure.min.js  http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/languages/clojure.min.js
curl -L -o ajax\libs\highlight.js\9.13.1\languages\cs.min.js       http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/languages/cs.min.js
::        styles
curl -L -o ajax\libs\highlight.js\9.13.1\styles\default.min.css     http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/styles/default.min.css
type ajax\libs\highlight.js\9.13.1\styles\default.min.css
::            default.min.js
curl -L -o ajax\libs\highlight.js\9.13.1\highlight.min.js          http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/highlight.min.js
