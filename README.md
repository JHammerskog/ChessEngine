# ChessEngine

This is the repository for my 2019 masters project - aiming at creating a Chess Engine capable of solving endgame puzzles.

In its final iteration, it was able to solve all King-Rook-King endgames - and most King-Queen-King endgames.

# Setup

Currently the project requires any Java IDE to run.

# Project Abstract:

Chess is a game with an astronomical depth and branch factor of search trees,
which makes it fundamentally difficult to implement respectable computer play
for any position. This fact holds true for endgame positions due to the pieces being
able to move freely around the board. Relying on brute-force methods in these
situations can be unnecessarily time-consuming, as the algorithms may have to
iterate through trillions of positions in order to find a desirable outcome
(checkmate). This paper will outline specific heuristics geared towards solving
specific endgame situations which are able to generate moves quickly. The paper
will evaluate various ways of approaching the problem of time consumption by
chess solving applications, and then implement its own specific heuristics to solve
the tackled endgame puzzles. Heuristics for King-Rook vs. King and King-Pawn
vs. King endgames were completed and found to reach checkmate whilst
simultaneously generating moves very quickly (<25ms).
