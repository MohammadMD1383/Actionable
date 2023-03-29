## What Does It Do?

* Duplicates the line, where the caret is, up.
* Duplicates all the lines that has a selection.

[//]: # (@formatter:off)

    ??? example
        | Symbol |  Meaning  |
        |:------:|:---------:|
        | &vert; |   Caret   |
        |  [ ]   | Selection |

        before:
        ```
        this [is a
        multiline
        te]|xt.
        ```

        after:
        ```
        this [is a
        multiline
        te]|xt.
        this is a
        multiline
        text.
        ```

!!! info
    This is similar to what ==vscode== does!
