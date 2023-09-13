# Freecell Solver
This Java program provides a FreeCell generator, as long as, a solver that can solve the generated boards using various algorithms.

## Table of Contents
- [Features](#features)
- [Usage](#usage)
- [Disclaimer](#disclaimer)
- [Contributing](#contributing)

## Features

- Generate random FreeCell game boards.
- Solve Freecell boards using one of the following algorithms:
  - Astar
  - Best
  - Breadth
  - Depth

## Usage

Here is how to use the Generator:
- java Generator [prefix] [number] [maxValue]
  - `prefix`: The prefix of the generated .txt files.
  - `number`: The number of .txt files to be generated.
  - `maxValue`: The maximum value of cards (must be set to 13 for a complete FreeCell game).


 Here is how to use the Solver:
 1. Before using the solver, make sure that in the Constants class you define the proper number for the N constant. It must be the same as the one used to generate the board of the input file (maxValue).
 2. java Solve [method] [input-file] [output-file]
    - `method`: Choose the algorithm to solve the board (astar, best, breadth, depth).
    - `input-file`: The generated .txt file that contains the board to be solved.
    - `output-file`: Provide the name of the .txt file that would be created containing the solution.

## Disclaimer

**Please Note:** This FreeCell solver program, while functional, has certain limitations that users should be aware of:

- **Algorithm Efficiency:** The efficiency of the solving algorithms in this program may not be optimal. Specifically:
  - The **Breadth-First Search Algorithm** may struggle to find solutions in most cases due to memory exhaustion, especially for complex FreeCell game boards.
  - The **Depth-First Search Algorithm** is suitable for smaller problems (n < 5) but may not be efficient for larger game boards.
  - While the Astar and Best-First Search Algorithms are capable of finding solutions, they may produce solutions that include unnecessary card movements.

- **Heuristic Function:** The heuristic function used by the A* and Best-First Search Algorithms for evaluating and ranking game board states can be further improved. As a result, the solutions provided by these algorithms may not always be optimal.

Please keep these limitations in mind when using this FreeCell solver.

## Contributing

Contributions to this project are welcome! Feel free to submit issues, suggest improvements, or contribute code to enhance the functionality of the Freecell Solver.
