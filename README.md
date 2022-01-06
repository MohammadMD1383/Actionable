# IntelliJ Platform Actionable Plugin

This plugin adds some extra functionality that makes it easier to interact with the editor.

<b>Fell free to extend this plugin by your needs!</b>

---

## Features

* Duplicate Line(s) down/up
  > this action is inspired by VSCode <br/>
  > intelliJ's duplicate action can only duplicate the selection to the front,
  > or duplicate the line down if there is no selection.<br/>
  > <br/>
  > the two actions added by **this plugin** can duplicate line(s) **both down and up**
  > and if there is a selection, it will duplicate the whole lines that exist in the selection.

* Justify/align cursors start/end
  > these two actions will work when there are **more than one caret** in the active editor.<br/>
  > the usage is that you can align your carets to the rightmost/leftmost caret. for example:<br/>
  > <small>_the pipe character shows the caret_</small>
  > 
  > before:
  > ```
  > String |s = new String();
  > String s2 = |new String();
  > ```
  > 
  > after(using align end/right):
  > ```
  > String s = n|ew String();
  > String s2 = |new String();
  > ```

* Copy/Cut word at caret
  > these actions will copy/cut the word at the caret.<br/>
  > **Note:** works when there is **only one caret** 

## How does it work?

For convenience, I haven't added any hot keys to the actions by default.<br/>
If you like any of the actions you can add your favorite hotkeys to them. 
