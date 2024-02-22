# PACMAN IN THE MULTIVERSE

Arcade 24™ (A24), the modern game company has stepped in to fill this need with their new game
design, PacMan in the Multiverse. Instead of having ghosts like a classic PacMan game, A24 wants
various monsters. In addition to pills, new items (ice cube and gold pieces) will be placed into the maze.
These new items are intended to provide additional behaviours of the gameplay. A previous team of A24
has developed a simple version of the PacMan in the Multiverse; their code works with limited features.
The code includes some documentation (code comments and partial design class diagram), but
unfortunately it was built in a rush and was poorly designed. A24 has recruited you and your team to
revise the existing design and codebase and extend the simple version of PacMan in the Multiverse to
include the features that will make the game more exciting.

<img width="511" alt="image" src="https://github.com/Sanskar-Agarwal/PacMan-In-Multiverse/assets/86827884/d30a9b3b-99a7-43b2-a379-1ff59ea8c5d4">

# 1. Use Case: Playing PacMan in the Multiverse
Section 1.1 provides the use case text describing the simple version of PacMan in the Multiverse that is
already built and provided in the codebase. Section 1.2 provides the A24’s proposed extensions. The
extensions have not yet been built and will be detailed further in the specification.
1.1 Playing PacMan in the Multiverse
Main Success Scenario:
1. The player opens the game which initiates and places PacMan, pills, ice cubes, gold pieces, and
monsters (one Troll and one T-X5) into the map. The initial locations of PacMan and monsters are
configurable.
2. The game moves the monsters automatically within the maze. A monster will move one cell at a
time. A monster determines a next location to which it will move based on a given direction. The
direction can be specified based on compass directions (e.g., East) or degrees clockwise (0 – 360
where 0 is East and 270 is North). For example, in Figure 2, the current direction of the monster (a
red arrow) is towards East (or 0). To move to the bottom cell (i.e., going down; a blue arrow), the
direction should be set to South or 90.
3. The player controls PacMan with keyboard actions to walk in the maze. The Left and right keys move
PacMan horizontally. The Up and down keys move PacMan vertically.
Repeat steps 2-3

<img width="665" alt="image" src="https://github.com/Sanskar-Agarwal/PacMan-In-Multiverse/assets/86827884/1ead34a1-aae8-41bc-80cc-0340d90197ff">

# Alternate Scenarios
2a., 3a. If there is a collision between PacMan and one of the monsters (i.e., being on the same
location), PacMan explodes and the game is over.
3b. When PacMan eats a pill (being on the pill’s location), the pill disappears and the current score on
the title bar increases by 1 point.
SWEN30006 Software Modelling and Design—Sem 1 2023 © University of Melbourne 2023
Page 3 of 7
3c. When PacMan eats a gold piece (being on the gold piece’s location), the gold piece disappears and
the current score on the title bar increases by 5 points.
3d. When PacMan eats an ice cube (being on the ice cube’s location), the ice cube disappears but the
current score doesn’t increase.
3e. When PacMan eats all the pills and gold pieces, the player wins the game.
*a. At any stage, the player exits the game.

<img width="703" alt="image" src="https://github.com/Sanskar-Agarwal/PacMan-In-Multiverse/assets/86827884/0f9fd38a-d775-4df6-a176-ee21bd8a0f5b">

# Feature 2: Additional Capabilities of Items
Extends Playing PacMan in the Multiverse
3c. When PacMan eats a gold piece, all the monsters get furious and move fasters. The monsters
determine the moving direction once based on their walking approach and move towards that
direction for 2 cells if they can. Otherwise, determining the new direction again using their own
walking approach until it can move by 2 cells. After 3 seconds, all the monsters will be back to move
normally using their own walking approach.
3d. When PacMan eats an ice cube,
3d.1. Regardless of being normal or furious, all the monsters are frozen (i.e., stop moving) for 3
seconds. Then, they will be back to move normally using their own walking approach.
3d.2. While the monsters are frozen, PacMan can eat a gold piece without making the monsters
furious.

# Team Members
- Sanskar Agarwal (<sanskara@student.unimelb.edu.au>)
- Elise Marcun (<emarcun@student.unimelb.edu.au>)
- Lara Stebbens (<lstebbens@student.unimelb.edu.au>)

NOTE: A24 acknowledges that the descriptions of the new features above may not cover all the possible
cases of the game logic. As long as the described logics is in place, A24 is open to your ideas to handle
the corner cases that are not covered by the provided use cases.


