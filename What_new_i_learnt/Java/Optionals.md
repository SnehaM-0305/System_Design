# Java Optional

`Optional` is a Java class used when a value **may or may not be present**.

It helps us handle `null` values safely and avoid `NullPointerException`.

## 1. Creating Optional

### `Optional.of()`

Use when the value is **definitely not null**.

```java
Optional<String> name = Optional.of("Sneha");
```

If the value is `null`, it throws an exception.

### `Optional.ofNullable()`

Use when the value **might be null**.

```java
String name = null;

Optional<String> result = Optional.ofNullable(name);
```

### `Optional.empty()`

Creates an Optional with **no value**.

```java
Optional<String> result = Optional.empty();
```

## 2. Checking if Value Exists

Use `isPresent()`:

```java
Optional<String> name = Optional.of("Sneha");

if (name.isPresent()) {
    System.out.println(name.get());
}
```

Output:

```text
Sneha
```

## 3. Getting a Default Value

`orElse()` gives a default value if the Optional is empty.

```java
String name = null;

String result = Optional.ofNullable(name)
                        .orElse("Unknown");

System.out.println(result);
```

Output:

```text
Unknown
```

## 4. Using `ifPresent()`

`ifPresent()` executes code only when a value exists.

```java
Optional<String> name = Optional.of("Sneha");

name.ifPresent(n -> System.out.println(n));
```

Output:

```text
Sneha
```

## 5. Simple Real Example

Instead of:

```java
String name = getName();

if (name != null) {
    System.out.println(name);
} else {
    System.out.println("Name not found");
}
```

We can use:

```java
String name = getName();

Optional.ofNullable(name)
        .ifPresentOrElse(
            n -> System.out.println(n),
            () -> System.out.println("Name not found")
        );
```

## Common Methods

| Method         | Meaning                  |
| -------------- | ------------------------ |
| `of()`         | Value must not be null   |
| `ofNullable()` | Value can be null        |
| `empty()`      | No value                 |
| `isPresent()`  | Check if value exists    |
| `get()`        | Get the value            |
| `orElse()`     | Give a default value     |
| `ifPresent()`  | Run code if value exists |

### Easy way to remember

Think of `Optional` as a **box**:

```text
Optional
   |
   ├── Value exists → "Sneha"
   |
   └── Value doesn't exist → empty
```

**Main purpose:** Handle values that might be `null` in a cleaner and safer way.
