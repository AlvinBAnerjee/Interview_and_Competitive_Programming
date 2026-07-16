# _4 — Adapter

**Type:** Structural
**Intent:** Wrap an existing class so its **incompatible** interface looks like
the one the client expects. The "power plug in a foreign socket" pattern.

## Standard diagram

```mermaid
classDiagram
    class Target { <<interface>> +request() }
    class Adapter { +request() }
    class Adaptee { +specificRequest() }
    class Client

    Target <|.. Adapter
    Adapter o-- Adaptee : wraps
    Client ..> Target : uses
```

The Adapter **implements** the Target and **holds** the Adaptee, translating
`request()` into `specificRequest()`.

## This repo's example

The client wants the `Data` interface, but the source (`XML`) only exposes
`fetchXmlData()` returning XML. `Adapter_XML_to_JSON` bridges the two.

```mermaid
classDiagram
    class Data { <<interface>> +getData() }
    class Adapter_XML_to_JSON { +getData() }
    class XML { +fetchXmlData() String }
    class JsonReader { +main() }

    Data <|.. Adapter_XML_to_JSON
    Adapter_XML_to_JSON o-- XML : wraps + converts
    JsonReader ..> Data : uses
```

**Roles:** `Data` = Target · `Adapter_XML_to_JSON` = Adapter · `XML` = Adaptee
(incompatible method name/return type) · `JsonReader` = Client.

## Run

```
java MachineCoding_LLD.DesignPatterns._4_Adapter.JsonReader
```
