# _4 — Adapter

**Type:** Structural
**Intent:** Wrap an existing class so its **incompatible** interface looks like
the one the client expects. The "power plug in a foreign socket" pattern.

## Standard diagram

<img src="./assets/README-1.png" alt="Standard UML class diagram" width="460">

The Adapter **implements** the Target and **holds** the Adaptee, translating
`request()` into `specificRequest()`.

## This repo's example

The client wants the `Data` interface, but the source (`XML`) only exposes
`fetchXmlData()` returning XML. `Adapter_XML_to_JSON` bridges the two.

<img src="./assets/README-2.png" alt="Example UML class diagram — this repo" width="460">

**Roles:** `Data` = Target · `Adapter_XML_to_JSON` = Adapter · `XML` = Adaptee
(incompatible method name/return type) · `JsonReader` = Client.

## Run

```
java MachineCoding_LLD.DesignPatterns._04_Adapter.JsonReader
```
