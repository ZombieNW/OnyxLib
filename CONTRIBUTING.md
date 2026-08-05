# Contributing to OnyxLib

Thank you for being interested in contributing to OnyxLib!

To ensure this project remains focused and maintainable, I've put forth some guidelines on what does and does not belong in OnyxLib. Please read this document before submitting a PR to prevent a conflict in vision.

## Core Philosophy (Goals)

OnyxLib is a framework for creating custom content using PaperMC, specifically focused on streamlining asset management.

To support contribution and success of the project, here are some examples of things that would be nice:

- **Improvements:** Optimizations, bug fixes, performance bottlenecks, or enhancements to existing core features like the resource pack generator or API implementation. Issues that make OnyxLib look like the worse option for someone's project should be addressed.
- **Quality of Life:** Things that enhance developer or administrator workflow without hindering OnyxLib or PaperMC's core features.

## What is Out of Scope? (Anti-Goals)

**OnyxLib is not a full abstraction layer.**
The goal is to abstract the painful parts of custom content (like asset management) while staying out of the developer's way basically everywhere else. _If PaperMC already provides a way to do something, we do not want to re-invent the wheel, even if our way is easier._

To further prevent scope-creep and bloat, here are some examples of things that would likely be rejected:

- **Wrappers & Generators:** We do not want to re-invent the wheel, things already implemented in PaperMC should be left for the end-developer to use.
- **Logic Archetypes:** We do not want to provide rigid, pre-packaged item templates (e.g., `OnyxWeapon`, `OnyxFood`, or `OnyxTool`). Custom behavior and event listening are the responsibility of the end-developer using listeners or `itemMeta`.
