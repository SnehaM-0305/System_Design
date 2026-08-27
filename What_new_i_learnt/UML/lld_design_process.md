# How to approach any LLD problem — step by step

This is a general process for designing a Low Level Design (LLD), demonstrated using the Stack Overflow example, but it generalizes to any LLD question — parking lot, splitwise, tic-tac-toe, elevator system, etc.

---

## Step 1: Extract the nouns and verbs from requirements

Read the requirements and physically list them out in two columns.

| Nouns (become classes) | Verbs (become methods) |
|---|---|
| User, Question, Answer, Comment, Tag, Vote | post, answer, comment, vote, search, calculate reputation |

Every noun is a *candidate* class. Every verb is a *candidate* method. This step grounds the design in what the problem actually said, instead of designing in the abstract.

---

## Step 2: Separate "things" from "actions the system does"

This is the step people skip, and it's why designs turn into God objects. Ask of each noun: *is this a piece of data, or is this a coordinator?*

- **Data things (entities)**: `User`, `Question`, `Answer`, `Comment`, `Tag` — they hold state, describe something real in the domain.
- **Coordinators (services)**: nothing in the requirements literally says "reputation service" or "search service" — but requirement 4 ("search by keyword/tag/user") and requirement 5 ("assign reputation based on rules") are both *behaviors that span multiple entities*. That's the signal a service is needed, even though the requirements never name it directly.

**Rule of thumb:** if satisfying a requirement means touching more than one entity's data or a shared collection, it's a service. If it only needs the entity's own fields, it's a method on that entity.

---

## Step 3: Find repeated behavior across entities → pull into interfaces

Look at the entity list and ask: *do two or more of these need the same kind of behavior?*

- Both `Question` and `Answer` can be voted on → `Votable` interface.
- Both `Question` and `Answer` can be commented on → `Commentable` interface.

Only extract an interface when **behavior is shared across multiple classes**. If only one class needs it, it's just a method on that class — don't make an interface for a single implementer.

---

## Step 4: Find fixed, closed sets of values → make them enums

Ask: *is there a small, fixed list of options that will rarely change and carries no independent behavior of its own?*

- Vote can only be UPVOTE or DOWNVOTE → `VoteType` enum.
- Reputation events are a fixed list (question upvote, answer upvote, answer accepted...) → `ReputationType` enum.

**The test that tells enum vs. class:** if the "type" doesn't need its own methods, fields beyond a constant value, or subtype-specific overriding behavior — it's an enum, not a class hierarchy. (This is exactly the mistake the original Stack Overflow diagram made — modeling `ReputationType` and `VoteType` as class hierarchies instead of enums.)

---

## Step 5: Draw relationships between entities — ask "can this exist without that?"

For every pair of related entities, ask:

- **Can A exist without B?** If no → **composition** (filled diamond). Example: an `Answer` can't exist without its `Question`.
- **Can A exist without B, and can B be shared by others?** → **aggregation** (hollow diamond). Example: a `Tag` can exist independently and be reused across many questions.
- **Does A just need a reference to B, with independent lifecycles?** → **plain association** (arrow). Example: `Question` references its `User` author — deleting the question doesn't delete the user.
- **Does A behave like a B / must implement B's contract?** → **realization** (dashed hollow triangle). Example: `Question implements Votable`.

---

## Step 6: Design the service layer last, not first

Once entities + interfaces + enums are solid, ask what's left un-homed from the verb list in Step 1: creating things, searching, scoring, coordinating concurrency. Those become the service classes — and if a service is doing too many unrelated jobs (storage + search + scoring), split it. In the Stack Overflow example, this meant splitting the monolithic `StackOverflow` class into `StackOverflowService` (storage/orchestration) + `SearchService` (search) + `ReputationManager` (scoring).

---

## Step 7: Sanity-check with the requirements list again

Go back to the original requirements one at a time and confirm each is satisfiable by walking through the classes — build a requirement → design mapping table. If a requirement doesn't map cleanly to something in the diagram, a class or method was missed.

---

## Summary of the order

1. Nouns/verbs from requirements
2. Data entities vs. coordinating services
3. Shared behavior → interfaces
4. Fixed value sets → enums
5. Relationships between entities (composition / aggregation / association / realization)
6. Service layer, split by responsibility
7. Re-check against requirements

Follow this same order for any LLD problem — parking lot, elevator, splitwise, tic-tac-toe — the nouns/verbs change but the decision process doesn't.
