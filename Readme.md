# AEEI Game Jam Winter 2012
At the beginning of the competition, each team received a pair of randomly generated game styles and a pair of themes to drive their game design. Our team received the following constraints:

#### Styles
* Shooter
* Strategy

#### Themes
* Jungle
* Post-apocalyptic

Using these, we created an action / puzzle game where two players competed over the network to feed their base with energy using mirrors and other items. The game takes place in a post-apocalyptic world, where energy is a rare resource and where tribes do all they can to gather this energy and destroy other civilizations. They can do this by powering a central factory, which in turn powers up a cannon to fire at the enemy (the cannon does not appear in the final game; the goal is to gather enough power in our central factory).

At the beginning of the game, two yellow beams emanate from a single, unmovable object in the center of the level. Players must use mirrors to reflect this beam to their base and use it to power factories, which in turn create new items from this energy. The thing is, each factory demands a specific colored beam, and each items help the player to achieve his goal. The spawnable items are the following:

* Mirrors : Always spawning, infinite number or available mirrors.
* Lens : Changes the color of a beam.
* Concentrator : Concentrate the energy of a beam to create a more powerful one (beams hurt the player more and generate items faster when concentrated).
* Splitters : Split a beam in two smaller beams but lowers the beam power if it was enhanced.
* Hearts : Fully “heal” the player. If a player “dies”, it takes time to respawn.

The players must make use of strategy to make a clever use of their items (or  their enemy's) and power their central factory as quickly as they can.
