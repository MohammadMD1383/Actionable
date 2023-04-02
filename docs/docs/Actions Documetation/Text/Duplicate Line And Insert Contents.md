Duplicates a line (or multiple lines) and inserts contents gotten from an input dialog.

[//]: # (@formatter:off)

!!! note
		* Using multiple carets are supported.
		* Selections are supported.

[//]: # (@formatter:on)

## Example

| Symbol |  Meaning  |
|:------:|:---------:|
| &vert; |   Caret   |
|  [ ]   | Selection |

### Before

```go
| := "variable-[aaaaa]|"
```

### Action

1. Execute the action
2. Insert the following content to the input dialog:
   ```
   username
   password
   ```
3. Press ++enter++

### After

```go
[username]| := "variable-[username]|"
[password]| := "variable-[password]|"
```

## Another Example

### Before

```go
func | () {
println("|")
}
```

### Action

1. Execute the action
2. Enter the following content
   ```
   hello
   world
   ```
3. Set the ==Extra below lines== to ==1==
4. Press `OK`

### After

```go
func [hello]| () {
println("[hello]|")
}
func [world]| () {
println("[world]|")
}
```

## Remarks

[//]: # (@formatter:off)

!!! info
		There is a setting under `Actionable > Text` that you can check it if you
		want the replacement to be case-preserving.

		before:
		```
		aCamelCaseName
		```

		your input:
		```
		HELLO_WORLD
		PascalCase
		```

		after:
		```
		helloWorld
		pascalCase
		```

		You can also control this setting, for the active action, within the dialog opened.
