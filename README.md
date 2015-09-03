# midi-data

Handles parsing of MIDI data tuples to maps containing friendly representations of the events they represent.

## Documentation

Documentation for this project has been written in a literate style and can be
generated using [marginalia](https://github.com/gdeer81/marginalia). Once
installed the simplest means for generating it is:
```
lein marg .\src\midi_data\core.cljc .\test\midi_data\core_test.clj
```

## Testing

The project uses [Midje](https://github.com/marick/Midje/) for testing.

### How to run the tests

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.
