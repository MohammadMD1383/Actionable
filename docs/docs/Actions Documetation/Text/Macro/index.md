# Macro

Macros are just keyboard shortcuts to paste content into editor, but with a few other interesting features!

## Template

Macro templates are just plain text files, but have a few keywords:

* `$SELECTION$`: Will be replaced by the user selection, if exists.
* `$WORD$`: Will be replaced by the word under the caret, if exists.
* `$ELEMENT$`: Will be replaced by the _PsiElement_ under the caret, if exists.
* `$0$`: Final caret position

## Sample

| Symbol |  Meaning  |
|:------:|:---------:|
| &vert; |   Caret   |
|  [ ]   | Selection |

Template:

```csharp
public required $0$ $WORD$ { get; set; }
```

Text:

```csharp
Username|
```

After executing action:

```csharp
public required | Username { get; set; }
```

## Adding Macros

To add macros [Open Macro Settings](Open%20Macro%20Settings.md), and simply add macros :)

## Using Macros

After adding your macros, you need to assign shortcuts to them (but this is optional).

Your macros will be available under menu bar `Edit > Actionable > Text > Macro`.

**To Assign Shortcuts:**

* press ++ctrl+shift+a++ to open actions search
* type the name of the macro
* press ++alt+enter++ to assign a shortcut
* and you're ready to use it!
