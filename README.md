# clargon

Clargon is a Command Line ARG parser...ON. (For clojure).

## Usage

Example:

    (clargon args
      (required ["-p" "--port" "the port for the server"] #(Integer. %))
      (optional ["--host" "hostname of server" :default "localhost"])
      (optional ["--verbose" "run in chatty mode" :default true])
      (optional ["--log-directory" "where you put logs" :default "/some/path"])))

with args of:

     '("-p" "8080" "--no-verbose" "--log-directory" "/tmp")

will produce a clojure map with the names picked out for you as keywords:

     {:port 8080
      :host "localhost"
      :verbose false
      :log-directory "/tmp"}

A flag of -h or --help is provided which will currently give a
documentation string:

    Usage:

     Switches         Desc                     Default     Required 
     --------         ----                     -------     -------- 
     -p, --port       the port for the server              Yes      
     --host           hostname of server       localhost   No       
     --verbose        run in chatty mode       true        No       
     --log-directory  where you put logs       /some/path  No       

Required parameters will halt program execution if not provided,
optionals will not. Defaults can be provided as shown above. Errors
caused by parsing functions (such as #(Integer. %) above) will halt
program execution.

See example.clj for a simple example.

## TODO

Support arbritrary nested maps by some kind of convention of parameter
naming that I havent thought of yet O_o.

## License

Copyright (C) 2010 Gareth Jones

Distributed under the Eclipse Public License, the same as Clojure.
