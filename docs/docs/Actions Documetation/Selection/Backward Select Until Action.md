Selects from the caret backward up to the key you press, or the start of line.

## Example

| Symbol |  Meaning  |
|:------:|:---------:|
| &vert; |   Caret   |
|  [ ]   | Selection |

### Before

[//]: # (@formatter:off)
```javascript
const a = document.body.style|;
```
[//]: # (@formatter:on)

### Action

1. Execute the action
2. Press ++dot++

### After

[//]: # (@formatter:off)
```javascript
const a = document.body.|[style];
```
[//]: # (@formatter:on)

## Remarks

[//]: # (@formatter:off)
!!! tip
    To cancel the action, press ++escape++.
[//]: # (@formatter:on)
