# AI-Assisted Development Process

## Tool Used
Claude Code — Anthropic's CLI agent for software engineering tasks.

## How the conversation evolved

The solution was developed iteratively through a back-and-forth conversation. Each step refined
the design based on explicit feedback and challenges.

**Step 1 — Initial generation**
Started with a broad prompt describing the car rental requirements. The AI produced a working
solution with all classes in a single `org.carrental` package, using plain classes with
getters/setters, and single-letter variable names in tests.

**Step 2 — Records and naming**
Pushed back on: flat package structure, no use of Java records, lazy variable names like `a`
and `r`. The AI refactored `Reservation` into an immutable record, renamed test variables to
be descriptive (e.g., `janFirstToFourth` instead of `a`), and split into `model` + `service`
packages.

**Step 3 — Hexagonal-lite architecture**
Pushed back on: everything still under `org.carrental.model` — not domain-separated. Asked for
hexagonal-lite with core domains that don't import other packages. The AI restructured into
`car/` (core domain) and `reservation/` + `rental/` packages, introducing `BookedPeriod` to
decouple `Car` from `Reservation`.

**Step 4 — Merging rental and reservation**
The AI proactively questioned why `rental` and `reservation` were separate packages — they only
had one class each and `Reservation` was just the output of the rental operation. Agreed and
merged them. Also dropped `Reservation.overlapsWith()` since overlap logic already lived in
`Car` via `BookedPeriod`.

**Step 5 — Repository pattern and thread safety**
Explored adding a `CarRepository` interface with an in-memory implementation, and making the
system thread-safe with `synchronized` and `ReentrantLock`. Both were built, tested, and
discussed.

**Step 6 — Simplification for assessment scope**
Decided that the repository pattern and thread safety were overengineering for the scope of the
task. Stripped back to the essentials — every line of code justified by a requirement, with gaps
documented for discussion.

**Step 7 — Test refinement**
Renamed tests to `should...` pattern, reformatted bodies to given/when/then blocks, added
explicit fleet-size-per-type test.

## How AI output was validated

- **Tests after every change** — `mvn test` was run after each refactoring step. No change was
  accepted without a green build.
- **Design challenges** — each structural decision was questioned
- **Incremental refinement** — rather than generating the final solution in one shot, each
  prompt targeted a specific concern. This made it easier to review and understand each change.
