# _9 — Proxy

**Type:** Structural
**Intent:** Provide a stand-in for another object to control access to it —
add a check, lazy-load it, cache, or log — while sharing its interface so the
client can't tell the difference.

## Standard diagram

```mermaid
classDiagram
    class Subject { <<interface>> +request() }
    class RealSubject { +request() }
    class Proxy { +request() }
    class Client

    Subject <|.. RealSubject
    Subject <|.. Proxy
    Proxy o-- RealSubject : controls access
    Client ..> Subject : uses
```

Proxy and RealSubject share the `Subject` interface; the Proxy **holds** the
real object and gates calls to it.

## This repo's example

`ProxyInternet` implements the same `Internet` interface as `RealInternet` but
blocks banned hosts before delegating (a **protection proxy**).

```mermaid
classDiagram
    class Internet { <<interface>> +connectTo(String) }
    class RealInternet { +connectTo(String) }
    class ProxyInternet { -bannedSites +connectTo(String) }
    class Client { +main() }

    Internet <|.. RealInternet
    Internet <|.. ProxyInternet
    ProxyInternet o-- RealInternet : guards
    Client ..> Internet : uses
```

**Roles:** `Internet` = Subject · `RealInternet` = RealSubject · `ProxyInternet`
= Proxy (access control) · `Client` = Client.

Common proxy flavors: **protection** (this example), **virtual** (lazy/expensive
init), **remote** (network stub), **caching**.

## Run

```
java MachineCoding_LLD.DesignPatterns._09_Proxy.Client
```
