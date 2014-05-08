Contributions to Wayward are welcome - we will happily guide you through the steps required to hack at the plugins and make any changes you want to.
In order to keep code tidy, here are a few guidelines:

Code
----

* If you make a modification to WaywardLib, make sure to add JavaDoc comments! This makes sure that other WaywardLib implementations know how to implement it, but more importantly, other developers know what the method does.
* If you make a standard component, make sure to write an interface for it - this allows a higher level of abstraction and allows us to freely switch out components at will.
* If you find yourself using something often, and think someone else might too - for example, a serialisation utility or a parsing utility for location arguments - add a utility class in WaywardLib
* No weird mid-line breaks - please continue along the same line. For small PRs, we're happy to tidy this up, but it becomes a pain when you've added a lot of code
* Use spaces over tabs.

Commits
-------

* One change per commit. This allows for nice commit messages that outline what you've changed. It's better you split something into many commits than combine them into huge commits with a generic commit message.

Branches
--------

* Keep branches topic-related! That means no strange commits changing things unrelated to the branch topic.
* We use [nvie's branching model](http://nvie.com/posts/a-successful-git-branching-model/)
* Feature branches should be prefixed with feature/
* Bugfix branches should be prefixed with bugfix/
* Hotfix branches should be prefixed with hotfix/
* Release branches should be prefixed with release/

That said, these are guidelines: don't let them put you off. If you have any code you want to submit, go for it. We're happy you've taken the time to help us implement new features & fix bugs.
We'll help you sort out any issues with your code before merging them into master.
