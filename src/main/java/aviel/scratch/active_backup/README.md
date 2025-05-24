# Multi level architecture

This package is made out of 3 levels of abstractions.
Each such level is a state machine over a finite set of states and transitions between them.
Each states may hold additional data that may be modified during the transitions.
Transitions may accept arguments.
Transitions are often named `on*` such as `onAmStrongest` or `onActive`.
States are represented by interfaces while transitions are represented by interface methods.

### world_events

This is the most primitive-concrete level, and they correspond to the most basic events
that are needed in order to eventually compute the activeness state.

### competition_events

This is a middle level.
It acts as a transition between the `world_events` and `active_backup_events` levels.
This level is responsible to realizing whether the component is awake or dormant (that is upon creation before alarm
went off) and whether this component is leading the activeness competition (is it the strongest).

### active_backup_events

This is the most abstract level.
This level corresponds to the actual activeness state and consequently constitutes two states, `Active` and `Backup`,
as well as two events, `onActive` and `onBackup`.

## translations

An abstract level is often translated into a concrete level, adding an abstraction the details of implementation through
the language of the concrete level.
Those translations are represented by classes that implement the events of the concrete level, while the abstract level
is help as a field and passed as an argument constructor to the translation.

## code base

Here we have