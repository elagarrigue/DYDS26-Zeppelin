---
name: Conventional Commits Skill
description: Use this skill to write git commit messages that follow the Conventional Commits 1.0.0 specification.
---

# Conventional Commits Skill

## When to use
- Writing or reviewing commit messages for this repository.

## Commit format
Use this structure:

`<type>[optional scope]: <description>`

## Rules
- Write the type in lowercase.
- Keep the description short, specific, and in the imperative mood.
- Do not end the subject with a period.
- Use a scope only when it adds clarity.
- Keep each commit focused on one cohesive change.

## Common types
- `feat`: a new user-facing feature.
- `fix`: a bug fix.
- `docs`: documentation-only changes.
- `style`: formatting or whitespace-only changes.
- `refactor`: code changes that do not alter behavior.
- `perf`: performance improvements.
- `test`: test additions or updates.
- `build`: build system or dependency changes.
- `ci`: continuous integration changes.
- `chore`: maintenance work that does not fit another type.

## Examples
- `feat(home): add movie filtering`
- `fix(detail): handle missing poster image`
- `docs: update project setup instructions`
- `refactor(viewmodel): extract movie mapping helper`

## Breaking changes
If a change is breaking, add `!` after the type or scope and include a `BREAKING CHANGE:` footer with a short explanation.

## Recommended process
1. Identify the single conceptual change.
2. Choose the most accurate type.
3. Add a scope only when it improves clarity.
4. Write a concise subject line.
5. Add a body only when extra context is useful.

## Do
- Keep commits small and readable.
- Prefer one clear intent per commit.
- Use the commit message to describe the change, not the implementation details.

## Do not
- Mix unrelated changes into one commit.
- Use vague subjects like `update` or `changes`.
- Capitalize the type or add punctuation to the end of the subject.
