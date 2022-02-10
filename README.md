# IntelliJ Platform Actionable Plugin

This plugin adds some extra functionality that makes it easier to interact with the editor.

<b>Feel free to extend this plugin by your needs!</b>

Already familiar with the plugin actions? see [How does it work](#how-does-it-work)

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

* Copy/Cut word/element at caret
  > these actions will copy/cut the word/element<sup>*</sup> at the caret.<br/>
  > **Note:** works when there is **only one caret**
  >
  > <small>*: e.g. a string literal is an element, a key word is an element, etc. Actually a <u>psi element</u>.</small>

* Move caret to next/previous word (+ with selection)
  > These four actions are like the IDE ones, but a little different and also customizable.

* Add selection to next/previous occurrence
  > Like the one that IDE has, but note that intelliJ platform only supports add selection for **next occurrence**;
  > but this plugin offers both **next/previous** occurrence finding.
  > _<small>will be configurable in future releases.</small>_

* Set copy/cut paste word/element offset + Execute paste
  > By executing the mentioned actions, you will set the paste location in your **project**, which will be later used to
  > paste the clicked word/element.
  >
  > Let's make it clearer by an example: _(pipe sign indicates the caret position)_
  > ```js
  > document.write("Hello, world! |");
  > ```
  > then you execute the set `set copy-paste word offset` action;<br>
  > now you click on the word `document`, the result will be as follows:
  > ```js
  > document.write("Hello, world! document|");
  > ```
  >
  > In this example, I used the `mouse left click` keybinding for the `Execute paste` action.<br>
  > I recommend using this keybinding if possible.<br>

## How does it work?

For convenience, I haven't added any hot keys to the actions by default.<br/>
If you like any of the actions you can add your favorite hotkeys to them. 
