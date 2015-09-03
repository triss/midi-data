# midi-data

Handles parsing of MIDI data tuples to maps containing friendly representations of the events they represent.

## Install

Add the following dependency to your `project.clj` file:
```
[triss/midi-data "0.1.0-SNAPSHOT"]
```

## Documentation

Documentation for this project has been written in a literate style and can be
generated using [Marginalia](https://github.com/gdeer81/marginalia). Once
installed the simplest means for generating it is: `lein marg`

## Testing

The project uses [Midje](https://github.com/marick/Midje/) for testing.

### How to run the tests

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.

## Copyright

Copyright © 2015 Tristan Strange <tristan.strange@gmail.com>

This work is free. You can redistribute it and/or modify it under the
terms of the Do What The Fuck You Want To Public License, Version 2,
as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
