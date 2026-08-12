# SWAPI Test Framework

API test automation for the read-only **Star Wars API** ([swapi.dev](https://swapi.dev/api/)),
built with **Java 21 · RestAssured 5.5 · TestNG 7.10 · Jackson 2.17 · Lombok 1.18**.

---

## What it covers

Six resources — **People, Planets, Films, Species, Vehicles and Starships** — validated across six layers:

| Layer | Group | Description |
|-------|-------|-------------|
| Contract — read verbs | `happy` | GET list, GET by id, HEAD, OPTIONS → 200 |
| Contract — write verbs | `negative` | POST, PUT, DELETE → 403 (read-only API) |
| Field deserialization | `people` `films` `planet` `specie` `starship` `vehicle` | Per-field assertions via Jackson deserialization |
| Body validation | (same resource groups) | Inline Hamcrest matchers on raw JSON response |
| Pagination | `pagination` | Default page size + query-parameter behaviour |
| Performance | `performance` | HTTP response time classified and asserted |
| Security | `security` | XSS injection probe in path → must return 404 |

The default suite (`mvn test`) runs all six layers for every resource.

---

## Project structure

```
SWAPI_Automation/
├── pom.xml
├── testng.xml                          # suite: happy + negative + pagination + performance + security
│
├── src/main/
│   ├── resources/
│   │   └── config.properties           # base.uri / base.path
│   └── java/com/swapi/framework/
│       ├── config/
│       │   └── ConfigManager.java      # loads config.properties; -D overrides supported
│       ├── constants/
│       │   └── Endpoints.java          # resource paths with trailing slash
│       ├── core/
│       │   ├── BaseEndpoint.java       # GET/HEAD/OPTIONS/POST/PUT/DELETE + getAll(params) + getByPath(segment)
│       │   ├── HttpStatus.java         # named status codes (OK, NOT_FOUND, FORBIDDEN, …)
│       │   ├── ResponseCapture.java    # ThreadLocal status-code holder
│       │   └── SpecFactory.java        # shared RequestSpecification (timeouts + proxy)
│       ├── endpoints/                  # one thin class per resource
│       │   ├── FilmEndpoint.java
│       │   ├── PeopleEndpoint.java
│       │   ├── PlanetEndpoint.java
│       │   ├── SpeciesEndpoint.java
│       │   ├── StarshipEndpoint.java
│       │   └── VehicleEndpoint.java
│       ├── models/                     # Jackson + Lombok POJOs
│       │   ├── BaseModel.java          # created / edited / url
│       │   ├── Film.java
│       │   ├── PagedResponse.java
│       │   ├── Person.java
│       │   ├── Planet.java
│       │   ├── Species.java
│       │   ├── Starship.java
│       │   └── Vehicle.java
│       └── reporting/
│           ├── HtmlReporter.java       # generates target/swapi-report.html
│           ├── PerformanceRating.java  # classifies response time into Excellent/Good/Acceptable/Slow
│           ├── StatusCodeListener.java # bridges ResponseCapture → TestNG result attributes
│           └── Story.java             # @Story annotation for human-readable report labels
│
├── src/test/
│   ├── resources/
│   │   └── swapi.properties            # expected field values for field-level tests
│   └── java/com/swapi/tests/
│       ├── AbstractResourceTest.java   # shared tests: happy + negative + body + performance + security
│       ├── BaseTest.java               # RestAssured global config + proxy auto-detection
│       ├── FilmTest.java
│       ├── PeopleTest.java
│       ├── PlanetTest.java
│       ├── SpeciesTest.java
│       ├── StarshipTest.java
│       ├── SwapiProperties.java        # reads swapi.properties; strips surrounding quotes
│       └── VehicleTest.java
│
└── target/
    └── swapi-report.html               # HTML executive summary (auto-generated)
```

---

## Configuration

### `src/main/resources/config.properties`

Controls where the suite points. Any value can be overridden at runtime with a
`-D` system property without touching the file.

```properties
base.uri=https://swapi.dev
base.path=/api
```

### `src/test/resources/swapi.properties`

Holds the expected field values for each resource used in deserialization and Hamcrest tests.
Update a value here and every corresponding assertion picks up the change automatically.

```properties
# People (id = 1 – Luke Skywalker)
people.name = "Luke Skywalker"
people.height = "172"
people.mass = "77"
# ...

# Films (id = 1 – A New Hope)
films.title = "A New Hope"
films.episode_id = "4"
# ...
```

Values are wrapped in double-quotes inside the file; `SwapiProperties` strips them
automatically so tests receive the bare string.

---

## Running

```bash
# Full suite — all six layers for all six resources
mvn test

# Contract tests only
mvn test -Dgroups=happy
mvn test -Dgroups=negative

# Field-level + body validation for a single resource
mvn test -Dgroups=people
mvn test -Dgroups=films
mvn test -Dgroups=planet
mvn test -Dgroups=specie
mvn test -Dgroups=starship
mvn test -Dgroups=vehicle

# Pagination tests only (People, Planets, Starships, Vehicles)
mvn test -Dgroups=pagination

# Performance tests only (all six resources)
mvn test -Dgroups=performance

# Security (XSS injection) tests only (all six resources)
mvn test -Dgroups=security

# Combine groups as needed
mvn test -Dgroups=happy,performance
mvn test -Dgroups=security,negative
mvn test -Dgroups=people,pagination

# Point at a different environment without editing files
mvn test -Dbase.uri=https://swapi.dev -Dbase.path=/api
```

After every execution the HTML report is written to `target/swapi-report.html`.

---

## Test layers in detail

### 1 — Contract tests (group: `happy`, `negative`)

Shared via `AbstractResourceTest`. Every concrete resource class inherits these tests
automatically — no duplication required.

| Verb | Group | Expected |
|------|-------|----------|
| GET (list) | `happy` | 200 + `count > 0` + non-empty `results` |
| GET (by id) | `happy` | 200 + non-null `url` field |
| HEAD | `happy` | 200, empty body |
| OPTIONS | `happy` | 200 |
| POST | `negative` | 403 Forbidden |
| PUT | `negative` | 403 Forbidden |
| DELETE | `negative` | 403 Forbidden |

---

### 2 — Field deserialization tests (resource-specific groups)

Each resource class has a `@BeforeClass` that makes **one HTTP call** and caches the
deserialized model. All `verify*` tests run against the cached object — zero additional
network traffic.

| Resource | Group | Sample verified fields |
|----------|-------|----------------------|
| People | `people` | name, height, mass, hair\_color, skin\_color, eye\_color, birth\_year, gender |
| Films | `films` | title, episode\_id, director, producer, release\_date |
| Planets | `planet` | name, rotation\_period, orbital\_period, diameter, climate, gravity, terrain, surface\_water, population |
| Species | `specie` | name, classification, designation, average\_height, skin\_colors, hair\_colors, eye\_colors, average\_lifespan, language |
| Starships | `starship` | name, model, manufacturer, cost\_in\_credits, length, max\_atmosphering\_speed, crew, passengers, cargo\_capacity |
| Vehicles | `vehicle` | name, model, manufacturer, cost\_in\_credits, length, max\_atmosphering\_speed, crew, passengers |

---

### 3 — Inline Hamcrest body validation (resource-specific groups)

Two complementary Hamcrest tests per resource:

**`getById_bodyHasCommonFields()`** — shared in `AbstractResourceTest`, validates that
`url`, `created`, and `edited` (fields from `BaseModel`) are non-null for every resource.

**`verifyResponseBody()`** — per concrete class, validates all resource-specific fields
directly against the raw JSON response using the RestAssured fluent chain:

```java
response.then()
    .statusCode(200)
    .body("name",    equalTo(SwapiProperties.peopleName()))
    .body("films",   not(empty()))
    .body("homeworld", notNullValue());
```

Available matchers in use:

| Matcher | Purpose |
|---------|---------|
| `equalTo(value)` | Exact field value match against `swapi.properties` |
| `notNullValue()` | Field is present and non-null |
| `not(empty())` | List field contains at least one element |

---

### 4 — Pagination tests (group: `pagination`)

Available for: **People, Planets, Starships, Vehicles**.

| Test | What it verifies |
|------|-----------------|
| `verifyDefaultPageSize()` | `results.size() == 10` and `count > 10` (confirms multiple pages exist) |
| `verifyCustomLimitBehavior()` | Sending `?limit=5` returns 10 records (SWAPI ignores unknown parameters) |

The `?limit=` parameter is **not supported** by swapi.dev. These tests document that
behaviour as a contract: the API silently ignores the parameter and always returns the
default page of 10 records.

Parameterized requests use `BaseEndpoint.getAll(Map<String, Object> queryParams)`:

```java
// Internally calls: RestAssured.given().spec(...).queryParams(params).get(path)
Response r = people.getAll(Map.of("limit", 5));
```

---

### 5 — Performance tests (group: `performance`)

Shared via `AbstractResourceTest` — one test covers all six resources automatically.

```
verifyResponseTime()  →  endpoint().getAll()  →  r.getTime()  →  classify  →  assert not Slow
```

Uses `response.getTime()` from RestAssured (actual HTTP round-trip, not TestNG overhead).
Fails only when the server response exceeds 2 000 ms.

**Performance rating thresholds** (`PerformanceRating`):

| Response time | Rating |
|---------------|--------|
| < 200 ms | Excellent |
| 200 – 1 000 ms | Good |
| 1 001 – 2 000 ms | Acceptable |
| > 2 000 ms | Slow *(test fails)* |

---

### 6 — Security tests (group: `security`)

Shared via `AbstractResourceTest` — one test covers all six resources automatically.

The test probes each endpoint with an XSS payload (`<script>`) embedded in the URL path
and asserts that the server returns **404 Not Found**, confirming the input is not
processed, reflected, or executed.

**Request format:**
```
GET https://swapi.dev/api/{resource}/<script>
```

**Implementation detail — URL encoding:**  
The payload is passed as a RestAssured path parameter, which triggers automatic
URL-encoding before the request is sent. The character sequence `<script>` is
transmitted as `%3Cscript%3E` on the wire — SWAPI receives a lookup for a
non-existent resource ID and responds with 404.

```java
// BaseEndpoint.getByPath("<script>")
// → GET /api/people/%3Cscript%3E  →  404 Not Found
endpoint().getByPath("<script>");
assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND.code());
```

The report shows one row per resource:

| Endpoint | Expected |
|----------|----------|
| `People :: XSS injection in path → 404` | 404 |
| `Films :: XSS injection in path → 404` | 404 |
| `Planets :: XSS injection in path → 404` | 404 |
| `Species :: XSS injection in path → 404` | 404 |
| `Starships :: XSS injection in path → 404` | 404 |
| `Vehicles :: XSS injection in path → 404` | 404 |

---

## `@Story` annotation

`@Story` (package `com.swapi.framework.reporting`) provides human-readable labels that
replace Java class/method names in the HTML report.

```java
// class level → replaces the left side of "::" in the Endpoint column
@Story("People")
public class PeopleTest extends AbstractResourceTest { ... }

// method level → replaces the right side of "::"
@Story("name")
@Test
public void verifyName() { ... }
// Report shows: "People :: name"
```

Resolution priority:

| Scope | Fallback |
|-------|---------|
| Method `@Story` | method name |
| Class `@Story` | extracted class name (e.g. `PeopleTest` → `People`) |

---

## HTML Report

`HtmlReporter` (a TestNG `IReporter`) generates a self-contained HTML file at
`target/swapi-report.html` after each suite run.

### Summary cards

- **Tests Summary** — donut chart with pass / fail / skipped counts.
- **Pass Percentage** — numeric indicator with a progress bar.

### Endpoints table

Each row represents one `@Test` method.

| Column | Source | Notes |
|--------|--------|-------|
| **Endpoint** | `@Story` class + method | Falls back to class name + method name |
| **Result** | TestNG status | Color-coded badge: green / red / amber |
| **Status Code** | `ResponseCapture` via `StatusCodeListener` | HTTP code from the last request in that method; falls back to `@BeforeClass` code for assertion-only tests |
| **Time** | TestNG method duration | From `getEndMillis() - getStartMillis()` |
| **Performance** | `PerformanceRating.classify(durationMs)` | Color-coded badge: green / blue / amber / red |

### Status Code propagation

`StatusCodeListener` bridges `ResponseCapture` to TestNG result attributes:

- After every `@BeforeClass`, the captured HTTP status code is stored as a thread-local fallback.
- After every `@Test`, the method-level code is used if an HTTP call was made; otherwise the `@BeforeClass` code is used as fallback.
- This ensures that model-assertion tests (`verifyName`, `verifyHeight`, …) always display the `200` from the fetch even though they make no requests of their own.

### Performance badge colours

| Rating | Badge colour |
|--------|-------------|
| Excellent | Green |
| Good | Blue |
| Acceptable | Amber |
| Slow | Red |

No external dependencies — chart uses CSS `conic-gradient` and all styling is inline;
the file opens correctly offline.

---

## Corporate proxy / network setup

`BaseTest.globalSetup()` reads the standard POSIX proxy environment variables
(`HTTPS_PROXY`, `https_proxy`, `HTTP_PROXY`, `http_proxy`) in the same priority
order that `curl` uses and calls `RestAssured.proxy(host, port)` automatically.
The suite routes through a corporate proxy on any machine where those variables are
set, with no configuration change required.

`SpecFactory` additionally applies a **10-second connection timeout** and a
**30-second socket timeout** so network problems surface quickly instead of
hanging for the OS-level TCP default (≈ 90 s on Windows).

The Surefire JVM starts with `-Djava.net.preferIPv4Stack=true` to avoid dual-stack
IPv6 fallback delays on Windows networks.

---

## Design notes

- **API Object Model** — endpoint objects only build and send requests and return the raw
  `Response`; all assertions live in the test layer.
- **One `@BeforeClass` per resource** — a single HTTP call fetches the object for all
  field-level and Hamcrest `@Test` methods in that class; assertions share the cached
  model and `Response` with no additional network traffic.
- **Three validation styles** — deserialization assertions (`assertEquals` via model
  getters), Hamcrest matchers (`.then().body()` chain on the raw response), and JSON path
  access (`response.jsonPath().getList("results")`) can all coexist in the same class.
- **`SwapiProperties`** — reads `swapi.properties` once at class-load time (`static final`);
  callers use typed helpers (`peopleName()`, `filmsEpisodeId()`, …).
- **`@Story` + `HtmlReporter`** — the annotation provides a stable, human-readable label
  that survives method renames; the reporter resolves it at generation time using reflection.
- **`ResponseCapture`** — a `ThreadLocal<Integer>` set by every HTTP verb in `BaseEndpoint`
  and cleared before each method by `StatusCodeListener`. The listener propagates the
  `@BeforeClass` code as a fallback so every test row in the report has a meaningful status.
- **Trailing slashes on paths** — SWAPI runs on Django REST Framework, whose slash-redirect
  can downgrade a POST to a GET. The trailing slash prevents the redirect.
- **`403` vs `405`** — SWAPI returns `403 Forbidden` for write verbs on a read-only API.
  If you retarget the suite at an API that uses `405 Method Not Allowed`, update the
  negative assertions — `HttpStatus` already defines both constants.
- **Performance measurement** — `response.getTime()` (RestAssured) measures the actual HTTP
  round-trip and is used in performance assertions. The HTML report's Performance column
  uses TestNG's method duration (`endMillis - startMillis`) which is a close approximation
  for tests with a single HTTP call.
- **XSS injection probe** — `BaseEndpoint.getByPath(String segment)` passes the segment as
  a RestAssured path parameter so special characters (`<`, `>`) are URL-encoded to
  `%3C` / `%3E` before the request leaves the JVM. The test asserts `404 Not Found`,
  confirming the server treats the payload as an unknown resource rather than executing
  or reflecting it. `HttpStatus.NOT_FOUND(404)` was added to the enum to keep the
  assertion readable.

---

## Requirements

| Tool | Minimum version |
|------|-----------------|
| JDK | 17 (tested on 21) |
| Maven | 3.9 |
