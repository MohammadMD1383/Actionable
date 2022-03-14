# A simple fast tutorial on scripting custom actions in Actionable:

## Step 1:
see [documentation](https://mim-lang.github.io/docs) of MiM language. this documentation is not yet complete so I will provide you the essentials you need at this moment in this tutorial.

## Step 2:
goto `settings -> tools -> actionable -> scripting` settings page in your IDE to edit custom action's source code.

## Step 3:
two important variables that you need are `event` and `Reflector`.
* `event`: which reflects `AnActionEvent`
* `Reflector`: which can be invoked with a string argument to get a `Class<T>` from a `fully.qualified.package.name.T`
  * **_notice_**: currently you can only use this function to retrive static fields and methods of the corresponding java class. instantiating feature will be added in the future.

## Step 4:
start coding!

you can refer to [Action Tutorial](https://plugins.jetbrains.com/docs/intellij/working-with-custom-actions.html#extending-the-actionperformed-method) at jetbrains docs to see their api and usage.<br>
The script will be executed in the context of `AnAction#actionPerformed(AnActionEvent event)`

## Step 5:
* execute the custom action at `Edit -> Actionable -> CustomActions -> Custom Action [1-9]`
* or add keyboard shortcuts to the actions, so you can execute you custom actions more convenient.

## Example:
this example illustrates an action that moves the caret to the end of the PsiElement which the caret is located at.
```
# load required classes for our action
val CommonDataKeys = Reflector("com.intellij.openapi.actionSystem.CommonDataKeys");
val FileDocumentManager = Reflector("com.intellij.openapi.fileEditor.FileDocumentManager");
val PsiManager = Reflector("com.intellij.psi.PsiManager");

# get the project from AnActionEvent
val project = event.getProject();

# all variables and references, by default, are wrapped with a Reflector
# so `CommonDataKeys.EDITOR` will return a Reflector<EDITOR>
# to unbox the `EDITOR` object, we need to add `.this` at the end.
val editor = event.getData(CommonDataKeys.EDITOR.this);
val document = editor.getDocument();
val pCaret = editor.getCaretModel().getPrimaryCaret();
val file = FileDocumentManager.getInstance().getFile(document.this); # same tip as above

val newOffset = PsiManager.getInstance(project.this).findFile(file.this).findElementAt(pCaret.getOffset().this).getTextRange().getEndOffset();
pCaret.moveToOffset(newOffset.this);
```

**Some necessary tips:**
* to declare mutable variables use `var`, otherwise `val`.
* in MiM, all numbers are `long` so if you would want to pass an `int` to a method or else, you need to use `intVal(long) -> int` which returns an `int`.
* chaining methods/properties are only supported on an identifier, e.g. `event.getProject()` would be valid in MiM, but `getEvent().getProject()` won't. **so if you want to chain on a function call or else you need to first put it in a variable**.
* if you want to debug the code, you need to clone the actionable project and debug the MiM code through the debugging process of the plugin.
* any exception happening in the custom action will pop up an error message dialog with full stacktrace.

## Hard to understand or confusing?
* you can ask all of your questions related to scripting in Actionable plugin, in this repository at [Discussions](https://github.com/MohammadMD1383/Actionable/discussions) section.
* any question related to the MiM language can be asked in [Discussions](https://github.com/MiM-Lang/docs/discussions) section in MiM docs repository.