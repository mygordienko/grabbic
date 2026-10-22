# Grabbic (WIP)

Handy clipboard sharing app

## Things to note

* Room's Flows are cold, but it does not mean it won't push updates do consumers. 
    If there is a collector(subscriber) Room will watch for DB changes in relevant tables and
    push updates (if any)
    When navigating between screens (navController), Compose won't run `init{}` method second time 
    for already initialized viewmodel, ex: navigating back to ListView from AddNewItemView. That
    means if we want to see updates after navigating back, we have two options:
    * Reactive: Use `StateFlow<>` on Screen side which can be obtained from cold `Flow<>` by using
        the `stateIn()` method (see `ClipSourceSelectionViewModel`)
    * Traditional: Make viewModel expose dedicated `reloadData()` method and call it from the screen
        from within `LifecycleResumeEffect`