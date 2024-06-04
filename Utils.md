# BOYC - Utils

## From DIV's to Links

Replace

```regexp
<a title=\"(.*)\" href=\"(.*)\">\W*.*\W*.*\W.*.\W*a>
```

With

```regexp
[$1]($2)
```
