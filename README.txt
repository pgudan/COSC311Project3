This program was written by Paul Gudan for the EMU Computer Science course COSC 311.

This program simulates a call center modem bank. There are two types of queues for callers, a priority queue
and a regular queue. The program takes input from the user for the simulation length, average time between
dial-in attempts, average connection time (call length), number of modems in the modem bank, and the size of
the waiting queue.

The time between dial-in attempts and the connection times are determined using a Poisson distribution,
based on the user averages entered. At the end of the simulation, the results are outputted to the screen
and to the file report.txt. These results include the simulation parameters, the percentage of modem usage,
the average wait time, and the number of users in the queue at the end of the simulation.