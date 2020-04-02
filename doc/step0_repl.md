### Step 0: The REPL

![step0_repl architecture](step0_repl.png)

This step introduces the skeleton of the interpreter.

* Open `src/make_a_lisp_with_clojure/step0_repl.clj`

* The file contains the 4 trivial functions `READ`, `EVAL`, `PRINT`, and
  `read-eval-print`. `READ`, `EVAL`, and `PRINT` are basically just stubs that
  return their first parameter and `read-eval-print` calls them in order,
  passing the return to the input of the next.

* There is also a main loop that repeatedly prints a prompt, gets a line of
  input from the user, calls `read-eval-print` with that line of input, and then
  prints out the result from `read-eval-print`.

You can run the tests for the first step by running:

```
lein test :only make-a-lisp-with-clojure.step0-repl-test
``

Everything passes, since the only thing our interpreter does right now is pass
its arguments through.

You can also compile and run our skeleton interpreter:

```
lein with-profile step0 uberjar
java -jar target/uberjar/step0_repl.jar
```

You should see a simple prompt that repeats your commands back to you:

```
user> hello world!
hello world!
```

You can cancel out of the repl by procssing CTRL-C.
