Selects from the caret forward up to the key you press, or the end of line.

## Example

| Symbol |  Meaning  |
|:------:|:---------:|
| &vert; |   Caret   |
|  [ ]   | Selection |

### Before

[//]: # (@formatter:off)
```javascript
const |a = document.body.style;
```
[//]: # (@formatter:on)

### Action

1. Execute the action
2. Press ++period++

### After

[//]: # (@formatter:off)
```javascript
const [a = document]|.body.style;
```
[//]: # (@formatter:on)

## Remarks

[//]: # (@formatter:off)
!!! tip
    To cancel the action, press ++escape++.
[//]: # (@formatter:on)
