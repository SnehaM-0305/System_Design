 #   WHEN TO KEEP WHAT IN WHERE 
If an object is just updating its OWN list/field → keep the method on that object.
If something needs to touch a big shared map/storage that many objects share → that goes in the service.

# Why not Strategy pattern for search:

Strategy pattern is for when you have ONE task, but many DIFFERENT ways to do it, and you want to switch between those ways while the program is running.

Search here isn't like that. searchByKeyword, searchByTag, searchByUser are just 3 separate, fixed jobs. Nobody switches between them — a user just picks one directly. So they're just 3 normal methods in one class. No need for extra pattern.

When Strategy WOULD make sense: if you later say "let user choose how results are ranked — by newest, by votes, by relevance" — now that's one job (ranking) with swappable ways to do it. That's a real Strategy pattern case.

Simple rule: only use a pattern when it actually saves you something. If removing the pattern doesn't break anything or make things harder, you don't need it.

# General rule for interviews: 
don't reach for a pattern because the name sounds applicable — reach for it when you can point to the specific flexibility it buys you (runtime swapping, open-for-extension without modifying existing code, decoupling an algorithm family from its caller). If you can't articulate what breaks without the pattern, you probably don't need it yet — and interviewers notice when patterns are bolted on rather than earned.