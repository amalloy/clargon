# clargon

Clargon is a Command Line ARG parser...ON. 

## Usage

   (clargon \*command-line-args\*
     (required ["-p" "--port"] #(Integer. %)
     (option ["-h" "--host" :default "localhost"])
     (option ["--verbose" :default true])
     (option ["--log-directory" :default "/some/path"]))

with \*command-line-args\* of:

     '("-p" "8080" "--no-verbose" "--log-directory" "/tmp")

will produce a clojure map with the names picked out for you as keywords:

     {:port 8080
      :host "localhost"
      :verbose false
      :log-directory "/tmp"}

Required parameters will throw an Exception if not given, optionals
will not. Defaults can be provided as shown above.

## TODO

Support arbritrary nested maps by some kind of convention of parameter
naming that I havent thought of yet O_o.

## License

Copyright (C) 2010 Gareth Jones

Distributed under the Eclipse Public License, the same as Clojure.
